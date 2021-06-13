/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import kry.sql.format.SqlFormatRule;
import kry.sql.tokenizer.TokenUtil;

import org.eclipse.jface.text.templates.Template;

import zigen.plugin.db.preference.SQLTemplateEditorUI;
import zigen.plugin.db.ui.views.internal.SQLContextType;

public class DbPluginFormatRule {

	private static DbPluginFormatRule instance;

	private SqlFormatRule fRule;

	private String[] fDefaultKeywords;

	private String[] fDefaultDataTypes;

	private String[] fDefaultFunctions;

	private List fFunctionList;

	private Template[] templates;

	public synchronized static DbPluginFormatRule getInstance() {
		if (instance == null) {
			instance = new DbPluginFormatRule();
		}
		return instance;
	}

	private DbPluginFormatRule() {
		fRule = new SqlFormatRule();
		fRule.setRemoveEmptyLine(true);
		fDefaultFunctions = fRule.getFunctions();


		fDefaultKeywords = TokenUtil.KEYWORD;
		fDefaultDataTypes = TokenUtil.KEYWORD_DATATYPE;

		margeTemplate();
	}

	public void margeTemplate() {
		fFunctionList = new LinkedList(Arrays.asList(fDefaultFunctions));

		templates = SQLTemplateEditorUI.getDefault().getTemplateStore().getTemplates(SQLContextType.CONTEXT_TYPE_FUNCTION);
		for (int i = 0; i < templates.length; i++) {
			Template template = templates[i];
			String func = template.getName().toUpperCase();
			if (!fFunctionList.contains(func)) {
				fFunctionList.add(func);
			}
		}
		fRule.setFunctions((String[]) fFunctionList.toArray(new String[0]));

	}

	public String[] getFunctionNames() {
		return (String[]) fFunctionList.toArray(new String[0]);
	}

	public String[] getKeywordNames() {
		return fDefaultKeywords;
	}

	public String[] getDataTypeNames() {
		return fDefaultDataTypes;
	}


	public void setDefaultFunction() {
		fRule.setFunctions(fDefaultFunctions);
	}

	public SqlFormatRule getRule() {
		return fRule;
	}

	public void addFunctions(String[] addFunctions) {
		fRule.addFunctions(addFunctions);
	}

	public void subtractFunctions(String[] subtractFunctions) {
		fRule.subtractFunctions(subtractFunctions);
	}

	public void setFunctions(String[] functions) {
		fRule.setFunctions(functions);
	}

}
