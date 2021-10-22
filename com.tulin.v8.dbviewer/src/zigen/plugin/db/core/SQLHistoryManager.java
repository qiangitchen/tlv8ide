/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.core;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.preference.IPreferenceStore;

import zigen.plugin.db.DbPlugin;
import zigen.plugin.db.DbPluginConstant;
import zigen.plugin.db.DefaultXmlManager;
import zigen.plugin.db.preference.PreferencePage;
import zigen.plugin.db.ui.internal.History;
import zigen.plugin.db.ui.util.FileUtil;

public class SQLHistoryManager extends DefaultXmlManager {

	private int maxSize = 100;

	private List history = null;

	int currentPosition = 0;

	private IPreferenceStore preferenceStore;

	public SQLHistoryManager(IPath path) {
		super(path, DbPluginConstant.FN_SQL_HISTORY);

		this.preferenceStore = DbPlugin.getDefault().getPreferenceStore();

		history = load();
		if (history == null) {
			// history = new ArrayList();
			history = new LinkedList();
			history.add(new SQLHistory(true)); // blank
		}

	}

	public List load() {
		try {
			Object obj = super.loadXml();
			if (obj instanceof List) {
				history = (List) obj;
				if (history.size() > 0) {
					currentPosition = history.size();
				}
				// history.add(currentPosition, new SQLHistory(true));
				history.add(new SQLHistory(true));

				Collections.sort(history, new SQLHistorySorter());

				return history;
			}
		} catch (Exception e) {
			DbPlugin.getDefault().showErrorDialog(e);
		}
		return null;
	}

	public void save() {
		try {
			TimeWatcher tw = new TimeWatcher();
			tw.start();

			for (Iterator iter = history.iterator(); iter.hasNext();) {
				SQLHistory sql = (SQLHistory) iter.next();
				if (sql.isBlank()) {
					iter.remove();
				}

				if (!sql.isFileMode()) {
					saveContents(sql);
				}

			}
			super.saveXml(history);
			tw.stop();

		} catch (IOException e) {
			DbPlugin.log(e);
		}

	}


	public String loadContents(SQLHistory history) {
		String sql = null;
		String folderName = history.getFolderName();
		String fileName = history.getFileName();
		String filePath = file.getParent() + File.separator + folderName + File.separator + fileName + ".sql";
		File file = new File(filePath);
		if (file.exists()) {
			sql = FileUtil.getContents(new File(filePath));
		} else {
			sql = history.getSql();
		}
		return sql;
	}

	public void saveContents(SQLHistory history) {
		try {
			createFolder(history);
			createFile(history);
			history.setSql(getShortSql(history.getSql()));
			history.setFileMode(true);
			super.saveXml(history);

		} catch (IOException e) {
			DbPlugin.log(e);
		}

	}

	String getShortSql(String fullSql) {
		// String sql = SQLFormatter.unformat(fullSql);
		String sql = fullSql;
		if (sql == null)
			return "";
		if (sql.length() > History.MAX_LEN) {
			return sql.substring(0, History.MAX_LEN) + "...";
		} else {
			return sql;
		}
	}

	boolean createFolder(SQLHistory history) throws IOException {
		String folderName = history.getFolderName();

		String folderPath = file.getParent() + File.separator + folderName;
		File f = new File(folderPath);
		if (!f.exists()) {
			return f.mkdir();
		} else {
			return true;
		}
	}

	void createFile(SQLHistory history) throws IOException {
		String folderName = history.getFolderName();
		String fileName = history.getFileName();

		String filePath = file.getParent() + File.separator + folderName + File.separator + fileName + ".sql";
		File f = new File(filePath);
		InputStreamUtil.save(f, new StringReader(history.getSql()));
	}

	void removeFile(SQLHistory history) throws IOException {
		String folderName = history.getFolderName();
		String fileName = history.getFileName();

		String filePath = file.getParent() + File.separator + folderName + File.separator + fileName + ".sql";
		File f = new File(filePath);
		if (f.exists()) {
			f.delete();
		}


		if (f.getParentFile().list().length == 0) {
			f.getParentFile().delete();
		}
	}


	public List getHistory() {
		return this.history;
	}

	public int getHistoryCount() {
		return history.size();
	}

	public void clearHistory() {
		history = new ArrayList();
		currentPosition = 0;

	}

	public void removeOverHistory() throws IOException {
		try {
			this.maxSize = preferenceStore.getInt(PreferencePage.P_MAX_HISTORY);
			while (history.size() - 1 > maxSize) {
				SQLHistory sh = (SQLHistory) history.get(0);
				removeFile(sh); // add

				history.remove(0);
				currentPosition--;
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
		}

	}

	private boolean isSameHistory(String sql, int position) {
		if (position >= 0) {
			TimeWatcher tw = new TimeWatcher();
			tw.start();

			SQLHistory his = (SQLHistory) history.get(position);
			String targetSql = loadContents(his);

			// It comments for the performance issue.
			// String hSql = SQLFormatter.unformat(targetSql);
			// String uSql = SQLFormatter.unformat(sql);
			String hSql = targetSql;
			String uSql = sql;

			if (uSql.equals(hSql)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}

	}

	public boolean addHistory(SQLHistory his) throws IOException {
		TimeWatcher tw = new TimeWatcher();
		tw.start();
		boolean isAdd = false;
		String sql = his.getSql();
		SQLHistory current = currentHistory();

		if (!isSameHistory(sql, currentPosition)) {

			saveContents(his);
			history.add(his);
			currentPosition = history.size() - 2;
			isAdd = true;
		}

		Collections.sort(history, new SQLHistorySorter());

		removeOverHistory();

		tw.stop();
		return isAdd;
	}

	public void remove(SQLHistory sqlHistory) throws IOException {
		removeFile(sqlHistory);
		history.remove(sqlHistory);
		if (currentPosition > 0) {
			currentPosition--;
		}
	}
	public SQLHistory currentHistory() {
		return (history.size() == 0) ? null : (SQLHistory) history.get(currentPosition);

	}

	public SQLHistory prevHisotry() {
		return (currentPosition <= 0) ? null : (SQLHistory) history.get(--currentPosition);
	}

	public SQLHistory nextHisotry() {
		return (currentPosition == history.size() - 1) ? null : (SQLHistory) history.get(++currentPosition);
	}

	public boolean hasPrevHistory() {
		return (currentPosition >= 1);
	}

	public boolean hasNextHistory() {
		return (history.size() - 1 > currentPosition);
	}

	// for debug
	public int getCurrentPosition() {
		return currentPosition;
	}

	public void modifyCurrentPosition(SQLHistory target) {
		for (int i = history.size() - 1; i >= 0; i--) {
			SQLHistory sql = (SQLHistory) history.get(i);
			if (sql.equals(target)) {
				currentPosition = i;
				return;
			}
		}

	}

}
