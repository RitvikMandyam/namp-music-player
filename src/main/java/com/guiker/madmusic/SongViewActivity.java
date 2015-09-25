package com.guiker.madmusic;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.media.session.*;

public class SongViewActivity extends Activity
{
	private static MusicPlaybackService musicSrv;
	private static SeekBar seekbar;
	public static boolean active = false;
	private static View root;

	@Override
	public void onStart()
	{
		super.onStart();
		active = true;
	} 

	@Override
	public void onStop()
	{
		super.onStop();
		active = false;
	}

	@Override
    protected void onCreate(Bundle savedInstanceState)
    {
	    //setting up the UI
        super.onCreate(savedInstanceState);
        setContentView(R.layout.song_view);

		musicSrv = MainActivity.getMusicSrv();
		root = getWindow().getDecorView();
		seekbar = (SeekBar) findViewById(R.id.seekbar);
		musicSrv.updateSeekbar();
		ImageButton play  =(ImageButton) findViewById(R.id.seekbar_pause);
		if (musicSrv.playing())
		{
			play.setBackgroundResource(R.drawable.pause);
		}
		else
		{
			play.setBackgroundResource(R.drawable.play);
		}

		seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

				@Override
				public void onStopTrackingTouch(SeekBar seekBar)
				{
				}

				@Override
				public void onStartTrackingTouch(SeekBar seekBar)
				{
					musicSrv.getUiThread().removeCallbacks(musicSrv.getUpdater());
				}

				@Override
				public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
				{       
					if (musicSrv.getPlayer() != null && fromUser)
					{
						float prog = (((float) progress) / 5000) * musicSrv.getTime();
						musicSrv.getPlayer().seekTo(Math.round(prog));
					}
					musicSrv.getUiThread().postDelayed(musicSrv.getUpdater(), 100);
				}
			});
	}

	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
		overridePendingTransition(R.anim.pop_in,  R.anim.slide_to_bottom);
	}

	public static SeekBar getSeekbar()
	{
		return seekbar;
	}

	public static View getRootView()
	{
		return root;
	}

	public void onClickHandler(View v)
	{
		switch (v.getId())
		{
			case R.id.seekbar_pause:
				musicSrv.playPause();
				if (musicSrv.playing())
				{
					v.setBackgroundResource(R.drawable.pause);
				}
				else
				{
					v.setBackgroundResource(R.drawable.play);
				}
				break;
				
			case R.id.seekbar_shuffle: 
				MainActivity.shuffle(false);
				break;
		}
	}
}
