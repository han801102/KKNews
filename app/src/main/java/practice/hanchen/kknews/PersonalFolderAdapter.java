package practice.hanchen.kknews;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by HanChen on 2016/2/5.
 */
public class PersonalFolderAdapter extends BaseAdapter {
	private List<PersonalFolder> personalFolders = null;
	private LayoutInflater mInflater;
	private Context mContext;

	public static class ViewHolder {
		public ImageView imageView;
		public TextView textView;
	}

	public PersonalFolderAdapter(Context context, List<PersonalFolder> personalFolders) {
		this.mInflater = LayoutInflater.from(context);
		this.personalFolders = personalFolders;
		this.mContext = context;
	}

	@Override
	public int getCount() {
		return personalFolders.size();
	}

	@Override
	public Object getItem(int position) {
		return personalFolders.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.listview_item_personal_folder, null);
			viewHolder.imageView = (ImageView) convertView.findViewById(R.id.view_folder_cover);
			viewHolder.textView = (TextView) convertView.findViewById(R.id.label_folder_name);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.textView.setText(personalFolders.get(position).getFolderName());
		Picasso.with(convertView.getContext())
				.load(personalFolders.get(position).getDefaultPicUrl())
				.resize(350, 350)
				.centerInside()
				.into(viewHolder.imageView);
		convertView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, PersonalArticleActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("id", personalFolders.get(position).getId().intValue());
				bundle.putString("title", personalFolders.get(position).getFolderName());
				intent.putExtras(bundle);
				mContext.startActivity(intent);
			}
		});
		return convertView;
	}
}
