package by.korotkov.chatbot.utils;

import by.korotkov.chatbot.services.TelegramMessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.support.PeriodicTrigger;

@Configuration
@EnableScheduling
public class SchedulerConfig {

    @Autowired
    private TelegramMessageSender telegramMessageSender;

    @Scheduled(fixedDelayString = "${scheduler.fixedDelay}")
    public void scheduleTask() {
        telegramMessageSender.sendMessages();
    }
}