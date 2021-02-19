package com.ivash.myyoutubetomp3bot.botapi.handlers;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface UpdateHandler {
    BotApiMethod<?> handle(Message message, long chat_id);
}
