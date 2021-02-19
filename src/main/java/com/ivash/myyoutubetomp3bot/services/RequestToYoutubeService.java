package com.ivash.myyoutubetomp3bot.services;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RequestToYoutubeService {

    public RequestToYoutubeService() {}

    private static class YoutubeVideoInfo {

        public YoutubeVideoInfo() {}

        private static String videoName;
        private static String videoAuthor;

        public String getVideoAuthor() {
            return videoAuthor;
        }

        public void setVideoAuthor(String videoAuthor) {
            this.videoAuthor = videoAuthor;
        }

        public String getVideoName() {
            return videoName;
        }

        public void setVideoName(String videoName) {
            this.videoName = videoName;
        }
    }

    private void request(String youtubeLink) {
        YoutubeVideoInfo videoInfo = new YoutubeVideoInfo();
        try {
            Document doc = Jsoup.connect(youtubeLink).get();

            String videoName = doc.getElementsByAttribute("property").stream().
                    filter(element -> element.attr("property").equals("og:title")).
                    collect(Collectors.toList()).get(0).attr("content");
            String videoAuthor = getYoutubeVideoAuthorFromLink(youtubeLink);

            videoInfo.setVideoName(videoName);
            videoInfo.setVideoAuthor(videoAuthor);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getYoutubeVideoFullName(String youtubeVideo) {
        request(youtubeVideo);
        return YoutubeVideoInfo.videoName.replaceAll("\\p{Punct}", "_");
    }

    private String getYoutubeVideoAuthorFromLink(String youtubeLink) {
        return youtubeLink.substring(youtubeLink.lastIndexOf("=")+1);
    }
}
