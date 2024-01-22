package com.example.demo;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class DemoApplication {

    public static JDA jda;

    public static void main(String[] args) {
        jda = JDABuilder.createDefault(LectyBot.token)
                .enableIntents(GatewayIntent.DIRECT_MESSAGES,GatewayIntent.GUILD_MESSAGES,GatewayIntent.DIRECT_MESSAGE_REACTIONS)
                .addEventListeners(new LectyBot())
                .setActivity(Activity.playing("개발"))
                .build();

        SpringApplication.run(DemoApplication.class, args);


    }

}
