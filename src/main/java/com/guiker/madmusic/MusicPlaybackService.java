package com.guiker.madmusic;

import android.app.*;
import android.content.*;
import android.media.*;
import android.net.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import java.util.concurrent.*;

public class MusicPlaybackService extends Service implements
MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
MediaPlayer.OnCompletionListener 
{
	private boolean loading = false;
    //media player
	private MediaPlayer player;
    //song list
	private List<Song> songs;
    //current position
	private int songPosn;
	//binder
	private final IBinder musicBind = new MusicBinder();

	public void setList(List<Song> songList)
	{
		songs = songList;
	}

	public void setList(ArrayList<Song> theSongs)
	{
		songs = theSongs;
	}

	@Override
	public void onCreate()
	{
		//create the service
		super.onCreate();
        //initialize position
		songPosn = 0;
        //create player
		player = new MediaPlayer();
		initMusicPlayer();
		loading = true;
	}

	public void initMusicPlayer()
	{
		//set player properties
		player.setWakeMode(getApplicationContext(),
						   PowerManager.PARTIAL_WAKE_LOCK);
		player.setAudioStreamType(AudioManager.STREAM_MUSIC);

		player.setOnPreparedListener(this);
		player.setOnCompletionListener(this);
		player.setOnErrorListener(this);
	}

	public void playSong()
	{
		try
		{
			loading = true;
			getUiThread().removeCallbacks(mUpdateTimeTask);
			//resetting the music player
			player.reset();
			//get song
			Song playSong = songs.get(songPosn);
			//get id
			String currSong = playSong.getPath();
			//set uri
			Uri trackUri = Uri.parse(currSong);

			try
			{
				player.setDataSource(getApplicationContext(), trackUri);
			}
			catch (Exception e)
			{
				Log.e("MUSIC SERVICE", "Error setting data source", e);
			}
			player.prepareAsync();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void setSong(int songIndex)
	{
		songPosn = songIndex;
	}

	public boolean playing()
	{
		return player.isPlaying();
	}

	public void playPause()
	{
		if (player.isPlaying())
		{
			player.pause();
		}
		else
		{
			player.start();
		}
	}

	public int getTime()
	{
		return player.getDuration();
	}

	public void seek(int millis)
	{
		player.seekTo(millis);
	}

	public void updateSeekbar()
	{
        getUiThread().postDelayed(mUpdateTimeTask, 100);
    }   

    /**
     * Background Runnable thread
     * */
    private Runnable mUpdateTimeTask = new Runnable() {
		public void run()
		{
			long totalDuration = player.getDuration();
			long currentDuration = player.getCurrentPosition();

			/*// Displaying Total Duration time
			 songTotalDurationLabel.setText(""+utils.milliSecondsToTimer(totalDuration));
			 // Displaying time completed playing
			 songCurrentDurationLabel.setText(""+utils.milliSecondsToTimer(currentDuration));*/

			// Updating progress bar
			float perc = ((float)currentDuration) / totalDuration * 5000;

			//Log.d("Progress", ""+progress);
			SongViewActivity.getSeekbar().setMax(5000);
			SongViewActivity.getSeekbar().setProgress(Math.round(perc));
			
			View root = SongViewActivity.getRootView();
			
			TextView currentTime = (TextView) root.findViewById(R.id.seekbar_currentTime);
			TextView endTime = (TextView) root.findViewById(R.id.seekbar_endTime);
			
			String current = String.format("%d:%02d", 
										   TimeUnit.MILLISECONDS.toMinutes(currentDuration),
										   TimeUnit.MILLISECONDS.toSeconds(currentDuration) - 
										   TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(currentDuration))
										   );
										   
			String end = String.format("%d:%02d", 
										   TimeUnit.MILLISECONDS.toMinutes(totalDuration),
										   TimeUnit.MILLISECONDS.toSeconds(totalDuration) - 
										   TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(totalDuration))
										   );
										   
										  currentTime.setText(current);
										  endTime.setText(end);
			

			// Running this thread after 100 milliseconds
			if (!loading)
			{
				getUiThread().postDelayed(this, 100);
			}
		}
	};
	
	public Runnable getUpdater() {
		return mUpdateTimeTask;
	}

	public Handler getUiThread()
	{
		return new Handler(Looper.getMainLooper());
	}

	public class MusicBinder extends Binder
	{
		MusicPlaybackService getService()
		{
			return MusicPlaybackService.this;
		}
	}

	@Override
	public void onCompletion(MediaPlayer p1)
	{
		MainActivity.songPicked(songPosn + 1);
	}

	@Override
	public boolean onError(MediaPlayer p1, int p2, int p3)
	{
		return true;
	}

	@Override
	public void onPrepared(MediaPlayer p1)
	{
		//start playback
		player.start();
		loading = false;
		if (SongViewActivity.active)
		{
			updateSeekbar();
		}
		else {
			getUiThread().removeCallbacks(mUpdateTimeTask);
		}
	}
	
	public MediaPlayer getPlayer() {
		return player;
	}


	@Override
	public IBinder onBind(Intent intent)
	{
		return musicBind;
	}

	@Override
	public boolean onUnbind(Intent intent)
	{
		player.stop();
		player.release();
		return false;
	}
}
