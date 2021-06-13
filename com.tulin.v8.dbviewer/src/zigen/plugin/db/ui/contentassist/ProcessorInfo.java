/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.contentassist;

import zigen.sql.parser.INode;

public class ProcessorInfo {

	private String wordGroup;

	private String word;

	private int offset;

	private boolean isAfterPeriod;

	private int currentScope;

	private INode currentNode;

	public INode getCurrentNode() {
		return currentNode;
	}

	public void setCurrentNode(INode currentNode) {
		this.currentNode = currentNode;
	}

	public int getCurrentScope() {
		return currentScope;
	}

	public void setCurrentScope(int currentScope) {
		this.currentScope = currentScope;
	}

	public boolean isAfterPeriod() {
		return isAfterPeriod;
	}

	public void setAfterPeriod(boolean isAfterPeriod) {
		this.isAfterPeriod = isAfterPeriod;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getWordGroup() {
		return wordGroup;
	}

	public void setWordGroup(String wordGroup) {
		this.wordGroup = wordGroup;
	}

}
