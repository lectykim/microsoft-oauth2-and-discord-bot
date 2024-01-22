package com.example.demo.controller;

import com.example.demo.LectyBot;
import com.fasterxml.jackson.databind.util.JSONPObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.swing.text.html.parser.Entity;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.example.demo.DemoApplication.jda;

@RestController

public class MicrosoftOauthController {


    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @GetMapping("/login/oauth2/code/azure-dev")
    public String recvOauth(
            HttpServletRequest request,
            HttpMethod httpMethod,
            HttpSession session,
            Locale locale,
            @RequestHeader MultiValueMap<String,String> headerMap,
            @RequestHeader("host") String host,
            @RequestParam String code,
            @RequestParam String state
    ){
        String loginUrl = "https://login.microsoftonline.com/{tenant-id}/oauth2/v2.0/token";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        RestTemplate restTemplate = new RestTemplate();
        String urlencoded = "grant_type=authorization_code&code="+code+"&client_id={client-secret}&client_secret={client-secret}&scope=https://graph.microsoft.com/.default offline_access&redirect_url=https://localhost:8080/login/oauth2/code/azure-dev";
        HttpEntity<String> entity = new HttpEntity<String>(urlencoded,headers);
        ResponseEntity<String> response = restTemplate.exchange(loginUrl,HttpMethod.POST,entity,String.class);

        JSONObject jsonObject = new JSONObject(response.getBody());

        String accessToken = (String) jsonObject.get("access_token");

        restTemplate = new RestTemplate();
        headers = new HttpHeaders();
        headers.add("Authorization","Bearer "+accessToken);
        loginUrl = "https://graph.microsoft.com/v1.0/me";
        entity = new HttpEntity<String>("",headers);
        response = restTemplate.exchange(loginUrl,HttpMethod.GET,entity,String.class);

        jsonObject = new JSONObject(response.getBody());
        String userPrincipalName = jsonObject.getString("userPrincipalName");
        String id = jsonObject.getString("id");
        loginUrl = "https://graph.microsoft.com/v1.0/me";
        response = restTemplate.exchange(loginUrl,HttpMethod.GET,entity,String.class);
        System.out.println(response.getBody());

        List<String> blackList = redisTemplate.opsForList().range("blackList",0,-1);

        for(String str: blackList)
        {
            if(str.equals(id)){
                return "밴 된 사용자입니다.";
            }
        }


        List<String> whiteList = redisTemplate.opsForList().range("whiteList",0,-1);
        for(String str:whiteList)
        {
            //재 인증의 경우
            if(str.equals(id)){

                JDA currentJda = jda;
                Guild guild = currentJda.getGuildById(404084672931561475L);
                Role role = currentJda.getRoleById(1198829387768926208L);
                User user = currentJda.getUserById(state);
                assert guild != null;
                assert user != null;
                assert role != null;
                guild.addRoleToMember(user,role).queue();
                return "success";
            }
        }


        //재인증이 아닌 경우에는 DB 저장이 필요하다
        redisTemplate.opsForList().rightPush("whiteList",id);
        JDA currentJda = jda;
        Guild guild = currentJda.getGuildById(404084672931561475L);
        Role role = currentJda.getRoleById(1198829387768926208L);
        User user = currentJda.getUserById(state);
        assert guild != null;
        assert user != null;
        assert role != null;
        guild.addRoleToMember(user,role).queue();

        return "success";
    }



}
