package com.wootecobook.turkey.websocket.a;

import com.wootecobook.turkey.commons.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MessengerRoom extends BaseEntity {

    @OneToMany(mappedBy = "messengerRoom")
    private List<Messenger> users = new ArrayList<>();
}
