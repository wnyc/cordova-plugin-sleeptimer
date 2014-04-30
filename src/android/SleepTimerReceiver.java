package org.nypr.cordova.sleeptimerplugin;

import org.apache.cordova.PluginResult;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class SleepTimerReceiver extends BroadcastReceiver {

	private static final String LOG_TAG = "SleepTimerReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(LOG_TAG, "sleep timer expired");
		if ( SleepTimerPlugin.connectionCallbackContext != null ) {
			JSONObject o=new JSONObject();
		    PluginResult result=null;
		    try {
		    	o.put("type", "sleep");
		        result = new PluginResult(PluginResult.Status.OK, o);
		    } catch (JSONException e){
		        result = new PluginResult(PluginResult.Status.ERROR, e.getMessage());
		    } finally {
		        SleepTimerPlugin.connectionCallbackContext.sendPluginResult(result);
		    }
		}
	}
}
