package com.wootecobook.turkey.websocket.a;

import com.wootecobook.turkey.commons.domain.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Message extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "messenger_id", nullable = false, foreignKey = @ForeignKey(name = "fk_message_to_messenger"), updatable = false)
    private Messenger messenger;

    @Lob
    @Column(nullable = false)
    private String contents;
}
