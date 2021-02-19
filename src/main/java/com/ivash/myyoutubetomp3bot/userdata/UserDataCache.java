package com.ivash.myyoutubetomp3bot.userdata;

import com.ivash.myyoutubetomp3bot.botapi.BotState;
import org.springframework.stereotype.Component;

import java.util.HashMap;
//Данный класс будет хранить в себе данные о пользователях, а также управлять ими (добавить, удалить)
@Component
public class UserDataCache {
    private HashMap<Long, BotState> usersBotStates= new HashMap<>();
    private HashMap<Long, UserData> usersData = new HashMap<>();

    public void addUser(UserData userData) {
        if(!usersData.containsKey(userData.getChat_id())) {
            usersData.put(userData.getChat_id(), userData);
        }
    }

    public void removeUser(UserData userData) {
        if(!usersData.containsKey(userData.getChat_id())) {
            usersData.remove(userData);
        }
    }
}
