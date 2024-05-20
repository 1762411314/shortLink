package org.sulong.project12306.services.shortlinkservice.service.Impl;

import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import org.sulong.project12306.services.shortlinkservice.service.UrlTitleService;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * URL 标题接口实现层
 */
@Service
public class UrlTitleServiceImpl implements UrlTitleService {

    @Override
    @SneakyThrows
    public String getTitleByUrl(String url) {
        URL targetUrl=new URL(url);
        HttpURLConnection connection=(HttpURLConnection) targetUrl.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        if (connection.getResponseCode()== HttpURLConnection.HTTP_OK){
            Document document= Jsoup.connect(url).get();
            return document.title();
        }
        return "Error while fetching title.";
    }
}
