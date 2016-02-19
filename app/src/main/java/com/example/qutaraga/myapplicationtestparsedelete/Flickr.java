package com.example.qutaraga.myapplicationtestparsedelete;


import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class Flickr {
    //запрос на сарвер
    String FlickrQuery_url =  "https://api.flickr.com/services/rest/?method=flickr.photos.search&license=1,2,3,4,5,6&content_type=7";

    //количество возвращаемых элементов
    String FlickrQuery_per_page = "&per_page=10";
    String FlickrQuery_page = "&page=";
    String FlickrQuery_nojsoncallback = "&nojsoncallback=1";
    String FlickrQuery_format = "&format=json";
    String FlickrQuery_tag = "&tags=";
    String FlickrQuery_key = "&api_key=92dd4373337de5de44cbf6a5806c9ff7";


    int countPage;


    public String QueryFlickr(String q){

        String qResult = null;

        String qString =
                FlickrQuery_url
                        + FlickrQuery_per_page+FlickrQuery_page + getCountPage() + FlickrQuery_nojsoncallback
                        + FlickrQuery_format
                        + FlickrQuery_tag + q
                        + FlickrQuery_key;

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(qString);

        try {
            HttpEntity httpEntity = httpClient.execute(httpGet).getEntity();
            if (httpEntity != null){
                InputStream inputStream = httpEntity.getContent();
                Reader in = new InputStreamReader(inputStream);
                BufferedReader bufferedreader = new BufferedReader(in);
                StringBuilder stringBuilder = new StringBuilder();

                String stringReadLine = null;

                while ((stringReadLine = bufferedreader.readLine()) != null) {
                    stringBuilder.append(stringReadLine + "\n");
                }
                qResult = stringBuilder.toString();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return qResult;
    }

    public int getCountPage() {
        return countPage;
    }

    public void setCountPage(int countPage) {
        this.countPage = countPage;
    }

}
