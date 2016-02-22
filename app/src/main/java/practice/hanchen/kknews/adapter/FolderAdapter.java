package practice.hanchen.kknews.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import practice.hanchen.kknews.R;
import practice.hanchen.kknews.dao.PersonalFolder;

/**
 * Created by HanChen on 2016/2/19.
 */
public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.ViewHolder> {
	protected List<PersonalFolder> personalFolderList = null;
	protected Context mContext;

	public static class ViewHolder extends RecyclerView.ViewHolder {
		private ImageView viewFolderCover;
		private TextView labelFolderName;
		private View view;

		public View getView() {
			return view;
		}

		public ViewHolder(View v) {
			super(v);
			viewFolderCover = (ImageView) v.findViewById(R.id.view_folder_cover);
			labelFolderName = (TextView) v.findViewById(R.id.label_folder_name);
			view = v;
		}

		public TextView getLabelFolderName() {
			return labelFolderName;
		}

		public ImageView getViewFolderCover() {
			return viewFolderCover;
		}
	}

	public FolderAdapter(Context context, List<PersonalFolder> personalFolders) {
		this.mContext = context;
		this.personalFolderList = personalFolders;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.listview_item_personal_folder, parent, false);
		return new ViewHolder(v);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, final int position) {
		Picasso.with(mContext)
				.load(personalFolderList.get(position).getDefaultPicUrl())
				.resize(300, 300)
				.into(holder.getViewFolderCover());
		holder.getLabelFolderName().setText(personalFolderList.get(position).getFolderName());
		holder.getLabelFolderName().setGravity(Gravity.CENTER_HORIZONTAL);
		holder.getLabelFolderName().setTextColor(Color.BLACK);
	}

	@Override
	public int getItemCount() {
		return personalFolderList.size();
	}
}
