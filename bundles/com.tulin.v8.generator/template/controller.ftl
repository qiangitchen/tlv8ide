package ${basePackage}.controller;

import ${generatorPackage}.core.Result;
import ${generatorPackage}.core.ResultGenerator;
import ${basePackage}.entity.${modelNameUpperCamel};
import ${basePackage}.service.${modelNameUpperCamel}Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.validation.constraints.NotNull;
import javax.annotation.Resource;
import java.util.List;

/**
* Created by ${author} on ${date}.
*/
@RestController
public class ${modelNameUpperCamel}Controller {
    @Resource
    private ${modelNameUpperCamel}Service ${modelNameLowerCamel}Service;

    @ApiOperation(value = "列表查询", notes = "${modelNameUpperCamel}列表")
    @ApiImplicitParams({
          @ApiImplicitParam(name="pageSize",value="每页数据量",paramType="query",dataType="String"),
          @ApiImplicitParam(name="pageNo",value="页码",paramType="query",dataType="String")
       })
    @GetMapping(value = "/v1${baseRequestMapping}/list")
    public Result list(
        @RequestParam(value = "pageNo",defaultValue = "0") Integer pageNo,
        @RequestParam(value = "pageSize",defaultValue = "0") Integer pageSize) {
        Condition condition = new Condition(${modelNameUpperCamel}.class,false,false);
        Example.Criteria criteria = condition.createCriteria();
        criteria.orEqualTo("", "");
        PageHelper.startPage(pageNo, pageSize);
        List<${modelNameUpperCamel}> list = ${modelNameLowerCamel}Service.findByCondition(condition);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @ApiOperation( value = "根据ID查找实例",notes = "通过id查找详情" )
    @ApiImplicitParam( name="id" , value="id" , paramType = "path",dataType = "String",required = true)
    @GetMapping(value = "/v1${baseRequestMapping}/{id}")
    public Result detail(
       @PathVariable @NotNull(message = "id不能为空") String id) {
       ${modelNameUpperCamel} ${modelNameLowerCamel} = ${modelNameLowerCamel}Service.findById(id);
       return ResultGenerator.genSuccessResult(${modelNameLowerCamel});
    }

    @ApiOperation( value = "添加一个实例",notes = "添加${modelNameLowerCamel}" )
    @PostMapping(value = "/v1${baseRequestMapping}/save")
    public Result save(@RequestBody ${modelNameUpperCamel} ${modelNameLowerCamel}) {
        ${modelNameLowerCamel}Service.save(${modelNameLowerCamel});
        return ResultGenerator.genSuccessResult();
    }

    @ApiOperation( value = "根据ID删除",notes = "通过id删除${modelNameLowerCamel}" )
    @ApiImplicitParam( name="id" , value="id" , paramType = "path",dataType = "String",required = true)
    @DeleteMapping("/v1${baseRequestMapping}/{id}")
    public Result delete(@PathVariable(value = "id") @NotNull(message = "id不能为空") String id) {
        ${modelNameLowerCamel}Service.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @ApiOperation( value = "修改",notes = "修改${modelNameLowerCamel}" )
    @PutMapping("/v1${baseRequestMapping}/update")
    public Result update(@RequestBody ${modelNameUpperCamel} ${modelNameLowerCamel}) {
        ${modelNameLowerCamel}Service.update(${modelNameLowerCamel});
        return ResultGenerator.genSuccessResult();
    }


}
