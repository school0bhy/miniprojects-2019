package com.wootecobook.turkey.websocket.a;

import com.wootecobook.turkey.commons.resolver.LoginUser;
import com.wootecobook.turkey.commons.resolver.UserSession;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.Set;

@Controller
public class MessengerController {
    private final MessengerService messengerService;

    public MessengerController(MessengerService messengerService) {
        this.messengerService = messengerService;
    }

    @PostMapping("/messenger")
    @ResponseBody
    public Long createMessenger(@RequestBody MessengerRequest messengerRequest, @LoginUser UserSession userSession) {
        Long roomId = messengerService.findRoomId(messengerRequest, userSession.getId());
        return roomId;
    }

    @GetMapping("/messenger/{roomId}")
    public ModelAndView page(@PathVariable  Long roomId, @LoginUser UserSession userSession) {
        messengerService.checkMember(roomId, userSession.getId());
        return new ModelAndView("messenger", "roomId", roomId);
    }

    @MessageMapping("/chat/{roomId}")  //
    @SendTo("/topic/chat/{roomId}")  // 리턴값이 해당 api를 구독하고 있는 클라이언트에 브로드캐스팅
    public MessageResponse chat(Chat chat, SimpMessageHeaderAccessor messageHeaderAccessor) {
        UserSession talker = (UserSession) ((HttpSession) messageHeaderAccessor.getSessionAttributes().get("session")).getAttribute("loginUser");
        return messengerService.sendMessage(talker.getId(), chat.getMessage());
    }
}
