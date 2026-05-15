package com.example.demo1.service;

import com.example.demo1.model.dto.ChatRequestDTO;
import com.example.demo1.model.vo.ChatResponseVO;

public interface ChatService {
    ChatResponseVO chat(ChatRequestDTO requestDTO);
}