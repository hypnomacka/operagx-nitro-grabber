package eu.hypno.nitrograbber;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

public class Grabber {
    public static void main(String[] args) throws IOException, InterruptedException {
        int sleepTimeSeconds = 5 * 60;
        while (true) {
            try {
                URL url = new URL("https://api.discord.gx.games/v1/direct-fulfillment");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("authority", "api.discord.gx.games");
                conn.setRequestProperty("accept", "*/*");
                conn.setRequestProperty("accept-language", "en-GB,en-US;q=0.9,en;q=0.8");
                conn.setRequestProperty("content-type", "application/json");
                conn.setRequestProperty("origin", "https://www.opera.com");
                conn.setRequestProperty("referer", "https://www.opera.com/");
                conn.setRequestProperty("sec-ch-ua", "\"Opera GX\";v=\"105\", \"Chromium\";v=\"119\", \"Not?A_Brand\";v=\"24\"");
                conn.setRequestProperty("sec-ch-ua-mobile", "?0");
                conn.setRequestProperty("sec-ch-ua-platform", "\"Windows\"");
                conn.setRequestProperty("sec-fetch-dest", "empty");
                conn.setRequestProperty("sec-fetch-mode", "cors");
                conn.setRequestProperty("sec-fetch-site", "cross-site");
                conn.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36 OPR/105.0.0.0");
                conn.setDoOutput(true);

                String partnerUserId = UUID.randomUUID().toString();
                String jsonInputString = "{\"partnerUserId\":\"" + partnerUserId + "\"}";

                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                        String responseLine = null;
                        // Open the file in append mode
                        PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("response.txt", true)));
                        while ((responseLine = br.readLine()) != null) {
                            responseLine = responseLine.trim().replace("{\"token\":\"", "https://discord.com/billing/partner-promotions/1180231712274387115/").replace("\"}", "");
                            System.out.println(responseLine);
                            writer.println(responseLine);
                        }
                        writer.close();
                    }
                    sleepWithCountdown(1);
                } else {
                    System.out.println("Error response code: " + responseCode + ". Sleeping for " + sleepTimeSeconds + " seconds.");
                    sleepWithCountdown(sleepTimeSeconds);
                }

                conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Exception" + e.getCause() + ". Sleeping for " + sleepTimeSeconds + " seconds.");
                sleepWithCountdown(sleepTimeSeconds);
            }
        }
    }

    private static void sleepWithCountdown(int sleepTimeSeconds) throws InterruptedException {
        for (int i = sleepTimeSeconds; i > 0; i--) {
            System.out.println("Remaining time until next run: " + i + " seconds");
            Thread.sleep(1000);
        }
    }
}
