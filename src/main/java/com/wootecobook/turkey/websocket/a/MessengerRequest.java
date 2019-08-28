package com.wootecobook.turkey.websocket.a;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class MessengerRequest {
    private Set<Long> userIds;

    public void add(Long userId) {
        userIds.add(userId);
    }
}
