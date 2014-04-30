package org.nypr.cordova.sleeptimerplugin;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

public class SleepTimerPlugin extends CordovaPlugin {
	
	protected static final String LOG_TAG = "SleepTimerPlugin";
		
	protected boolean _countdown;
	protected Timer _countdownTimer;
	protected long _time;
	public static CallbackContext connectionCallbackContext;
	
	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		boolean ret=true;
		try {
			if(action.equalsIgnoreCase("sleep")){
				Log.d(LOG_TAG, "SleepTimer Plugin sleeping...");
				JSONObject options = args.getJSONObject(0);
				
				int seconds = 0;
				if (options.has("sleep")) {
					seconds = options.getInt("sleep");
				}
				
				boolean _countdown = false;
				if (options.has("countdown")) {
					_countdown=options.getBoolean("countdown");
				}
				
				Intent intent = new Intent(cordova.getActivity().getApplicationContext(), SleepTimerReceiver.class);
				
				PendingIntent sender = PendingIntent.getBroadcast(cordova.getActivity().getApplicationContext(), 88888, intent, PendingIntent.FLAG_ONE_SHOT);
				AlarmManager alarmManager = (AlarmManager) cordova.getActivity().getApplicationContext().getSystemService(Context.ALARM_SERVICE);
				alarmManager.cancel(sender);
				
				if (_countdownTimer!=null){
					_countdownTimer.cancel();
					_countdownTimer=null;
				}
				
				if (seconds > 0) {
					_time = System.currentTimeMillis() + (seconds * 1000);
					alarmManager.set(AlarmManager.RTC_WAKEUP, _time, sender);
					
					if (_countdown) {
						_countdownTimer = new Timer();
						_countdownTimer.scheduleAtFixedRate(
								new TimerTask() {
									public void run() {
										long remaining = (long)Math.ceil( (_time - System.currentTimeMillis()) / 1000);
										JSONObject o=new JSONObject();
									    PluginResult result=null;
									    try {
									    	if (remaining<=0){
									    		_countdownTimer.cancel();
									    		_countdownTimer=null;
									    	}
									    	
									    	o.put("type", "countdown");
									    	o.put("timeLeft", remaining);
									        result = new PluginResult(PluginResult.Status.OK, o);
									    } catch (JSONException e){
									        result = new PluginResult(PluginResult.Status.ERROR, e.getMessage());
									    } finally {
									        result.setKeepCallback(true);
									        SleepTimerPlugin.connectionCallbackContext.sendPluginResult(result);
									    }
									}
								},
								1000,
								1000);
					}
				}
				
				SleepTimerPlugin.connectionCallbackContext = callbackContext;
	            PluginResult pluginResult = new PluginResult(PluginResult.Status.OK);
	            pluginResult.setKeepCallback(true);
	            callbackContext.sendPluginResult(pluginResult);  
	            
			}else{
				callbackContext.error(LOG_TAG + " error: invalid action (" + action + ")");
				ret=false;
			}
		} catch (JSONException e) {
			callbackContext.error(LOG_TAG + " error: invalid json");
			ret = false;
		} catch (Exception e) {
			callbackContext.error(LOG_TAG + " error: " + e.getMessage());
			ret = false;
		}
		return ret;
	}
}
