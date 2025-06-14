package com.lp.demo.langchain4j.example2.controller;

import com.lp.demo.langchain4j.example2.json.Person;
import com.lp.demo.langchain4j.example2.json.PersonService;
import com.lp.demo.langchain4j.example2.service.Assistant;
import com.lp.demo.langchain4j.example2.util.JsonUtil;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.community.model.dashscope.QwenChatRequestParameters;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.ClassPathDocumentLoader;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.splitter.DocumentByLineSplitter;
import dev.langchain4j.data.document.splitter.DocumentByWordSplitter;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.chat.request.ResponseFormatType;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import dev.langchain4j.model.chat.request.json.JsonSchema;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 聊天模型
 */
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class LangChain4jExampleController {

    final ChatModel chatModel;

    final Assistant assistant;

    /**
     * --------------------------------------1.聊天--------------------------------------
     */

    /**
     * 聊天模型：使用低级接口实现
     * 低级接口：调用底层接口，灵活度高，支持复杂自定义，需要编写更多代码
     */
    @GetMapping("/low/chat")
    public String lowChat(@RequestParam(value = "message") String message) {
//        return chatModel.chat(message);
        return chatModel.chat(SystemMessage.systemMessage("请以特朗普的语气来对话"),
                UserMessage.userMessage(message))
                .aiMessage()
                .text();
    }

    /**
     * 聊天模型：使用高级接口实现
     * 高级接口：调用框架封装好的接口，使用简单，用于简单直接的功能实现，代码编写量少
     */
    @GetMapping("/high/chat")
    public String highChat(@RequestParam(value = "message") String message) {
        return assistant.chat(message);
    }

    /**
     * --------------------------------------2.聊天记忆--------------------------------------
     */

    private final ChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(10);

    private final Map<String, ChatMemory> chatMemoryMap = new ConcurrentHashMap<>();

    @GetMapping("/memory/low/chat")
    public String memoryLowChat(@RequestParam(value = "message") String message) {
        chatMemory.add(UserMessage.userMessage(message));
        ChatResponse response = chatModel.chat(chatMemory.messages());
        chatMemory.add(response.aiMessage());
        return response.aiMessage().text();
    }

    @GetMapping("/memory/high/chat")
    public String memoryHighChat(@RequestParam(value = "memoryId") String memoryId, @RequestParam(value = "message") String message) {
        return assistant.chat(memoryId, message);
    }

    @GetMapping("/memory/high/chat2")
    public String memoryHighChat2(@RequestParam(value = "memoryId") String memoryId, @RequestParam(value = "message") String message) {
        if (!chatMemoryMap.containsKey(memoryId)) {
            MessageWindowChatMemory messageWindowChatMemory = MessageWindowChatMemory.withMaxMessages(10);
            messageWindowChatMemory.add(UserMessage.userMessage(message));
            chatMemoryMap.put(memoryId, messageWindowChatMemory);
        }
        List<ChatMessage> messages = chatMemoryMap.get(memoryId).messages();
        ChatResponse chat = chatModel.chat(messages);
        chatMemoryMap.get(memoryId).add(chat.aiMessage());
        return chat.aiMessage().text();

    }


    /**
     * --------------------------------------3.返回json格式--------------------------------------
     */

    @GetMapping("/json/high/chat")
    public String jsonHighChat(@RequestParam(value = "message") String message) {
        PersonService personService = AiServices.create(PersonService.class, chatModel);
        Person person = personService.extractPerson(message);
        return person.toString();
    }

    @GetMapping("/json/low/chat")
    public String jsonLowChat(@RequestParam(value = "message") String message) {
        ResponseFormat responseFormat = ResponseFormat.builder()
                .type(ResponseFormatType.JSON)
                .jsonSchema(JsonSchema.builder()
                        .rootElement(JsonObjectSchema.builder()
                                .addIntegerProperty("age", "the person's age")
                                .addIntegerProperty("weight", "the person's weight")
                                .required("age", "weight")
                                .build())
                        .build())
                .build();

        ChatResponse chat = chatModel.chat(ChatRequest.builder()
                .messages(List.of(UserMessage.from(message)))
                .parameters(ChatRequestParameters.builder()
                        .responseFormat(responseFormat)
                        .build())
                .build());
        return chat.aiMessage().text();
    }

    /**
     * --------------------------------------4.Function Calling：函数调用--------------------------------------
     */

    /**
     * 前提：添加工具配置
     * 1、AssistantConfig：配置工具类.tools(new HighLevelCalculator())
     * 2、HighLevelCalculator：工具方法上添加注解@Tool("计算两数之和")
     * 3、选择一个支持function calling的模型，如qwen，deepseek当前不支持
     *
     * @param message
     * @return
     */
    @GetMapping("/functionCalling/high/chat")
    public String functionCallingHighChat(@RequestParam(value = "message") String message) {
        return assistant.chat(message);
    }

    @GetMapping("/functionCalling/low/chat")
    public String functionCallingLowChat(@RequestParam(value = "message") String message) {
        ToolSpecification specifications = ToolSpecification.builder()
                .name("Calculator")
                .description("输入两个数，对这两个数求和")
                .parameters(JsonObjectSchema.builder()
                        .addIntegerProperty("a","第一个参数")
                        .addIntegerProperty("b","第二个参数")
                        .required("a","b")
                        .build())
                .build();

        ChatResponse chatResponse = chatModel.doChat(ChatRequest.builder()
                .messages(List.of(UserMessage.from(message)))
                .parameters(ChatRequestParameters.builder()
                        .toolSpecifications(specifications)
                        .build())
                .build());

        chatResponse.aiMessage().toolExecutionRequests().forEach(toolExecutionRequest -> {

            System.out.println(toolExecutionRequest.name());
            System.out.println(toolExecutionRequest.arguments());

            try {
                Class<?> aClass = Class.forName("com.lp.demo.langchain4j.example2.functioncalling." + toolExecutionRequest.name());
                Runnable runnable = (Runnable) JsonUtil.toJsonObject(toolExecutionRequest.arguments(), aClass);
                runnable.run();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }


        });

        return chatResponse.aiMessage().text();
    }


    /**
     * --------------------------------------5.image：图片识别--------------------------------------
     */

//    @Value("classpath:/static/documents/cat.png")
//    private Resource systemResource;

    /**
     * 前提：
     * 1、支持图片识别的模型，如：qwen-vl系列:qwen-vl-plus。qwen2.5、deepseek-r1只支持文本Text，
     * 2、DashScope要求图片必须为公网可访问的url，不能直接传base64
     *
     * tokens：调用两次耗费2700，第二次可能还命中缓存
     *
     * @param message
     * @return
     * @throws IOException
     */
    @GetMapping("/image/high/chat")
    public String imageHighChat(@RequestParam(value = "message") String message) throws IOException {
//        String name = "static/documents/cat.png";
//        InputStream inputStream = ClassLoader.getSystemResourceAsStream(name);
//        if (inputStream == null) {
//            throw new IOException("not found resource: " + name);
//        }

        String imagePath = "https://pics5.baidu.com/feed/8435e5dde71190efbd218f41398f3d1afffa60f3.jpeg@f_auto?token=c1a7a74ca9b6b4b54f1e76b479d634a4";
        UserMessage userMessage = UserMessage.from(TextContent.from(message),
                ImageContent.from(imagePath)
//                ImageContent.from(Base64.getEncoder().encodeToString(inputStream.readAllBytes()), "image/png")
        );
        return chatModel.chat(List.of(userMessage)).aiMessage().text();
    }

    /**
     * --------------------------------------5.RAG：多模态，分词，向量存储、检索--------------------------------------
     */

    @GetMapping("/rag/high/chat")
    public String ragLowChat(@RequestParam(value = "message") String message) {
        return assistant.chat(message);
    }

    final EmbeddingStore<TextSegment> embeddingStore;

    final EmbeddingModel embeddingModel;

    @GetMapping("/rag/load")
    public String ragLoad(){
        String path = "static/documents"; // "D:\\Projects\\opensource\\demo\\demo-langchain4j\\demo-langchain4j-example2\\src\\main\\resources\\static\\documents"
//        List<Document> documents = FileSystemDocumentLoader.loadDocuments(path);
        List<Document> documents = ClassPathDocumentLoader.loadDocuments(path);
        // EmbeddingStoreIngestor.ingest(documents, embeddingStore);
        EmbeddingStoreIngestor.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(embeddingModel)
                // 分词，文档按行拆分，每30个字符拆分一次，最大重复20个字符
                .documentSplitter(new DocumentByLineSplitter(30,20))
                .build()
                .ingest(documents);
        return "success";
    }

}
