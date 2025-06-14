
> [https://docs.langchain4j.dev/tutorials/spring-boot-integration/](https://docs.langchain4j.dev/tutorials/spring-boot-integration/)  
> [https://github.com/langchain4j/langchain4j](https://github.com/langchain4j/langchain4j)


# langchain4j支持

## jdk

jdk17+ from version 0.36.0

[Upgrade to JDK 17](https://github.com/langchain4j/langchain4j/pull/1913)

## springboot

对于Spring Boot的集成需要Java 17 和 Spring Boot 3.2.x以上，从版本0.32.0开始

> [https://docs.langchain4j.dev/tutorials/spring-boot-integration#supported-versions](https://docs.langchain4j.dev/tutorials/spring-boot-integration#supported-versions)  
> [New Baseline: Spring Boot 3 and Java 17](https://github.com/langchain4j/langchain4j-spring/pull/24)  

## 语言模型

Provider供应商 | 流式 | 工具（同步/流式） | JSON结构（Schema） | JSON模式（Mode） | 支持的模式（输入） | 可观察性 | 可定制HTTP客户端 | 本地部署 | 镜像支持 | 备注
---|---|---|---|---|---|---|---|---|---|---
[OpenAI](https://docs.langchain4j.dev/integrations/language-models/open-ai/) | ✅ | ✅/✅| ✅ | ✅ | 文本, 图片, 音频 | ✅ | ✅ | 兼容: Ollama, LM Studio, GPT4All, 等. | ✅ | 兼容: Groq, 等.
[Ollama](https://docs.langchain4j.dev/integrations/language-models/ollama/) | ✅ | ✅/✅| ✅ | ✅ | 文本, 图片 | ✅ | ✅ | ✅ |  |
[DashScope(Qwen) (阿里百炼)](https://docs.langchain4j.dev/integrations/language-models/dashscope/) | ✅ | ✅/✅|  |  | 文本, 图片, 音频 | ✅ |  |  |  | 
[Qianfan(百度千帆)](https://docs.langchain4j.dev/integrations/language-models/qianfan/) | ✅ | ✅/✅|  |  | 文本 |  |  |  |  | 
[Zhipu AI(智谱AI)](https://docs.langchain4j.dev/integrations/language-models/zhipu-ai/) | ✅ | ✅/✅|  |  | 文本, 图片 | ✅ |  |  |  | 
... |  |  |  |  |  |  |  |  |  | 


> [https://docs.langchain4j.dev/integrations/language-models/](https://docs.langchain4j.dev/integrations/language-models/)


# langchain4j版本特性

## 1.0.0 + 1.0.0-beta5

发布时间：2025.05.14



## 1.0.0-rc1 + 1.0.0-beta4

发布时间：2025.05.02

- 5月中旬发布最终1.0.0版本
- 6月发布下一组候选1.0.0-rc1模块

### 1.0.0-rc1 (release candidate 1)
候选release版本：
- langchain4j
- langchain4j-core
- langchain4j-open-ai
- langchain4j-http-client
- langchain4j-http-client-jdk

### 1.0.0-beta4

- langchain4j-ollama
- langchain4j-community-dashscope
- ...

### 内容变更

- 支持GPT4.1




# LangChain4j对比SpringAI

- langchain4j功能更强大
- langchain4j学习成本较高
- langchain4j文档不够全面

- 简单功能、短时开发，使用spring ai
- 功能复杂、定制化要求多、灵活度更高，使用langchain4j

功能 | LangChain4j | SpringAI 
---|---|---
 |  | 

