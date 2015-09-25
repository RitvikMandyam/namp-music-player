package com.guiker.madmusic;

import java.util.*;

public class MusicQueue
{
	ArrayList<Song> songs;
	ArrayList<Song> originalSongs;

	public MusicQueue(ArrayList<Song> songs)
	{
		this.songs = songs;
		this.originalSongs = songs;
	}
	
	public MusicQueue() {
		
	}

	public void setOriginalSongs(ArrayList<Song> originalSongs)
	{
		this.originalSongs = originalSongs;
	}
	
	public void add(Song song) {
		songs.add(song);
	}
	
	public void remove(int pos) {
		songs.remove(pos);
	}
	
	public void shuffle() {
		Collections.shuffle(songs);
	}

	public void setSongs(ArrayList<Song> songs)
	{
		this.songs = songs;
	}

	public ArrayList<Song> getSongs()
	{
		return songs;
	}
	
	public ArrayList<Song> getOriginalSongs()
	{
		return originalSongs;
	}
}
