package practice.hanchen.kknews;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by HanChen on 2016/2/2.
 */
//public class ChannelsAdapter extends BaseAdapter {
public class ChannelsAdapter extends RecyclerView.Adapter<ChannelsAdapter.ViewHolder> {
	private ArrayList<ChannelInfo> channelInfoArrayList;
	private Context mContext;
	private LayoutInflater mInflater;
	private static final String LOG_TAG = "ChannelsAdapter";

	public static class ViewHolder extends RecyclerView.ViewHolder {
		private final TextView textView;

		public ViewHolder(View v) {
			super(v);
			textView = (TextView) v.findViewById(R.id.label_channels);
		}

		public TextView getTextView() {
			return textView;
		}
	}

	public ChannelsAdapter(Context context, ArrayList<ChannelInfo> channelList) {
		this.channelInfoArrayList = channelList;
		this.mContext = context;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.listview_item_channels, parent, false);
		return new ViewHolder(v);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, final int position) {
		holder.getTextView().setText(channelInfoArrayList.get(position).getTitle());
		holder.getTextView().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, ChannelArticleActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("url", getURL(position));
				bundle.putString("title", channelInfoArrayList.get(position).getTitle());
				intent.putExtras(bundle);
				mContext.startActivity(intent);
			}
		});
	}

	@Override
	public int getItemCount() {
		return channelInfoArrayList.size();
	}

	public String getURL(int position) {
		return channelInfoArrayList.get(position).getUrl();
	}
}
