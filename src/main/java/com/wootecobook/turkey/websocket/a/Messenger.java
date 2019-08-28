package com.wootecobook.turkey.websocket.a;

import com.wootecobook.turkey.commons.domain.UpdatableEntity;
import com.wootecobook.turkey.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Messenger extends UpdatableEntity {

    @ManyToOne
    @JoinColumn(name = "messenger_room_id")
    private MessengerRoom messengerRoom;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Messenger(MessengerRoom messengerRoom, User user) {
        this.messengerRoom = messengerRoom;
        this.user = user;
    }
}
