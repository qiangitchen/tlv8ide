package com.tulin.v8.ide.widgets;

import org.dom4j.Element;

public class JSLibraryEntity
{
  private boolean jdField_a_of_type_Boolean;
  private String jdField_a_of_type_JavaLangString;
  private String b;
  private String c;
  private String d;
  private String e;
  private Element jdField_a_of_type_OrgDom4jElement;

  public Element getOwenElement()
  {
    return this.jdField_a_of_type_OrgDom4jElement;
  }

  public void setOwenElement(Element paramElement)
  {
    this.jdField_a_of_type_OrgDom4jElement = paramElement;
  }

  public boolean isDoc()
  {
    return this.jdField_a_of_type_Boolean;
  }

  public void setDoc(boolean paramBoolean)
  {
    this.jdField_a_of_type_Boolean = paramBoolean;
  }

  public String getSourcePath()
  {
    return this.e;
  }

  public void setSourcePath(String paramString)
  {
    this.e = paramString;
  }

  public String getLocalPath()
  {
    return this.d;
  }

  public void setLocalPath(String paramString)
  {
    this.d = paramString;
  }

  public int hashCode()
  {
    int i = 1;
    i = 31 * i + (this.e == null ? 0 : this.e.hashCode());
    return i;
  }

  public boolean equals(Object paramObject)
  {
    if (this == paramObject)
      return true;
    if (paramObject == null)
      return false;
    if (getClass() != paramObject.getClass())
      return false;
    JSLibraryEntity localJSLibraryEntity = (JSLibraryEntity)paramObject;
    if (this.e == null)
    {
      if (localJSLibraryEntity.e != null)
        return false;
    }
    else if (!this.e.equals(localJSLibraryEntity.e))
      return false;
    return true;
  }

  public String getName()
  {
    return this.jdField_a_of_type_JavaLangString;
  }

  public void setName(String paramString)
  {
    this.jdField_a_of_type_JavaLangString = paramString;
  }

  public String getPath()
  {
    return this.b;
  }

  public void setPath(String paramString)
  {
    this.b = paramString;
  }

  public String getParentPath()
  {
    return this.c;
  }

  public void setParentPath(String paramString)
  {
    this.c = paramString;
  }
}
