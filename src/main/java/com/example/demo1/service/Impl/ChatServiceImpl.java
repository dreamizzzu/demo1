package com.example.demo1.service.Impl;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import com.example.demo1.model.dto.ChatRequestDTO;
import com.example.demo1.model.vo.ChatResponseVO;
import com.example.demo1.service.ChatService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class ChatServiceImpl implements ChatService {

    private final ChatClient chatClient;
    private final StringRedisTemplate stringRedisTemplate;

    public ChatServiceImpl(ChatClient.Builder chatClientBuilder,
                           StringRedisTemplate stringRedisTemplate) {
        this.chatClient = chatClientBuilder
                .defaultSystem("你是一名专业、友好、简洁的中文智能助手，请结合历史对话回答用户问题")
                .defaultOptions(
                        DashScopeChatOptions.builder()
                                .withTopP(0.7)
                                .build()
                )
                .build();
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public ChatResponseVO chat(ChatRequestDTO requestDTO) {
        // ========== 附加题：sessionId 为空校验 ==========
        if (requestDTO == null || !StringUtils.hasText(requestDTO.getSessionId()) || !StringUtils.hasText(requestDTO.getMessage())) {
            throw new IllegalArgumentException("会话ID和用户消息不能为空");
        }

        String sessionId = requestDTO.getSessionId();
        String message = requestDTO.getMessage();
        String redisKey = "chat:session:" + sessionId;

        // 1. 读取历史消息
        List<String> records = stringRedisTemplate.opsForList().range(redisKey, 0, -1);
        String historyText = "";
        if (records != null && !records.isEmpty()) {
            historyText = String.join("\n", records);
        }

        // 2. 拼接上下文
        String finalPrompt = """
                以下是历史对话：
                %s
                
                当前用户问题：
                %s
                """.formatted(historyText, message);

        // ========== 附加题：异常处理 ==========
        String answer;
        try {
            // 3. 调用模型
            answer = chatClient.prompt(finalPrompt)
                    .call()
                    .content();
        } catch (Exception e) {
            throw new RuntimeException("调用大模型失败：" + e.getMessage(), e);
        }

        // 4. 保存本轮记录
        String recordText = "用户：" + message + "\n助手：" + answer;
        stringRedisTemplate.opsForList().rightPush(redisKey, recordText);

        // ========== 附加题：历史轮数控制策略 ==========
        // 只保留最近 3 轮对话
        Long size = stringRedisTemplate.opsForList().size(redisKey);
        if (size != null && size > 3) {
            stringRedisTemplate.opsForList().trim(redisKey, size - 3, size - 1);
        }

        return new ChatResponseVO(message, answer);
    }
}