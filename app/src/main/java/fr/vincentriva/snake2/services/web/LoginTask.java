package fr.vincentriva.snake2.services.web;

import android.os.AsyncTask;
import android.util.Log;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class LoginTask extends AsyncTask<URL, Void, Boolean> {
    private Document document;
    private WebService service;
    private final int MAX_ATTEMPTS = 5;

    LoginTask(WebService service) {
        this.service = service;
        this.document = null;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        this.service.notifyLogin(aBoolean, this.document);
    }

    @Override
    protected Boolean doInBackground(URL... urls) {
        Boolean isConnected = false;
        int numberAttempts = 0;
        try {
            do {
                HttpURLConnection connection = (HttpURLConnection) urls[0].openConnection();
                connection.setRequestMethod("GET");
                connection.setDoOutput(true);
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(10000);
                connection.connect();
                Log.d("ISnake", "Connecting");

                if(connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    Log.d("ISnake", "Connected");
                    Map<String, List<String>> headersFields = connection.getHeaderFields();
                    List<String> cookiesHeader = headersFields.get("Set-Cookie");

                    if(cookiesHeader != null) {
                        for(String cookie : cookiesHeader) {
                            android.webkit.CookieManager cookieManager = android.webkit.CookieManager.getInstance();
                            cookieManager.setCookie("snake", cookie);
                        }
                    }

                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuffer buffer = new StringBuffer();
                    String inputLine;

                    while((inputLine = reader.readLine()) != null) {
                        buffer.append(inputLine);
                    }

                    reader.close();

                    Log.d("ISnake", buffer.toString());

                    DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();
                    InputSource inputSource = new InputSource(new StringReader(buffer.toString()));
                    this.document = documentBuilder.parse(inputSource);
                    isConnected = true;
                }
                numberAttempts++;
            } while(!isConnected && numberAttempts < MAX_ATTEMPTS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isConnected;
    }
}
