package practice.hanchen.kknews;

import android.content.Context;
import android.graphics.LightingColorFilter;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

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
