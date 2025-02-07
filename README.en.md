# SHIR-AI

<div align="center">
  <p>
  </p>

**English** | [**简体中文**](README.md)

</div>
AI Intelligent Conversation System, integrates common functions of the backend management system, internationalization, online code generation, AI conversation, AI development tools, etc.


AI Intelligent Conversation System, integrates common functions of the backend management system, internationalization, online code generation, AI conversation, AI development tools, etc.

AI Intelligent Conversation System, integrates common functions of the backend management system, internationalization, online code generation, AI conversation, AI development tools, etc.

The AI conversation features are as follows:

* OPENAI protocol conversation, supports streaming output with typewriter effect
* RGA conversation, supports common PDF, TXT, Word documents, etc.
* AI online function, supports sending web addresses in conversation, and the backend will automatically retrieve web content and submit it to AI
* Google search function, supports AI to automatically use Google search for the latest information
* Image conversation, supports sending images
* Integration with DingTalk, WeChat Work, and Feishu bot requests for AI conversation interface (just need to configure the related keys)

**Note: Using Google search and RGA conversation functions requires starting the [Python service](python/readme.md)**

Backend management functions are as follows:

* Dictionary configuration (including OPENAI API KEY and proxy address)
* System management
* Chat record management
* Online generation of Mapper, Service, Controller, Entity code functions

### Based on

* Spring boot 3.0
* Mybatis plus
* [lui-auth 2.x](https://github.com/reinershir/lui-auth)
* Mysql 5.8 +
* [Redis](https://redis.io/)
* Swagger 3
* i18n
* Jdk 17 +
* Python 3 (For Python code related instructions, please see: [readme](python/readme.md))
* Llama-index

### Usage

1. Download the source code
2. Execute shir-boot-mysql.sql to initialize the database
3. Download the [frontend source code](https://github.com/reinershir/Shir-Boot-Admin) and run it
4. Ensure that Redis is available
5. Run `ShirBootApplication.java` to start the backend service

### Web UI

For the Web operation page code, please visit: [shir-boot-admin](https://github.com/reinershir/Shir-Boot-Admin)
