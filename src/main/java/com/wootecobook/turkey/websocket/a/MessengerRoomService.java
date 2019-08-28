package com.wootecobook.turkey.websocket.a;

import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class MessengerRoomService {

    private static final String NOT_FOUND_MESSAGE = "존재하지 않는 메신저방입니다.";

    private final MessengerRoomRepository messengerRoomRepository;

    public MessengerRoomService(MessengerRoomRepository messengerRoomRepository) {
        this.messengerRoomRepository = messengerRoomRepository;
    }

    public MessengerRoom save() {
        return messengerRoomRepository.save(new MessengerRoom());
    }

    public MessengerRoom findById(Long roomId) {
        return messengerRoomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException(NOT_FOUND_MESSAGE));
    }
}
