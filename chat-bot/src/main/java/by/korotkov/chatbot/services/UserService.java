package by.korotkov.chatbot.services;

import by.korotkov.chatbot.models.Percent;
import by.korotkov.chatbot.models.UserChatBot;
import by.korotkov.chatbot.repositories.UserChatBotRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class UserService {
    private final UserChatBotRepository userChatBotRepository;

    public UserService(UserChatBotRepository userChatBotRepository) {
        this.userChatBotRepository = userChatBotRepository;
    }

    public void addPercent(Long chatId, Percent percent){
        UserChatBot userChatBot = UserChatBot.builder()
                .userId(chatId)
                .percent(percent)
                .build();
        userChatBotRepository.saveOrUpdateUser(userChatBot);
    }

    public List<UserChatBot> getUsers(){
        return userChatBotRepository.getAllUsers();
    }
}
