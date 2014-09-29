package com.tw.friendhelp.service;

import static com.tw.friendhelp.model.CommonUtilities.TAG;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gcm.GCMRegistrar;
import com.tw.friendhelp.R;
import com.tw.friendhelp.model.CommonUtilities;

public final class ServerUtilities {
	static Context ctx;

	// Register this account/device pair within the server.

	public static void register(final Context context, String userId, final String regId) {
		ctx = context;
		Log.i(TAG, "registering device (regId = " + regId + ")");
		JSONObject jobj = new JSONObject();
		try {
			jobj.put("user_id", userId);
			jobj.put("gcm_regid", regId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		new SendGcmData().execute(jobj.toString());
	}

	/**
	 * Unregister this account/device pair within the server.
	 */
	// static void unregister(final Context context, final String regId) {
	// Log.i(TAG, "unregistering device (regId = " + regId + ")");
	// String serverUrl = SERVER_URL + "/unregister";
	// Map<String, String> params = new HashMap<String, String>();
	// params.put("regId", regId);
	// try {
	// post(serverUrl, params);
	// GCMRegistrar.setRegisteredOnServer(context, false);
	// String message = context.getString(R.string.server_unregistered);
	// CommonUtilities.displayMessage(context, message);
	// } catch (IOException e) {
	// // At this point the device is unregistered from GCM, but still
	// // registered in the server.
	// // We could try to unregister again, but it is not necessary:
	// // if the server tries to send a message to the device, it will get
	// // a "NotRegistered" error message and should unregister the device.
	// String message = context.getString(R.string.server_unregister_error,
	// e.getMessage());
	// CommonUtilities.displayMessage(context, message);
	// }
	// }
}

class SendGcmData extends AsyncTask<String, Void, JSONArray> {
	JSONArray jaa;

	@Override
	protected JSONArray doInBackground(String... arg0) {
		List<NameValuePair> signup = new ArrayList<NameValuePair>(1);
		signup.add(new BasicNameValuePair("GcmInfo", arg0[0]));

		JSONArray jarray = new DbConnect().workingMethod("UpdateGcmId", signup);
		return jarray;

	}

	@Override
	protected void onPostExecute(JSONArray result) {
		super.onPostExecute(result);
		if (result != null) {
			Log.i("jsonaaa", result.toString());
//			GCMRegistrar.setRegisteredOnServer(ServerUtilities.ctx, true);
//			String message = ServerUtilities.ctx.getString(R.string.server_registered);
//			CommonUtilities.displayMessage(ServerUtilities.ctx, message);
		}
	}
}
