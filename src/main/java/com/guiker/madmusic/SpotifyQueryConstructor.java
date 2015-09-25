package com.guiker.madmusic;

import android.text.*;
import java.net.*;

public class SpotifyQueryConstructor
{
	public static String makeQuery(String title, 
	String artist, String album) {
	      String query = Reference.SpotifyRoot;
		  
		  if (!TextUtils.isEmpty(title)) {
			  query += URLEncoder.encode(title);
		  }
		  
		  if (!TextUtils.isEmpty(artist)) {
			  query += URLEncoder.encode(" artist:" + artist);
		  }
		  
		  if (!TextUtils.isEmpty(album)) {
			  query += URLEncoder.encode(" album:" + album);
		  }
		  
		  query += "&type=track";
		  
		  return query;
	}
}
