package ${basePackage}.controller;

import ${modelPackage}.${modelNameUpperCamel};
import ${basePackage}.service.${modelNameUpperCamel}Service;

import com.tlv8.common.domain.AjaxResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
* Created by ${author} on ${date}.
*/
@Controller
@RequestMapping("/${modelNameLowerCamel}")
public class ${modelNameUpperCamel}Controller {
    @Autowired
    private ${modelNameUpperCamel}Service ${modelNameLowerCamel}Service;

	@ResponseBody
    @RequestMapping("/deleteByPrimaryKey")
	public Object deleteByPrimaryKey(String id){
		return AjaxResult.success(${modelNameLowerCamel}Service.deleteByPrimaryKey(id));
	}
	
	@ResponseBody
    @RequestMapping("/insert")
	public Object insert(${modelNameUpperCamel} row){
		return AjaxResult.success(${modelNameLowerCamel}Service.insert(row));
	}
	
	@ResponseBody
    @RequestMapping("/selectByPrimaryKey")
	public Object selectByPrimaryKey(String id){
		return AjaxResult.success(${modelNameLowerCamel}Service.selectByPrimaryKey(id));
	}
	
	@ResponseBody
    @RequestMapping("/selectAll")
	public Object selectAll(){
		return AjaxResult.success(${modelNameLowerCamel}Service.selectAll());
	}
	
	@ResponseBody
    @RequestMapping("/updateByPrimaryKey")
	public Object updateByPrimaryKey(${modelNameUpperCamel} row){
		return AjaxResult.success(${modelNameLowerCamel}Service.updateByPrimaryKey(row));
	}

}
