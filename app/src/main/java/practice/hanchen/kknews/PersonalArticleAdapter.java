package practice.hanchen.kknews;

import android.content.Context;
import android.view.View;

import java.util.List;

/**
 * Created by HanChen on 2016/2/5.
 */
public class PersonalArticleAdapter extends ArticleAdapter {
	public PersonalArticleAdapter(Context context, List<PersonalList> personalList) {
		super(context, personalList);
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
