package practice.hanchen.kknews;

import android.content.Context;
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

import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by HanChen on 2016/2/5.
 */
public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {
	protected List<Article> articleList;
	protected Context mContext;
	private static final String LOG_TAG = "ChannelDataAdapter";

	public static class ViewHolder extends RecyclerView.ViewHolder {
		private final TextView labelAlbumName;
		private final ImageView viewAlbum;
		private final TextView labelAlbumDescription;
		private View view;

		public ViewHolder(View v) {
			super(v);
			view = v;
			labelAlbumName = (TextView) v.findViewById(R.id.label_album_name);
			viewAlbum = (ImageView) v.findViewById(R.id.view_album);
			labelAlbumDescription = (TextView) v.findViewById(R.id.label_album_description);
		}

		public TextView getLableAlbumName() {
			return labelAlbumName;
		}

		public ImageView getViewAlbum() {
			return viewAlbum;
		}

		public TextView getLableAlbumDescription() {
			return labelAlbumDescription;
		}

		public View getView() {
			return view;
		}
	}

	public ArticleAdapter(Context context, List<Article> articleList) {
		this.articleList = articleList;
		this.mContext = context;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.listview_item_channel_data, parent, false);
		return new ViewHolder(v);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		holder.getLableAlbumName().setText(articleList.get(position).getTitle());
		holder.getLableAlbumDescription().setText(Html.fromHtml(articleList.get(position).getDescription()));
		holder.getLableAlbumDescription().setMovementMethod(LinkMovementMethod.getInstance());
		Picasso.with(mContext)
				.load(articleList.get(position).getPicURL())
				.placeholder(R.drawable.loading)
				.resize(400, 400)
				.into(holder.getViewAlbum());
	}

	@Override
	public int getItemCount() {
		return articleList.size();
	}
}
