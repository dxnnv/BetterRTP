package me.SuperRonanCraft.BetterRTP.references.web;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LogUploader {

    private static final String UPLOAD_URL = "https://logs.ronanplugins.com/documents";
    public static final String KEY_URL = "https://logs.ronanplugins.com/";

    @Nullable
   public static String post(List<String> requestBody) {
        try {
            HttpURLConnection connection = getHttpURLConnection(requestBody);

            StringBuilder response = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }

            return response.toString();
        } catch (IOException e) {
            return null;
        }
    }

    private static @NotNull HttpURLConnection getHttpURLConnection(List<String> requestBody) throws IOException {
        URL url = new URL(UPLOAD_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "text/plain");
        connection.setDoOutput(true);

        //String requestBody = "This is the raw body of text.";

        try (OutputStream outputStream = connection.getOutputStream()) {
            for (String str : requestBody) {
                byte[] input = (str + System.lineSeparator()).getBytes(StandardCharsets.UTF_8);
                outputStream.write(input, 0, input.length);
            }
        }
        return connection;
    }

    @Nullable
    public static String post(File file) {
        // Create the connection to the server
        try {
            HttpURLConnection connection = getHttpURLConnection(file);

            StringBuilder response = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }

            return response.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        //getLogger().log(Level.INFO, "Response: " + response.toString());
    }

    private static @NotNull HttpURLConnection getHttpURLConnection(File file) throws IOException {
        URL url = new URL(UPLOAD_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "text/yaml");
        connection.setDoOutput(true);

        try (OutputStream outputStream = connection.getOutputStream()) {
            Scanner scan = new Scanner(file);
            while(scan.hasNextLine()){
                String str = scan.nextLine();
                byte[] input = (str + System.lineSeparator()).getBytes(StandardCharsets.UTF_8);
                outputStream.write(input, 0, input.length);
            }
            scan.close();
        }
        return connection;
    }

}
