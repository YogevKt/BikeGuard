package server.businessLogic;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import server.entities.User;

public class FireBaseServiceHandler {
	
	// Method to send Notifications from server to client end.
	public final static String AUTH_KEY_FCM = "AIzaSyARny0iGRtcLmdjd63G92ZF-Hu9ByebwCg";//"SERVER_KEY_HERE";
	public final static String API_URL_FCM = "https://fcm.googleapis.com/fcm/send";
	
	
	public static String sendPushNotification(User user,String title,String body) {
		 String DeviceIdKey = user.getToken();
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
	            System.out.println(DeviceIdKey); // Form Debug
	            JSONObject data = new JSONObject();
	            data.put("to", DeviceIdKey.trim());
	            JSONObject info = new JSONObject();
	            info.put("title", title); // Notification title
	            info.put("body", body); // Notification body
	            data.put("notification", info);
	            System.out.println(data.toString()); // Form Debug
	            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
	            wr.write(data.toString());
	            wr.flush();
	            wr.close();

	            int responseCode = conn.getResponseCode();
	            System.out.println("Response Code : " + responseCode); // Form Debug

	            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	            String inputLine;
	            StringBuffer response = new StringBuffer();

	            while ((inputLine = in.readLine()) != null) {
	                response.append(inputLine);
	            }
	            
	            System.out.println("Response: " + response); // Form Debug
	            
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
