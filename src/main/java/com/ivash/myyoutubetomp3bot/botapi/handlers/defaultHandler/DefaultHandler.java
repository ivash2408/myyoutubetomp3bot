package com.ivash.myyoutubetomp3bot.botapi.handlers.defaultHandler;


import com.ivash.myyoutubetomp3bot.botapi.handlers.UpdateHandler;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class DefaultHandler implements UpdateHandler {
    @Override
    public BotApiMethod<?> handle(Message message, long chat_id) {
        String replyMessageText = "${reply.default}";

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chat_id);
        sendMessage.setText(replyMessageText);
        return sendMessage;
    }
}
