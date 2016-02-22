package practice.hanchen.kknews.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import practice.hanchen.kknews.R;
import practice.hanchen.kknews.activities.ChannelArticleActivity;
import practice.hanchen.kknews.dao.Channel;

/**
 * Created by HanChen on 2016/2/2.
 */
public class ChannelsAdapter extends RecyclerView.Adapter<ChannelsAdapter.ViewHolder> {
	private List<Channel> channelList;
	private Context mContext;
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

	public ChannelsAdapter(Context context, List<Channel> channels) {
		this.channelList = channels;
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
		holder.getTextView().setText(channelList.get(position).getTitle());
		holder.getTextView().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, ChannelArticleActivity.class);
				Bundle bundle = new Bundle();
				bundle.putLong("id", getId(position));
				bundle.putString("title", channelList.get(position).getTitle());
				intent.putExtras(bundle);
				mContext.startActivity(intent);
			}
		});
	}

	@Override
	public int getItemCount() {
		return channelList.size();
	}

	public Long getId(int position) {
		return channelList.get(position).getId();
	}
}
