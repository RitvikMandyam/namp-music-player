package com.guiker.madmusic;

import android.content.*;
import com.android.volley.*;
import com.android.volley.toolbox.*;

public class VolleyUtils
{
	public static void doGet(String url, Context context, final CallbackInterface callback) {
		// Just a basic GET request. Not much happening.
		RequestQueue queue = Volley.newRequestQueue(context);
		
        // Request a string response from the provided URL.
		StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
            new Response.Listener<String>() {
				@Override
				public void onResponse(String response) {
					/* callback is the function
					that actually does something 
					with the response. */
					callback.onComplete(response);
				}
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					// Same thing as in onResponse.
		            callback.onComplete("Something went wrong!");
				}
			});
		queue.add(stringRequest);
	}
}
