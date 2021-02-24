# o2o校园商铺项目（springboot版本）

> 开发背景：笔者曾经在大学期间从事过一个叫做doer的项目校园商铺项目，当时大学处在比较荒芜的高校园，周边商铺较少，只有零零散散的街边餐车，加上学校不准随意进出，所以学生购买小吃十分不便利，加上刚入学大学生对周边环境比较陌生，所以参与了doer项目的基础开发工作，此在doer项目基础上进行开发。

项目已经部署到腾讯云，访问地址http://lookstarry.com/o2o/index

#### 技术栈

* Spring/SpringBoot
* MyBatis
* Redis
* Docker
* MySQL
* JQuery + AJAX + HTML + SUI-MOBILE



**项目前端**

在阿里开发的手机H5应用组件[SUI MOBILE](https://sui.thinkadmin.top/)上进行开发，利用JQuery + AJAX向后端异步请求数据，进行前端展示




**项目后端**

使用SSM框架进行开发，后期迭代迁移为SpringBoot版本

数据库使用Docker配置MySQL集群进行读写分离，使用MyBatis作为持久化框架

使用Redis进行缓存读多写少的数据。

商品、积分奖品兑换二维码使用谷歌的zXing来动态生成

扫码者信息通过调用[微信测试号](https://mp.weixin.qq.com/debug/cgi-bin/sandbox?t=sandbox/login)回传链接获取OpenId来获取扫码者信息方便进行店家身份校验核对

由于微信回传链接过长，容易造成被微信拦截，使用[缩我短链](https://suowo.cn/)API来请求实时生成短链接放入二维码中


