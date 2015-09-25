package com.guiker.madmusic;

import android.graphics.*;

public class Song
{
	/* Not much to see here. It's 
	pretty much just constructors,
	getters and setters. The path 
	variable is where the song is 
	on the device. */
	
	String path;
	String title;
	String artist;
	String album;
	String year;
	Bitmap artwork;

	public Song(String path, String title, String artist, String album, String year, Bitmap artwork)
	{
		this.path = path;
		this.title = title;
		this.artist = artist;
		this.album = album;
		this.year = year;
		this.artwork = artwork;
	}

	public Song(String title, String artist, String album, String year, Bitmap artwork)
	{
		this.title = title;
		this.artist = artist;
		this.album = album;
		this.year = year;
		this.artwork = artwork;
	}

	public Song(String title, String artist, String album, String year)
	{
		this.title = title;
		this.artist = artist;
		this.album = album;
		this.year = year;
	}

	public Song(String path)
	{
		this.path = path;
	}


	public void setArtwork(Bitmap artwork)
	{
		this.artwork = artwork;
	}

	public Bitmap getArtwork()
	{
		return artwork;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getTitle()
	{
		return title;
	}

	public void setArtist(String artist)
	{
		this.artist = artist;
	}

	public String getArtist()
	{
		return artist;
	}

	public void setAlbum(String album)
	{
		this.album = album;
	}

	public String getAlbum()
	{
		return album;
	}

	public void setYear(String year)
	{
		this.year = year;
	}

	public String getYear()
	{
		return year;
	}

	public void setPath(String path)
	{
		this.path = path;
	}

	public String getPath()
	{
		return path;
	}}
