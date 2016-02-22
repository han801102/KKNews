package practice.hanchen.kknews.adapter;

import android.content.Context;
import android.view.View;

import java.util.List;

import practice.hanchen.kknews.adapter.ArticleAdapter;
import practice.hanchen.kknews.dao.Article;

/**
 * Created by HanChen on 2016/2/5.
 */
public class PersonalArticleAdapter extends ArticleAdapter {
	public PersonalArticleAdapter(Context context, List<Article> articles) {
		super(context, articles);
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, final int position) {
		super.onBindViewHolder(holder, position);
		holder.getView().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (holder.getLableAlbumDescription().getVisibility() == View.GONE) {
					holder.getLableAlbumDescription().setVisibility(View.VISIBLE);
				} else {
					holder.getLableAlbumDescription().setVisibility(View.GONE);
				}
			}
		});
	}
}
