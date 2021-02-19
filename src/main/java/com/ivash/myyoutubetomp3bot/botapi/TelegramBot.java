package com.ivash.myyoutubetomp3bot.botapi;

import com.ivash.myyoutubetomp3bot.services.FileDownloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class TelegramBot extends TelegramWebhookBot {
    private String webHookPath;
    private String botUserName;
    private String botToken;

    private Map<String, Boolean> sentFilesMap = new HashMap<>();

    @Autowired
    private FileDownloadService fileDownloadService;

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public BotApiMethod onWebhookUpdateReceived(Update update) {
        Message message = update.getMessage();
        if(message != null && message.hasText()) {
            long chat_id = message.getChatId();
            String msgText = message.getText();
            response(msgText, chat_id);
/*            try {
                execute(new SendMessage(chat_id, msgText));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }*/
        }
        return null;
    }

    @Override
    public String getBotPath() {
        return webHookPath;
    }

    @Override
    public String getBotUsername() {
        return botUserName;
    }

    public void setWebHookPath(String webHookPath) {
        this.webHookPath = webHookPath;
    }

    public void setBotUserName(String botUserName) {
        this.botUserName = botUserName;
    }

    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }

    private void response(String msg, long chat_id) {
        if(msg.contains("/download")) {
            String [] temp = msg.split(" ");
            fileDownloadService.downloadFromLink(temp[1]);
            String filePath = fileDownloadService.getCurrentDownloadFilePath();
            if(fileDownloadService.isDownloadCompleted()) {
                if(!sentFilesMap.containsKey(temp[1])) {
                    if(sendDownloadedMP3(chat_id, filePath)) {
                        sentFilesMap.put(temp[1], true);
                        fileDownloadService.deleteDownloadFile(filePath);
                    }
                }
                else {
                    System.out.println("File already sent");
                }
            }
        }
    }

    private boolean sendDownloadedMP3(long chat_id, String filePath){
        File file = new File(filePath);
        SendAudio audio = new SendAudio();
        audio.setAudio(file);
        audio.setChatId(chat_id);
        try {
            execute(audio);
            return true;
        } catch (TelegramApiException e) {
            try {
                execute(new SendMessage(chat_id, "Ошибка при отправке файла"));
            } catch (TelegramApiException telegramApiException) {
                telegramApiException.printStackTrace();
            }
            e.printStackTrace();
            return false;
        }
    }

    private void sendMessage(long chat_id, String text){
        try {
            execute(new SendMessage(chat_id, text));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
