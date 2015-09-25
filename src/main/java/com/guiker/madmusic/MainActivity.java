package com.guiker.madmusic;

import android.app.*;
import android.content.*;
import android.database.*;
import android.graphics.*;
import android.os.*;
import android.provider.*;
import android.view.*;
import android.widget.*;
import com.guiker.madmusic.*;
import java.util.*;

public class MainActivity extends Activity 
{
	private static MusicPlaybackService musicSrv;
	private Intent playIntent;
	private boolean musicBound = false;
	static View root;
	static List<Song> songList;
//	@Bind (R.id.list) ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
		//setting up the UI
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
//		ButterKnife.bind(this);
		root = getWindow().getDecorView();

		if (playIntent == null)
		{
			playIntent = new Intent(this, MusicPlaybackService.class);
			getApplicationContext().bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
			startService(playIntent);
		}

		VolleyUtils.doGet(SpotifyQueryConstructor.makeQuery("Rap God", "Eminem", ""), this, new CallbackInterface() {

				@Override
				public void onComplete(String result)
				{

				}
			});

		songList = getMusic();

		ListView list = (ListView) findViewById(R.id.list);

		View shuffle = LayoutInflater.from(this).inflate(R.layout.shuffle_list_item, list, false);

		ListAdapter adapter = new MusicAdapter(songList, this);
		list.setAdapter(adapter);
		list.addHeaderView(shuffle);
    }

	//connect to the service
	private ServiceConnection musicConnection = new ServiceConnection(){

		@Override
		public void onServiceConnected(ComponentName name, IBinder service)
		{
			MusicPlaybackService.MusicBinder binder = (MusicPlaybackService.MusicBinder)service;
			//get service
			musicSrv = binder.getService();
			//pass list
			musicSrv.setList(getMusic());
			musicBound = true;
		}

		@Override
		public void onServiceDisconnected(ComponentName name)
		{
			musicBound = false;
		}
	};

	private ArrayList<Song> getMusic()
	{
		Bitmap artwork = null;
		//Some audio may be explicitly marked as not being music
		String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

		String[] projection = {
			MediaStore.Audio.Media.DATA,
			MediaStore.Audio.Media.TITLE,
			MediaStore.Audio.Media.ARTIST,
			MediaStore.Audio.Media.ALBUM,
			MediaStore.Audio.Media.ALBUM_ID
		};

		Cursor cursor = this.managedQuery(
			MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
			projection,
			selection,
			null,
			MediaStore.Audio.Media.TITLE + " COLLATE NOCASE ASC");

		String[] projectionImages  = new  String[]{
			MediaStore.Audio.Albums.ALBUM_ART, 
			MediaStore.Audio.Albums.ALBUM_KEY};

		ArrayList<Song> songs = new ArrayList<>();
		while (cursor.moveToNext())
		{
			//Long albumId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
			Long albumId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
			artwork = SdCardManager.getArtworkQuick(this, albumId, 75, 75);
			songs.add(new Song(
						  cursor.getString(0),
						  cursor.getString(1), 
						  cursor.getString(2),
						  cursor.getString(3), 
						  "",
						  artwork));
		}
		return songs;
	}

	public static void songPicked(int pos)
	{
		musicSrv.getUiThread().removeCallbacks(musicSrv.getUpdater());
		musicSrv.setSong(pos);
		musicSrv.playSong();

		TextView title = (TextView) root.findViewById(R.id.readout_title);
		TextView artist = (TextView) root.findViewById(R.id.readout_artist);
		TextView album = (TextView) root.findViewById(R.id.readout_album);
		ImageView artwork = (ImageView) root.findViewById(R.id.current_artwork);

		title.setText(songList.get(pos).getTitle());
		artist.setText(songList.get(pos).getArtist());
		artwork.setImageBitmap(songList.get(pos).getArtwork());

		ImageButton v = (ImageButton) root.findViewById(R.id.play);
		v.setBackgroundResource(R.drawable.pause);
	}

	public void onClickHandler(View v)
	{
		switch (v.getId())
		{
			case R.id.play:
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
			case R.id.readout: 
				startActivity(new Intent(this, SongViewActivity.class));
				overridePendingTransition(R.anim.slide_to_top, R.anim.hang_around);
				break;

			case R.id.shuffle_button: 
				shuffle(true);
				break;
		}
	}

	public static MusicPlaybackService getMusicSrv()
	{
		return musicSrv;
	}

	public static void shuffle(boolean playNext)
	{
		Collections.shuffle(songList);
		Random r = new Random();
		int i1 = r.nextInt(songList.size());
		musicSrv.setList(songList);
		if (playNext)
		{
			songPicked(i1);
		}
	}
}
