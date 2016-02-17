package practice.hanchen.kknews;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by HanChen on 2016/2/15.
 */
public class FolderPictureAdapter extends RecyclerView.Adapter<FolderPictureAdapter.ViewHolder> {
	private List<PersonalList> personalLists;
	private Context mContext;
	private int selectedPosition;

	public static class ViewHolder extends RecyclerView.ViewHolder {
		private ImageView viewFolderCover;
		private View view;

		public ViewHolder(View v) {
			super(v);
			viewFolderCover = (ImageView) v.findViewById(R.id.view_folder_cover);
			view = v;
		}

		public ImageView getViewFolderCover(){
			return viewFolderCover;
		}
		public View getView() {return view;}
	}

	public FolderPictureAdapter(Context context, List<PersonalList> personalLists) {
		this.personalLists = personalLists;
		this.mContext = context;
		this.selectedPosition = -1;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.listview_item_personal_folder, parent, false);
		return new ViewHolder(v);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, final int position) {
		holder.getView().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (selectedPosition == -1) {
					setSelected(v, position, true);
					selectedPosition = position;
				} else if (selectedPosition == position) {
					setSelected(v, position, false);
					selectedPosition = -1;
				} else {
					setSelected(v, position, true);
					setSelected(v, selectedPosition, false);
					selectedPosition = position;
				}
			}
		});
		Picasso.with(mContext)
				.load(personalLists.get(position).getPicURL())
				.resize(300, 300)
				.into(holder.getViewFolderCover());
	}

	@Override
	public int getItemCount() {
		return personalLists.size();
	}

	private void setSelected(View v, int position, boolean isSelect) {
		RecyclerView recyclerView = (RecyclerView) v.getParent();
		ViewHolder viewHolder = (ViewHolder) recyclerView.findViewHolderForAdapterPosition(position);

		if (isSelect) {
			viewHolder.getViewFolderCover().setBackgroundColor(Color.BLUE);
		} else {
			viewHolder.getViewFolderCover().setBackgroundColor(Color.WHITE);
		}
	}

	public String getCoverURL() {
		if (selectedPosition == -1) {
			return "";
		} else {
			return personalLists.get(selectedPosition).getPicURL();
		}
	}
}
