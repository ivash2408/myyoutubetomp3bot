package com.ivash.myyoutubetomp3bot.botapi.handlers.downloadHandler;

import com.ivash.myyoutubetomp3bot.botapi.handlers.UpdateHandler;
import com.ivash.myyoutubetomp3bot.services.FileDownloadService;
import com.ivash.myyoutubetomp3bot.services.RequestToAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class DownloadHandler implements UpdateHandler {

    private final FileDownloadService fileDownloadService;

    public DownloadHandler(FileDownloadService fileDownloadService) {
        this.fileDownloadService = fileDownloadService;
    }

    @Override
    public BotApiMethod<?> handle(Message message, long chat_id) {
        String messageText = message.getText();
        String replyMessageText;

        fileDownloadService.downloadFromLink(messageText);

        //будет обращаться к file download service проверяя скачать ли файл
        //если загрузка не окончена, то новая загрузка начинаться не будет
        return null;
    }
}
