package by.coolproj;

import by.coolproj.bots.AleshaBot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
public class Runner {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Runner.class, args);
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(ctx.getBean("aleshaBot", AleshaBot.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
