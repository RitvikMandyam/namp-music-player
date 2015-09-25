package com.guiker.madmusic;
import android.animation.*;
import android.content.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import java.util.*;

public class MusicAdapter extends BaseAdapter implements OnClickListener
{
	List<Song> songs;
	Context context;

	public MusicAdapter(List<Song> songs, Context context)
	{
		this.songs = songs;
		this.context = context;
	}

	@Override
	public void onClick(View v)
	{
		// TODO: Implement this method
	}

	@Override
	public int getCount()
	{
		return songs.size();
	}

	@Override
	public Object getItem(int position)
	{
		return songs.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	public class ViewHolder
	{
		TextView Title;
		TextView Artist;
		TextView Album;
		ImageView Artwork;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		View v = convertView;
		ViewHolder holder;
		if (convertView == null)
		{
			//Setting up the children
			LayoutInflater inflater = LayoutInflater.from(context);
			v = inflater.inflate(R.layout.song_item, parent, false);

			holder = new ViewHolder();
			holder.Title = (TextView) v.findViewById(R.id.title);
			holder.Artist = (TextView) v.findViewById(R.id.artist);
			holder.Artwork = (ImageView) v.findViewById(R.id.artwork);

			// Taking their candy (not really ;))
			v.setTag(holder);
		}
		else
		{
			// making the children retrievable
			holder = (ViewHolder) v.getTag();
		}
		if (songs.size() <= 0)
		{
			// in case the list is empty
			holder.Title.setText("You haven't got any songs, mate.");
		}
		else
		{
		    // setting up the listview items
			Song song = songs.get(position);
			holder.Title.setText(song.getTitle());
			holder.Artist.setText(song.getArtist());
			holder.Artwork.setImageBitmap(song.getArtwork());
			v.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					MainActivity.songPicked(position);
				}
			});
		}
		return v;
	}
}
