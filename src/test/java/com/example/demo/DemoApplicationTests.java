package com.example.demo;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class DemoApplicationTests extends ListenerAdapter {

    LectyBot lectyBot = new LectyBot();
    @Test
    void contextLoads() {
    }



    @Test
    void 봇_테스트(){
        JDABuilder.createLight(lectyBot.token,GatewayIntent.GUILD_MESSAGES,GatewayIntent.DIRECT_MESSAGES)
                .addEventListeners(new LectyBot())
                .setActivity(Activity.playing("Type !Ping"))
                .build();
    }

}
