package server.businessLogic;

import java.util.HashMap;
import java.util.Map;

public class UserAlertsService {
 
	public static enum Alert {
		INTERSECTION_ENTRANCE ,MEDIUM, HIGH
	}
	
	private Map<String, UserAlerts> mapUsersAlerts;
	private static UserAlertsService instance = null;
	
	private UserAlertsService() {
		mapUsersAlerts = new HashMap<>();	
	}
	
	public static UserAlertsService getInstance() {
		if (instance == null) {
			instance = new UserAlertsService();
		}
		
		return instance;
	}
	
	/*** 
	 * Add alert to the user and create one if not exists
	 * 
	 * @param String, UserAlertsService.Alert
	 * 
	 */
	private void setUserAlert(String token, Alert alert) {
		
		if(mapUsersAlerts.containsKey(token)) {
			mapUsersAlerts.get(token).alertOccurred(alert);

		}else {
			mapUsersAlerts.put(token, new UserAlerts(alert));
			
		}
	}
	
	/*** 
	 * Check if the user was alerted, if not - add alert to user
	 * 
	 * @param String, UserAlertsService.Alert
	 * 
	 * @return If user was alerted
	 */
	public boolean isUserAlerted(String token, Alert alert) {
		boolean res = false;
		
		if(mapUsersAlerts.containsKey(token)) {
			res = mapUsersAlerts.get(token).isAlerted(alert);
	
		}
	
		if(!res)
			setUserAlert(token, alert);
		
		return res;
	}
	
	public void removeUserAlert(String token) {
		if(mapUsersAlerts.containsKey(token)) {
			mapUsersAlerts.remove(token);
		}
	}
	
	
	private class UserAlerts
	{
		private boolean[] isUserAlerted;
		
		public UserAlerts(Alert alert) {
			isUserAlerted = new boolean[Alert.values().length];
			alertOccurred(alert);
		}
		
		public boolean isAlerted(Alert alert) {
			return isUserAlerted[alert.ordinal()];
		}
		
		public void alertOccurred(Alert alert) {
			isUserAlerted[alert.ordinal()] = true;
		}
	}
}
