package com.tulin.v8.editors.vue.editor;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.eclipse.core.resources.IFile;

import com.tulin.v8.core.FileAndString;

public class ValidateVueCode {
	private IFile file;

	public ValidateVueCode(IFile file) {
		this.file = file;
	}

	public void doValidate() {
		try {
			String code = FileAndString.IFileToString(file);
			// 构建命令行
			String[] command = { "node", "-e", "require('eslint').lintText('" + code + "')" };

			// 执行命令行
			ProcessBuilder processBuilder = new ProcessBuilder(command);
			Process process = processBuilder.start();

			// 读取命令行输出
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			StringBuilder output = new StringBuilder();
			while ((line = reader.readLine()) != null) {
				output.append(line).append("\n");
			}

			// 等待命令行执行完成
			int exitCode = process.waitFor();

			// 输出验证结果
			if (exitCode == 0) {
				System.out.println("代码验证通过！");
			} else {
				System.err.println("代码验证失败：");
				System.err.println(output.toString());
			}
		} catch (Exception e) {
		}
	}

}
