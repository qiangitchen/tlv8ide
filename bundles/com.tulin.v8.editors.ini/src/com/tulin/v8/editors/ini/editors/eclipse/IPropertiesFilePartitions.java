package com.tulin.v8.editors.ini.editors.eclipse;

public abstract interface IPropertiesFilePartitions {
	public static final String PROPERTIES_FILE_PARTITIONING = "___pf_partitioning";
	public static final String COMMENT = "__pf_comment";
	public static final String PROPERTY_VALUE = "__pf_roperty_value";
	public static final String[] PARTITIONS = { "__pf_comment", "__pf_roperty_value" };
}