package com.tulin.v8.editors.vue.editor;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;

import com.tulin.v8.core.FileAndString;
import com.tulin.v8.webtools.ide.html.HTMLUtil;

/**
 * 验证VUE文件内容
 */
public class ValidateVueCode {
	private IFile file;

	public ValidateVueCode(IFile file) {
		this.file = file;
	}

	public void doValidate() {
		try {
			String content = FileAndString.IFileToString(file);

			// 检查是否存在<template>标签
			if (!content.contains("<template>")) {
				System.err.println("Vue文件缺少<template>标签");
				HTMLUtil.addMarker(file, IMarker.SEVERITY_ERROR, 1, "Vue文件缺少<template>标签");
			}

			// 检查是否存在<script>标签
			if (!content.contains("<script>")) {
				System.err.println("Vue文件缺少<script>标签");
				HTMLUtil.addMarker(file, IMarker.SEVERITY_WARNING, 2, "Vue文件缺少<script>标签");
			}

			// 使用正则表达式检查其他语法和规范
			// 例如，检查是否存在特定的Vue指令或标签
//			Pattern pattern = Pattern.compile("<[a-zA-Z]+");
//			Matcher matcher = pattern.matcher(content);
//			while (matcher.find()) {
//				String tag = matcher.group();
//				System.out.println("找到标签：" + tag);
//			}
		} catch (Exception e) {
		}
	}

}
