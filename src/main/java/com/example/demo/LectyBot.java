package com.example.demo;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonInteraction;

import java.util.List;

public class LectyBot extends ListenerAdapter {

    public static final String token = "<discord bot token>";

    @Override
    public void onReady(ReadyEvent event){
        Guild guild = event.getJDA().getGuildById(404084672931561475L);
        List<Role> roleList = guild.getRoles();
        for(Role role:roleList){
            System.out.println(role.getName());
            System.out.println(role.getId());
        }
        assert guild != null;
        guild.upsertCommand("verify","verify").queue();
    }


    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event){
        if(!event.getName().equals("verify")){
            return;
        }

        event.reply("마이크로소프트 계정을 인증해주세요.")
                .addActionRow(
                        Button.primary("verify","인증하기")
                ).queue();
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event)
    {
        if(event.getComponentId().equals("verify")){
            String userId = event.getUser().getId();
            event.reply("아래 버튼을 눌러 인증해주세요.").addActionRow(
                    Button.link("https://localhost:8080?userId="+userId,"auth")
            ).queue();
        }
    }

}
