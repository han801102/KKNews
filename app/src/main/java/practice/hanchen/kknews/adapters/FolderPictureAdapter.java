package practice.hanchen.kknews.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import practice.hanchen.kknews.R;
import practice.hanchen.kknews.dao.Article;

/**
 * Created by HanChen on 2016/2/15.
 */
public class FolderPictureAdapter extends RecyclerView.Adapter<FolderPictureAdapter.ViewHolder> {
	private List<Article> articles;
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

		public ImageView getViewFolderCover() {
			return viewFolderCover;
		}

		public View getView() {return view;}
	}

	public FolderPictureAdapter(Context context, List<Article> articles) {
		this.articles = articles;
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
		holder.getView().setSelected(selectedPosition == position);
		holder.getView().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (selectedPosition == -1) {
					selectedPosition = position;
				} else if (selectedPosition == position) {
					selectedPosition = -1;
				} else {
					selectedPosition = position;
				}
				notifyDataSetChanged();
			}
		});
		Picasso.with(mContext)
				.load(articles.get(position).getPicURL())
				.resize(300, 300)
				.into(holder.getViewFolderCover());
	}

	@Override
	public int getItemCount() {
		return articles.size();
	}

	public String getCoverURL() {
		if (selectedPosition == -1) {
			return "";
		} else {
			return articles.get(selectedPosition).getPicURL();
		}
	}
}
