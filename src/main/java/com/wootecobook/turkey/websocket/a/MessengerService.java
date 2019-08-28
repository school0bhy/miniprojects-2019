package com.wootecobook.turkey.websocket.a;

import com.wootecobook.turkey.user.domain.User;
import com.wootecobook.turkey.user.service.UserService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MessengerService {

    private static final String NOT_FOUND_MESSAGE = "존재하지 않는 메신저입니다.";

    private final MessengerRepository messengerRepository;
    private final MessengerRoomService messengerRoomService;
    private final UserService userService;

    public MessengerService(MessengerRepository messengerRepository, MessengerRoomService messengerRoomService, UserService userService) {
        this.messengerRepository = messengerRepository;
        this.messengerRoomService = messengerRoomService;
        this.userService = userService;
    }

    public Long findRoomId(MessengerRequest messengerRequest, Long userId) {
        messengerRequest.add(userId);
        Set<User> users = messengerRequest.getUserIds().stream()
                .map(userService::findById)
                .collect(Collectors.toSet());

        MessengerRoom messengerRoom = createMessengerRoom(users);
        return messengerRoom.getId();
    }

    public Long findMessengerId(Long userId, Long messengerRoomId) {
        Messenger messenger = messengerRepository.findByUserIdAndMessengerRoomId(userId, messengerRoomId)
                .orElseThrow(() -> new EntityNotFoundException(NOT_FOUND_MESSAGE));
        return messenger.getId();
    }

    private MessengerRoom createMessengerRoom(Set<User> users) {
        MessengerRoom messengerRoom = messengerRoomService.save();
        users.forEach(user -> messengerRepository.save(new Messenger(messengerRoom, user)));
        return messengerRoom;
    }

    public void checkMember(Long roomId, Long userId) {
        if (!messengerRepository.existsByUserIdAndMessengerRoomId(userId, roomId)) {
            throw new AccessDeniedException();
        }
    }

    public MessageResponse sendMessage(Long userId, String message) {
        return new MessageResponse(message, userService.findUserResponseById(userId));
    }
}
