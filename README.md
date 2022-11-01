<p align="center">
<a href="https://www.tlv8.com" rel="nofollow">
   <img src="https://images.gitee.com/uploads/images/2021/1019/103335_e14063b4_1210964.png" alt="tlv8" width="360">
</a>
</p>

<p align="center">  
  <a href="#"><img src='https://img.shields.io/badge/jdk-1.8+-redviolet.svg'/></a>
  <a href='LICENSE'><img src='https://img.shields.io/badge/License-EPL2.0-blue.svg'></img></a>
  <a href='../../releases'><img src='https://img.shields.io/badge/gitee--release-2.4-blueviolet.svg'></a>
</p>

#### 介绍
㭸林开发平台（tlv8 ide）是一款基于eclipse开发的快速开发工具, 目的是为开发人员提供一个可以快速实现业务需求并且可以将开发、测试和维护为一体的工具，

集成了tomcat插件，数据库插件，报表设计（可视化），流程设计（可视化），网页设计（可视化）等多功能于一体的JAVA EE项目开发工具。

能够实现业务系统快速开发，报表系统快速开发（只需要会SQL），工作流快速定制。

产品网站
[www.tlv8.com](https://www.tlv8.com)
帮助文档
[doc.tlv8.com](https://doc.tlv8.com)

#### 名记
 **㭸（tú）一种树，与“梌”同，㭸树：坚硬无比，寓意：坚硬、坚强  
项目是在本人生病住院时发起的，当时想着取个什么名字，思考很久~ 无意中看到这个子感觉寓意很好就想着用这个子命名项目，可惜这个字很多输入法打不出来~ 最后只能用拼音表示，拼音tl>就是㭸树林的意思~.~ v8则是因为之前我做的产品版本刚好是7，所以就从v8开始命名， 真实版本还是从1.0.0开始的， v8就是纯粹的名字~** 




#### 软件架构
Eclipse插件体系结构


#### 安装教程

1.  下载[eclipse](https://www.eclipse.org/downloads/packages/)4.8+
2.  离线安装：

（1）v2.2.0及以前的版本：下载发行版压缩包studio-app.zip [https://gitee.com/qianpou/tlv8ide/releases](https://gitee.com/qianpou/tlv8ide/releases),将解压后的studio-app文件夹放到eclipse/dropins目录下；

（2）v2.2.1及以后的版本：下载发行版分压包，解压后得到版本文件如【com.tulin.v8.ide.update-2.2.1-SNAPSHOT.zip】eclipse-Help-Install New Software-Add,Location选择归档文件，根据提示完成安装。

在线安装：eclipse-Help-Install New Software-Add,Location填写：[https://dl.tlv8.com/updates/](https://dl.tlv8.com/updates/),按提示步骤完成安装。

marketplace：eclipse|myeclipse-Help-Eclipse Marketplace-Search输入tlv8,点击安装即可。地址：[https://marketplace.eclipse.org/content/tlv8/](https://marketplace.eclipse.org/content/tlv8/)

[![Drag to your running Eclipse* workspace. *Requires Eclipse Marketplace Client](https://marketplace.eclipse.org/sites/all/themes/solstice/public/images/marketplace/btn-install.svg)](http://marketplace.eclipse.org/marketplace-client-intro?mpc_install=5466625 "Drag to your running Eclipse* workspace. *Requires Eclipse Marketplace Client")

3.  启动eclipse
4.  [下载项目源码](https://gitee.com/qianpou/tl)
5.  运行Tomcat（也可以使用其他中间件）查看效果

[详细配置教程](https://blog.csdn.net/qianpou/article/details/120593335?spm=1001.2014.3001.5502)

#### 使用说明

1.  jdk版本要求对应eclipse对jdk版本的要求
2.  插件项目的jdk版本要求1.8+
3.  报表设计器不支持IE环境，所以在Windows环境下需要CEF.
4.  Win64系统安装[CEF](https://gitee.com/qianpou/chromium-swt)，v2.2.1+不需要单独安装。


#### 展示
一键启动Tomcat：
![输入图片说明](https://images.gitee.com/uploads/images/2021/0918/105611_a540e114_1210964.jpeg "tomcat.jpg")

一站式数据库管理：
![输入图片说明](https://images.gitee.com/uploads/images/2021/0918/105734_2a26f3db_1210964.png "数据库.png")

可视化报表（图表）设计：
![输入图片说明](https://images.gitee.com/uploads/images/2021/0918/105900_66e21efa_1210964.png "报表.png")
![输入图片说明](https://images.gitee.com/uploads/images/2021/0918/111012_b2ec2658_1210964.png "图表.png")

可视化流程设计：
![输入图片说明](https://images.gitee.com/uploads/images/2021/0918/110506_33fea444_1210964.png "流程设计.png")

页面设计（所见及所得）：
![输入图片说明](https://images.gitee.com/uploads/images/2021/0918/110719_6dbdae41_1210964.png "界面设计.png")


#### 特别感谢


- [昆明三体科技有限公司](https://e.gitee.com/kmsanti/)
- [软捷科技](https://www.yunagile.com/)
- [起步科技](https://www.justep.com/)
- [Eclipse](https://www.eclipse.org/)
- [zigen](http://www.ne.jp/asahi/zigen/home/plugin/dbviewer/about_en.html)
- [Sysdeo Tomcat Launcher Plugin](http://www.eclipsetotale.com/tomcatPlugin.html)
- [ureport](https://gitee.com/youseries/ureport)




