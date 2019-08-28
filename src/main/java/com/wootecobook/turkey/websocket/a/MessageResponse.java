package com.wootecobook.turkey.websocket.a;

import com.wootecobook.turkey.user.service.dto.UserResponse;
import lombok.Getter;

@Getter
public class MessageResponse {
    private String content;
    private UserResponse sender;

    public MessageResponse(String content, UserResponse sender) {
        this.content = content;
        this.sender = sender;
    }
}
