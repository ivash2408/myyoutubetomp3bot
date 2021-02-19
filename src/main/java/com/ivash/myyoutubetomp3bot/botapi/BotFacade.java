package com.ivash.myyoutubetomp3bot.botapi;

import com.ivash.myyoutubetomp3bot.userdata.UserData;
import com.ivash.myyoutubetomp3bot.userdata.UserDataCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class BotFacade {

    final UserDataCache userDataCache;

    public BotFacade(UserDataCache userDataCache) {
        this.userDataCache = userDataCache;
    }


    public BotApiMethod<?> response(Message message) {
        long chat_id = message.getChatId();
        String msgText = message.getText();



        switch (msgText) {
            case "/start": {
                break;
            }
            //Если пользователь пишет /download, то следующее сообщение будет воспринято как ссылка на видеоролик
            case "/download": {

                break;
            }
            default: {

                break;
            }
        }

        return null;
    }
}
