package com.saucedemo.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.util.Objects;
import java.util.stream.Collectors;

public class HttpRequestFactory {
    private final Logger LOG = LoggerFactory.getLogger(HttpRequestFactory.class);

    private void makeHostsTrusted() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509ExtendedTrustManager() {
                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }

                        @Override
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        }

                        @Override
                        public void checkClientTrusted(X509Certificate[] xcs, String string, Socket socket) {
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] xcs, String string, Socket socket) {
                        }

                        @Override
                        public void checkClientTrusted(X509Certificate[] xcs, String string, SSLEngine ssle) {
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] xcs, String string, SSLEngine ssle) {
                        }
                    }
            };

            SSLContext certContext = SSLContext.getInstance("SSL");
            certContext.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(certContext.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String sendRequest(String url) {
        return sendRequest(url, "HEAD");
    }

    public String sendRequest(String url, String requestType) {
        return sendRequest(url, requestType, null, null);
    }

    public String sendRequest(String url, String requestType, String body, String cookie) {
        makeHostsTrusted();
        HttpURLConnection connection = null;
        int timeout = 60000;
        int responseCode = -1;
        String responseMessage;
        String responseBody = null;
        BufferedReader reader;
        int triesAmount = 5;
        while (--triesAmount >= 0 && responseCode != 200) {
            try {
                URL connectionUrl = new URL(url);
                connection = (HttpURLConnection) connectionUrl.openConnection();
                connection.setConnectTimeout(timeout);
                connection.setReadTimeout(timeout);
                connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) " +
                        "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36");
                connection.setRequestMethod(requestType);
                connection.setDoOutput(true);
                if (cookie != null) {
                    connection.setRequestProperty("Cookie", cookie);
                }
                if (body != null) {
                    OutputStream outStream = connection.getOutputStream();
                    OutputStreamWriter outStreamWriter = new OutputStreamWriter(outStream, StandardCharsets.UTF_8);
                    outStreamWriter.write(body);
                    outStreamWriter.flush();
                    outStreamWriter.close();
                    outStream.close();
                }
                connection.connect();
                responseCode = connection.getResponseCode();
                responseMessage = connection.getResponseMessage();
                if (connection.getErrorStream() == null) {
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                } else {
                    reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                }
                responseBody = reader.lines().collect(Collectors.joining());
                LOG.info("{} {} with {} body returned {} {} {}", requestType, connectionUrl,
                        body == null ? "empty" : body, responseCode, responseMessage, responseBody);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                Objects.requireNonNull(connection).disconnect();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        if (responseCode != 200) {
            throw new IllegalStateException(String.format("Response code from URL '%s' should be 200", url));
        }
        return responseBody;
    }
}
