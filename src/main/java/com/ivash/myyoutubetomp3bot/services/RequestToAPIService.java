package com.ivash.myyoutubetomp3bot.services;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class RequestToAPIService {

    public RequestToAPIService(){}

    private String downloadLink;

    private void request(String youtubeLink) {
        try {
            String youtubeID = getYoutubeVideoID(youtubeLink);
            String apiLink = getApiLink(youtubeID);
            Document doc = Jsoup.connect(apiLink).get();
            List<Element> downloadLinks = doc.getElementsByAttribute("href");
            downloadLink = (downloadLinks.stream().filter(element -> element.attr("href").contains("/320/")).
                    collect(Collectors.toList()).get(0).attr("href"));
            System.out.println(downloadLink);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Connection error");
        }
    }

    public String getDownloadLink(String youtubeLink) {
        request(youtubeLink);
        return downloadLink;
    }

    private String getYoutubeVideoID(String youtubeLink) {
        return youtubeLink.substring(youtubeLink.indexOf("=") + 1, youtubeLink.indexOf("_channel"));
    }

    private String getApiLink(String youtubeVideoID) {
        String downloadLink = "https://www.yt-download.org/api/button/mp3/" + youtubeVideoID;
        return downloadLink;
    }
}

