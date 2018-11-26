package server.businessLogic;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;


public class SendPushNotification {

    /**
     * 
     */
    private static final long serialVersionUID = -8022560668279505764L;

 // Method to send Notifications from server to client end.
    public final static String AUTH_KEY_FCM = "SERVER_KEY_HERE";
    public final static String API_URL_FCM = "https://fcm.googleapis.com/fcm/send";
    public static String DEVICE_ID = "DEVICE_TOKEN_HERE";

    public String execute() {
        String DeviceIdKey = DEVICE_ID;
        String authKey = AUTH_KEY_FCM;
        String FMCurl = API_URL_FCM;

        try {
            URL url = new URL(FMCurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "key=" + authKey);
            conn.setRequestProperty("Content-Type", "application/json");
            System.out.println(DeviceIdKey);
            JSONObject data = new JSONObject();
            data.put("to", DeviceIdKey.trim());
            JSONObject info = new JSONObject();
            info.put("title", "FCM Notificatoin Title"); // Notification title
            info.put("body", "Hello First Test notification"); // Notification body
            data.put("data", info);
            System.out.println(data.toString());
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data.toString());
            wr.flush();
            wr.close();

            int responseCode = conn.getResponseCode();
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return "Success";
        }
        catch(Exception e)
        {
            System.out.println(e);
        }

        return "Success";
    }
}
