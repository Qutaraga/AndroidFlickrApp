package com.example.qutaraga.myapplicationtestparsedelete.Flickr;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class Flickr {
    //запрос на сарвер
    private static final String FLICKR_QUERY_URL = "https://api.flickr.com/services/rest/?method=flickr.photos.search&license=1,2,3,4,5,6&content_type=7";
    //количество возвращаемых элементов
    private static final String FLIKRQUERY_PER_PAGE = "&per_page=10";
    private static final String FLICKRQUERY_PAGE = "&page=";
    private static final String FLICKRQUERY_NOJSONCALLBACK = "&nojsoncallback=1";
    private static final String FLICKRQUERY_FORMAT = "&format=json";
    private static final String FLICKRQUERY_TAG = "&tags=";
    private static final String FLICKRQUERY_KEY = "&api_key=92dd4373337de5de44cbf6a5806c9ff7";

    int countPage;
    StringBuilder buf;

    public String QueryFlickr(String q){
        String line;
        String qString =
                FLICKR_QUERY_URL
                        + FLIKRQUERY_PER_PAGE+FLICKRQUERY_PAGE + getCountPage() + FLICKRQUERY_NOJSONCALLBACK
                        + FLICKRQUERY_FORMAT
                        + FLICKRQUERY_TAG + q
                        + FLICKRQUERY_KEY;
        try {
            URL url=new URL(qString);
            HttpURLConnection c=(HttpURLConnection)url.openConnection();
            c.setRequestMethod("GET");
            c.setReadTimeout(10000);
            c.connect();
            BufferedReader reader= new BufferedReader(new InputStreamReader(c.getInputStream()));
            buf=new StringBuilder();
            while ((line=reader.readLine()) != null) {
                buf.append(line + "\n");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return(buf.toString());
    }

    public int getCountPage() {
        return countPage;
    }

    public void setCountPage(int countPage) {
        this.countPage = countPage;
    }

}
