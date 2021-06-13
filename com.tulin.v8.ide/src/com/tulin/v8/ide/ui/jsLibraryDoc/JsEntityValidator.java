package com.tulin.v8.ide.ui.jsLibraryDoc;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class JsEntityValidator {
	private boolean jdField_a_of_type_Boolean = true;
	public static List<String> KEY_WORDS = new ArrayList();
	private static String jdField_a_of_type_JavaLangString = "break,false,in,this,void,continue,for,new,true,while,delete,function,null,typeof,with,else,if,return,var,case,debugger,export,super,catch,default,extends,switch,class,do,finally,throw,const,enum,import,try";
	public static String PATTERN_JSVAR = "^[a-zA-Z_$]+[a-zA-Z_$0-9]*$";
	public static String PATTERN_JSFUNCTION = "^[a-zA-Z_$]+[a-zA-Z_$0-9.]*[a-zA-Z_$0-9]+$";

	public boolean isValidated() {
		return this.jdField_a_of_type_Boolean;
	}

	public void setValidated(boolean paramBoolean) {
		this.jdField_a_of_type_Boolean = paramBoolean;
	}

	public void validateName() {
	}

	public void validateType() {
	}

	static {
		String[] arrayOfString = jdField_a_of_type_JavaLangString.split(",");
		int i = 0;
		int j = arrayOfString.length;
		while (i < j) {
			KEY_WORDS.add(arrayOfString[i]);
			i++;
		}
	}
}
