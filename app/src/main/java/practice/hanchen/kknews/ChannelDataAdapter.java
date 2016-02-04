package practice.hanchen.kknews;

import android.content.Context;
import android.opengl.Visibility;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by HanChen on 2016/2/2.
 */
//public class ChannelsAdapter extends BaseAdapter {
public class ChannelDataAdapter extends RecyclerView.Adapter<ChannelDataAdapter.ViewHolder> {
	private ArrayList<ChannelData> channelDataArrayList;
	private Context mContext;
	private static final String LOG_TAG = "ChannelDataAdapter";
	public static class ViewHolder extends RecyclerView.ViewHolder
			implements View.OnClickListener {
		private final TextView lableAlbumName;
		private final ImageView viewAlbum;
		private final TextView lableAlbumDescription;

		public ViewHolder(View v) {
			super(v);
			v.setOnClickListener(this);
			lableAlbumName = (TextView) v.findViewById(R.id.lable_album_name);
			viewAlbum = (ImageView) v.findViewById(R.id.view_album);
			lableAlbumDescription = (TextView) v.findViewById(R.id.lable_album_description);
		}


		@Override
		public void onClick(View v) {

			if (lableAlbumDescription.getVisibility() == View.GONE) {
				lableAlbumDescription.setVisibility(View.VISIBLE);
			} else {
				lableAlbumDescription.setVisibility(View.GONE);
			}
			Log.d("han", lableAlbumDescription.getText().toString());
		}

		public TextView getLableAlbumName() {
			return lableAlbumName;
		}

		public ImageView getViewAlbum() {
			return viewAlbum;
		}

		public TextView getLableAlbumDescription() {
			return lableAlbumDescription;
		}
	}

	public ChannelDataAdapter(Context context, ArrayList<ChannelData> channelList) {
		this.channelDataArrayList = channelList;
		this.mContext = context;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.listview_item_channel_data, parent, false);
		return new ViewHolder(v);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, final int position) {
		holder.getLableAlbumName().setText(channelDataArrayList.get(position).getTitle());
		holder.getLableAlbumDescription().setText(Html.fromHtml(channelDataArrayList.get(position).getDescription()));
		holder.getLableAlbumDescription().setMovementMethod(LinkMovementMethod.getInstance());
		Picasso.with(mContext)
				.load(channelDataArrayList.get(position).getPictureURL())
				.into(holder.getViewAlbum());
	}

	@Override
	public int getItemCount() {
		return channelDataArrayList.size();
	}
}
