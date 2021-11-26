<p align="center">
<a href="https://www.tlv8.com" rel="nofollow">
   <img src="https://images.gitee.com/uploads/images/2021/1019/103335_e14063b4_1210964.png" alt="tlv8" width="360">
</a>
</p>

<p align="center">  
  <a href="#"><img src='https://img.shields.io/badge/jdk-1.8+-redviolet.svg'/></a>
  <a href='https://gitee.com/qianpou/tlv8ide/blob/master/LICENSE'><img src='https://img.shields.io/badge/License-EPL2.0-blue.svg'></img></a>
  <a href='https://gitee.com/qianpou/tlv8ide/releases'><img src='https://img.shields.io/badge/gitee--release-2.2-blueviolet.svg'></a>
</p>

#### 介绍
这是一款基于eclipse开发的快速开发工具

集成了tomcat插件，数据库插件，报表设计（可视化），流程设计（可视化），网页设计（可视化）等多功能于一体的JAVA EE项目开发工具。

能够实现业务系统快速开发，报表系统快速开发（只需要会SQL），工作流快速定制。

产品网站
[www.tlv8.com](https://www.tlv8.com)
帮助文档
[doc.tlv8.com](https://doc.tlv8.com)


#### 软件架构
Eclipse插件体系结构


#### 安装教程

1.  下载[eclipse](https://www.eclipse.org/downloads/packages/)4.8+
2.  离线安装：下载最新发行版压缩包studio-app.zip [https://gitee.com/qianpou/tlv8ide/releases](https://gitee.com/qianpou/tlv8ide/releases),将解压后的studio-app文件夹放到eclipse/dropins目录下；

    在线安装：eclipse-Help-Install New Software-Add,Location填写：[http://dl.tlv8.cn/updates/](http://dl.tlv8.cn/updates/),按提示步骤完成安装。

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
4.  Win64系统安装[CEF](https://github.com/equoplatform/chromium-swt)

    https://dl.equoplatform.com/chromium-swt-ce/69.0.0/repository 

    https://dl.equoplatform.com/chromium-cef-ce/69.0.0/repository


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

1.  [昆明三体科技有限公司](https://e.gitee.com/kmsanti/)
2.  [软捷科技](https://www.yunagile.com/)
3.  [起步科技](https://www.justep.com/)
4.  [Eclipse](https://www.eclipse.org/)
5.  [zigen](http://www.ne.jp/asahi/zigen/home/plugin/dbviewer/about_en.html)
6.  [Sysdeo Tomcat Launcher Plugin](http://www.eclipsetotale.com/tomcatPlugin.html)
7.  [ureport](https://gitee.com/youseries/ureport)


