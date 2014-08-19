package com.tw.friendhelp.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;

public class DbConnect {
	String Tag = "DbConnect";
	InputStream is = null;
	String url = "http://192.168.173.1/friendhelp/index.php?r=api/";
	String subUrl;
	List<NameValuePair> ll;

	public JSONArray workingMethod(String Suburl, List<NameValuePair> aaa) {
		String result = null;
		JSONArray jarray = null;
		subUrl = Suburl;
		ll = aaa;
		// get the response
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(url + Suburl);
			Log.d("RequestUrl", url + subUrl + aaa);
			post.setEntity(new UrlEncodedFormEntity(aaa));

			HttpResponse response = client.execute(post);

			HttpEntity entity = response.getEntity();
			is = entity.getContent();
		} catch (ClientProtocolException e) {
			Log.e(Tag, "error in network protocol");
			e.printStackTrace();
		} catch (IOException e) {
			Log.e(Tag, "error in network network " + e.getMessage());
			e.printStackTrace();
			return jarray;
		}
		// convert response to string (get the json array)
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result = sb.toString();
		} catch (Exception e) {
			Log.e(Tag, "buffered reader error " + e.getMessage());
			e.printStackTrace();
		}
		// get the required data using json array
		try {
			Log.d("Response", result);
			jarray = new JSONArray(result);
			// for (int i = 0; i < jarray.length(); i++) {
			// jObject = jarray.getJSONObject(i);
			// }
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jarray;
	}

	public JSONArray workingMethod(String Suburl) {
		String result = null;
		JSONArray jarray = null;
		subUrl = Suburl;
		// get the response
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(url + Suburl);

			HttpResponse response = client.execute(post);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
		} catch (ClientProtocolException e) {
			Log.e(Tag, "error in network protocol");
			e.printStackTrace();
		} catch (IOException e) {
			Log.e(Tag, "error in network network " + e.getMessage());
			e.printStackTrace();
			return jarray;
		}
		// convert response to string (get the json array)
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result = sb.toString();
		} catch (Exception e) {
			Log.e(Tag, "buffered reader error " + e.getMessage());
			e.printStackTrace();
		}
		// get the required data using json array
		try {
			Log.d("Response", result);
			jarray = new JSONArray(result);
			// for (int i = 0; i < jarray.length(); i++) {
			// jObject = jarray.getJSONObject(i);
			// }
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jarray;
	}

}
