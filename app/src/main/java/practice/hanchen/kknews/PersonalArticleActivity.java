package practice.hanchen.kknews;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

public class PersonalArticleActivity extends ArticleActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle extras = getIntent().getExtras();
		int id = extras.getInt("id");
		RecyclerView listviewChannelData = (RecyclerView) findViewById(R.id.listview_article_data);
		LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
		listviewChannelData.setLayoutManager(mLayoutManager);

		PersonalArticleAdapter personalArticleAdapter = new PersonalArticleAdapter(getApplicationContext(), getPersonalList(id));
		listviewChannelData.setAdapter(personalArticleAdapter);
	}

	private List<PersonalList> getPersonalList(int id) {
		QueryBuilder<PersonalList> queryBuilder = DBHelper.getInstance(getApplicationContext()).getPersonalListDao().queryBuilder();
		queryBuilder.where(PersonalListDao.Properties.FolderId.eq(id));
		return queryBuilder.list();
	}
}
