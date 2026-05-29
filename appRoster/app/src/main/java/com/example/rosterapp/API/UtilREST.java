package com.example.rosterapp.API;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class UtilREST {

    public enum QueryType {
        GET,
        POST,
        PUT,
        DELETE
    }

    public static class Response {
        public int statusCode;
        public String content;

        public Response(int statusCode, String content) {
            this.statusCode = statusCode;
            this.content = content;
        }
    }

    public interface OnResponseListener {
        void onSuccess(Response r);
        void onError(Response r);
    }

    public static void runQuery(QueryType type,
                                String urlString,
                                String body,
                                String token,
                                OnResponseListener listener) {

        new AsyncTask<Void, Void, Response>() {

            @Override
            protected Response doInBackground(Void... voids) {

                HttpURLConnection connection = null;

                try {
                    URL url = new URL(urlString);

                    connection = (HttpURLConnection) url.openConnection();

                    connection.setRequestMethod(type.name());
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setRequestProperty("Accept", "application/json");

                    if (token != null && !token.isEmpty()) {
                        connection.setRequestProperty("Authorization", "Bearer " + token);
                    }

                    if (type == QueryType.POST || type == QueryType.PUT) {
                        connection.setDoOutput(true);

                        BufferedWriter writer = new BufferedWriter(
                                new OutputStreamWriter(connection.getOutputStream())
                        );

                        writer.write(body);
                        writer.flush();
                        writer.close();
                    }

                    int statusCode = connection.getResponseCode();

                    BufferedReader reader;

                    if (statusCode >= 200 && statusCode < 300) {
                        reader = new BufferedReader(
                                new InputStreamReader(connection.getInputStream())
                        );
                    } else {
                        reader = new BufferedReader(
                                new InputStreamReader(connection.getErrorStream())
                        );
                    }

                    StringBuilder response = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    reader.close();

                    return new Response(statusCode, response.toString());

                } catch (Exception e) {
                    e.printStackTrace();
                    return new Response(500, e.getMessage());

                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }

            @Override
            protected void onPostExecute(Response response) {
                if (response.statusCode >= 200 && response.statusCode < 300) {
                    listener.onSuccess(response);
                } else {
                    listener.onError(response);
                }
            }

        }.execute();
    }
}