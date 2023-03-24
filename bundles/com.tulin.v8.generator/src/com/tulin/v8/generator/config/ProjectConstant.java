package com.tulin.v8.generator.config;

/**
 * 项目变量
 */
public  class ProjectConstant {
   //生成代码所在的基础包名称，可根据自己公司的项目修改（
   //注意：这个配置修改之后需要手工修改src目录项目默认的包路径，使其保持一致，不然会找不到类）
    public static  String BASE_PACKAGE = "com.tlv8.project";
    public static  String GENERATOR_PACKAGE = "com.tulin.v8.generator";//配置文件夹目录

    public static  String MODEL_PACKAGE = BASE_PACKAGE + ".entity";//生成的Model所在包
    public static  String MAPPER_PACKAGE = BASE_PACKAGE + ".mapper";//生成的Mapper所在包
    public static  String SERVICE_PACKAGE = BASE_PACKAGE + ".service";//生成的Service所在包
    public static  String SERVICE_IMPL_PACKAGE = SERVICE_PACKAGE + ".impl";//生成的ServiceImpl所在包
    public static  String CONTROLLER_PACKAGE = BASE_PACKAGE + ".controller";//生成的Controller所在包

    public static  String MAPPER_INTERFACE_REFERENCE = GENERATOR_PACKAGE + ".core.Mapper";//Mapper插件基础接口的完全限定名
}
