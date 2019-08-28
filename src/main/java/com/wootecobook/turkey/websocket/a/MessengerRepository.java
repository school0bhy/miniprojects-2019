package com.wootecobook.turkey.websocket.a;

import com.wootecobook.turkey.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MessengerRepository extends JpaRepository<Messenger, Long> {
    Optional<Messenger> findByUserIdAndMessengerRoomId(Long userId, Long messengerRoomId);

    boolean existsByUserIdAndMessengerRoomId(Long userId, Long messengerRoomId);
}
