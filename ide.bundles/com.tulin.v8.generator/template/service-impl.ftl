package ${basePackage}.service.impl;

import ${basePackage}.mapper.${modelNameUpperCamel}Mapper;
import ${modelPackage}.${modelNameUpperCamel};
import ${basePackage}.service.${modelNameUpperCamel}Service;

import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
/**
 * Created by ${author} on ${date}.
 */
@Service
public class ${modelNameUpperCamel}ServiceImpl implements ${modelNameUpperCamel}Service {
    @Autowired
    private ${modelNameUpperCamel}Mapper ${modelNameLowerCamel}Mapper;
    
    public int deleteByPrimaryKey(String id){
    	return ${modelNameLowerCamel}Mapper.deleteByPrimaryKey(id);
    }
	
	public int insert(${modelNameUpperCamel} row){
		return ${modelNameLowerCamel}Mapper.insert(row);
	}
	
	public ${modelNameUpperCamel} selectByPrimaryKey(String id){
		return ${modelNameLowerCamel}Mapper.selectByPrimaryKey(id);
	}
	
	public List<${modelNameUpperCamel}> selectAll(){
		return ${modelNameLowerCamel}Mapper.selectAll();
	}
	
	public int updateByPrimaryKey(${modelNameUpperCamel} row){
		return ${modelNameLowerCamel}Mapper.updateByPrimaryKey(row);
	}

}
