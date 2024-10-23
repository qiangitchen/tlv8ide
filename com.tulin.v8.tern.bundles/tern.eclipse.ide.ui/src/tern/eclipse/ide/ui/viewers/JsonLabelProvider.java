package tern.eclipse.ide.ui.viewers;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.eclipsesource.json.JsonValue;

import tern.server.protocol.JsonHelper;

public class JsonLabelProvider extends LabelProvider implements
		ITableLabelProvider {

	private static final JsonLabelProvider INSTANCE = new JsonLabelProvider();

	public static JsonLabelProvider getInstance() {
		return INSTANCE;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		if (element instanceof MemberWrapper) {
			MemberWrapper member = ((MemberWrapper) element);
			switch (columnIndex) {
			case 0:
				return member.getName();
			case 1:
				return JsonHelper.getString(member.getValue());
			}
		} else if (element instanceof JsonValue) {
			return JsonHelper.getString((JsonValue)element);
		}
		return null;
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

}
