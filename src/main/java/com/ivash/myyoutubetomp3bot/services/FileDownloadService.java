package com.ivash.myyoutubetomp3bot.services;

import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.Buffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.HashMap;
import java.util.Map;


@Service
public class FileDownloadService {

    private RequestToAPIService requestToAPIService;
    private RequestToYoutubeService requestToYoutubeService;

    private String currentYoutubeLink = " ";
    private String currentDownloadFilePath;
    private String downloadLink;

    private Map<String, Boolean> downloadedLinksMap = new HashMap<>();
    private boolean isDownloaded = true;


    public FileDownloadService(RequestToAPIService requestToAPIService, RequestToYoutubeService requestToYoutubeService) {
        this.requestToAPIService = requestToAPIService;
        this.requestToYoutubeService = requestToYoutubeService;
    }

    public boolean isDownloadCompleted() {
        if(downloadedLinksMap.containsKey(currentYoutubeLink)) {
            return downloadedLinksMap.get(currentYoutubeLink);
        }
        else {
            return false;
        }
    }

    public synchronized void downloadFromLink(String youtubeLink) {
        if (youtubeLink != null) {
            if (currentDownloadFilePath == null || !currentYoutubeLink.equals(youtubeLink)) {
                initFilePath(youtubeLink);
            }
            if (downloadLink == null || !currentYoutubeLink.equals(youtubeLink)) {
                initDownloadLink(youtubeLink);
            }

            if(downloadedLinksMap.containsKey(youtubeLink)) {
                if(downloadedLinksMap.get(youtubeLink)) {
                    System.out.println("This file already downloaded");
                }
                else {
                    System.out.println("This file downloading...");
                }
            }
            else {
                downloadedLinksMap.put(youtubeLink, false);
                File downloadFile = new File(currentDownloadFilePath);
                if (!downloadFile.exists()) {
                    currentYoutubeLink = youtubeLink;
                    createDownloadFile(currentDownloadFilePath);
                    if(downloadFile(downloadLink, currentDownloadFilePath)) {
                        downloadedLinksMap.put(youtubeLink, true);
                    }
                    else {
                        System.out.println("File downloading exception");
                    }
                } else {
                    System.out.println("This file already in process");
                }
            }
        }
    }

    private void initFilePath(String youtubeLink) {
        currentDownloadFilePath = createFilePath(requestToYoutubeService.getYoutubeVideoFullName(youtubeLink));
    }

    private void initDownloadLink(String youtubeLink) {
        downloadLink = requestToAPIService.getDownloadLink(youtubeLink);
    }

    private boolean downloadFile(String downloadLink, String filePath) {
        try {
        /*URL url = new URL(downloadLink);
        ReadableByteChannel rbc = Channels.newChannel(url.openStream());
        FileOutputStream fos = new FileOutputStream(filePath);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        fos.close();
        rbc.close();*/
            isDownloaded = false;
            URL url = new URL(downloadLink);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            double fileSize = (double) http.getContentLengthLong();
            BufferedInputStream in = new BufferedInputStream(http.getInputStream());
            FileOutputStream fos = new FileOutputStream(filePath);
            BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
            byte[] buffer = new byte[1024];
            double downloaded = 0.00;
            int read = 0;
            double percentDownloaded = 0.00;
            while ((read = in.read(buffer, 0, 1024)) >= 0) {
                bout.write(buffer, 0, read);
                downloaded += read;
                percentDownloaded = (downloaded * 100) / fileSize;
                String percent = String.format("%.4f", percentDownloaded);
                System.out.println("Downloaded " + percent + "% of a file");
            }
            bout.close();
            in.close();
            isDownloaded = true;
            System.out.println("Download complete");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    public String getCurrentDownloadFilePath() {
        return currentDownloadFilePath;
    }

    private String createFilePath(String youtubeVideoName) {
        return "src/main/resources/static/" + youtubeVideoName + ".mp3";
    }

    private void createDownloadFile(String filePath) {
        File downloadFile = new File(filePath);
        try {
            downloadFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteDownloadFile(String filePath) {
        File downloadFile = new File(filePath);
        if (downloadFile.delete()) {
            System.out.println("File deleted");
        } else {
            System.out.println("Delete Error");
        }
    }
}
