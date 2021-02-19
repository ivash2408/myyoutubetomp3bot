package com.ivash.myyoutubetomp3bot.userdata;

import com.ivash.myyoutubetomp3bot.botapi.BotState;

public class UserData {
    private BotState botState;
    private long chat_id;

    public BotState getBotState() {
        return botState;
    }

    public void setBotState(BotState botState) {
        this.botState = botState;
    }

    public long getChat_id() {
        return chat_id;
    }

    public void setChat_id(long chat_id) {
        this.chat_id = chat_id;
    }
}
