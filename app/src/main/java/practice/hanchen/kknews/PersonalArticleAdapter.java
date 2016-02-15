package practice.hanchen.kknews;

import android.content.Context;

import java.util.List;

/**
 * Created by HanChen on 2016/2/5.
 */
public class PersonalArticleAdapter extends ArticleAdapter {
	public PersonalArticleAdapter(Context context, List<PersonalList> personalList) {
		super(context, personalList);
	}

	public void onBindViewHolder(final ViewHolder holder, final int position) {
		super.onBindViewHolder(holder, position);
	}
}
