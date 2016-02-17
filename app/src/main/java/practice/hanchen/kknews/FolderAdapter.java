package practice.hanchen.kknews;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.security.acl.LastOwnerException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by HanChen on 2016/2/15.
 */
public class FolderAdapter extends BaseAdapter {
	protected List<PersonalFolder> personalFolders = null;
	private LayoutInflater mInflater;
	protected Context mContext;
	protected boolean selectedMode;
	protected ArrayList<Boolean> isSelected;

	public static class ViewHolder {
		public ImageView imageView;
		public TextView textView;
	}

	public FolderAdapter(Context context, List<PersonalFolder> personalFolders) {
		this.mInflater = LayoutInflater.from(context);
		this.personalFolders = personalFolders;
		this.mContext = context;
		this.selectedMode = false;
		isSelected = new ArrayList<Boolean>();
		for(int i = 0; i < personalFolders.size(); i++) {
			this.isSelected.add(false);
		}
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
		if (isSelected.get(position)) {
			viewHolder.textView.setTextColor(Color.BLUE);
		} else {
			viewHolder.textView.setTextColor(Color.BLACK);
		}
		viewHolder.textView.setGravity(Gravity.CENTER_HORIZONTAL);
		Picasso.with(convertView.getContext())
				.load(personalFolders.get(position).getDefaultPicUrl())
				.resize(350, 350)
				.centerInside()
				.into(viewHolder.imageView);
		return convertView;
	}

	public void changeSelectedMode() {
		selectedMode = !selectedMode;
	}

	public boolean getSelectedMode() {
		return selectedMode;
	}

	public void resetSelection() {
		isSelected = new ArrayList<Boolean>();
		for(int i = 0;i < personalFolders.size(); i++) {
			isSelected.add(i, false);
		}
		selectedMode = false;
	}
}
