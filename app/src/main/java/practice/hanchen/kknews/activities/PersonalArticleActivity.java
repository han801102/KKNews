package practice.hanchen.kknews.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import practice.hanchen.kknews.helpers.DBHelper;
import practice.hanchen.kknews.adapters.PersonalArticleAdapter;
import practice.hanchen.kknews.R;

public class PersonalArticleActivity extends ArticleActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle extras = getIntent().getExtras();
		Long id = extras.getLong("id");
		RecyclerView listViewChannelData = (RecyclerView) findViewById(R.id.listview_article_data);
		LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
		listViewChannelData.setLayoutManager(mLayoutManager);

		DBHelper dbHelper = DBHelper.getInstance(getApplicationContext());
		PersonalArticleAdapter personalArticleAdapter =
				new PersonalArticleAdapter(getApplicationContext(), dbHelper.getPersonalArticle(dbHelper.getPersonalListByFolderId(id)));
		listViewChannelData.setAdapter(personalArticleAdapter);
	}

}
