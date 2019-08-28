package com.wootecobook.turkey.websocket.a;

public class Chat {
    private String name;
    private String message;

    public Chat() {
    }

    public Chat(String name, String message) {
        this.name = name;
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }
}