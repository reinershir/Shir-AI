# SHIR-AI

<div align="center">
  <p>
  </p>

[**English**](README.en.md) | **简体中文** |

</div>

AI智能对话系统，整合了后台管理系统常用功能、国际化、在线代码生成、AI对话、AI开发工具等。

AI对话方面有如下功能：

* OPENAI协议对话，支持流式输出打字机效果
* RGA 对话，支持常见PDF、TXT、Word文档等
* AI联网功能，支持对话中发送网页地址，后台会自动获取网页内容提交给AI
* 谷歌搜索功能，支持AI自动使用谷歌搜索最新信息
* 图片对话，支持发送图片
* 对接钉钉、企业微信、飞书的机器人请求AI对话接口（只需配置相关密钥即可）

**注意：使用谷歌搜索和RGA对话功能需要启动[Python服务](python/readme.md)**

后台管理功能如下：

* 字典配置（包含OPENAI API KEY和代理地址）
* 系统管理
* 聊天记录管理
* 在线生成Mapper、Service、Controller、Entity 代码功能

### 基于

* Spring boot 3.0
* Mybatis plus
* [lui-auth 2.x](https://github.com/reinershir/lui-auth)
* Mysql 5.8 +
* [Redis](https://redis.io/)
* Swagger 3
* i18n
* Jdk 17 +
* Python 3 (Python代码相关说明请看：[readme](python/readme.md))
* Llama-index

### 使用

1. 安装依赖组件：JDK17、mysql5.8、Redis
2. 下载源码：`git clone https://github.com/reinershir/Shir-AI`
3. 执行shir-boot-mysql.sql初始化数据库
4. 下载[前端源码](https://github.com/reinershir/Shir-Boot-Admin)并运行
5. 运行 `ShirBootApplication.java` 启动后台服务

### Web UI

Web操作页面的代码请访问： [shir-boot-admin](https://github.com/reinershir/Shir-Boot-Admin)
