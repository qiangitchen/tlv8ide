package ${basePackage}.service;

import ${basePackage}.entity.${modelNameUpperCamel};

import java.util.List;

/**
 * Created by ${author} on ${date}.
 */
public interface ${modelNameUpperCamel}Service {

	int deleteByPrimaryKey(String id);
	
	int insert(${modelNameUpperCamel} row);
	
	${modelNameUpperCamel} selectByPrimaryKey(String id);
	
	List<${modelNameUpperCamel}> selectAll();
	
	int updateByPrimaryKey(${modelNameUpperCamel} row);
}
