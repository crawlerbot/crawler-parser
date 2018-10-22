package io.github.crawlerbot.client;


import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;


public class HttpEngine {
    private final CloseableHttpClient client;

    public HttpEngine(String userAgent) {
        if (userAgent == null || userAgent.isEmpty()) {
            userAgent = "Mozilla/5.0 (X11; Linux i686; rv:10.0) Gecko/20100101 Firefox/10.0";
        }
        client = HttpClients.custom().setUserAgent(userAgent).build();
    }

    public String getRedirectUrl(String url) {
        CloseableHttpClient httpclient = HttpClients.custom()
                .setRedirectStrategy(new LaxRedirectStrategy())
                .build();

        try {
            HttpClientContext context = HttpClientContext.create();
            HttpGet httpGet = new HttpGet(url);
            System.out.println("Executing request " + httpGet.getRequestLine());
            System.out.println("----------------------------------------");

            httpclient.execute(httpGet, context);
            HttpHost target = context.getTargetHost();
            List<URI> redirectLocations = context.getRedirectLocations();
            URI location = URIUtils.resolve(httpGet.getURI(), target, redirectLocations);
            System.out.println("Final HTTP location: " + location.toASCIIString());
            return location.toASCIIString();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public Document doFetch(String url) {
        String realUrl = getRedirectUrl(url);
        HttpGet request = null;
        try {
            request = new HttpGet(realUrl);
            HttpResponse httpResponse = client.execute(request);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if(statusCode == 200) {
                String html =  EntityUtils.toString(httpResponse.getEntity());
                Document document = Jsoup.parse(html);
                document.setBaseUri(realUrl);
                return document;
            }
            return null;
        }catch (Exception ex){
            return null;
        }finally {
            if(request != null) {
                request.releaseConnection();
            }
        }

    }

}
