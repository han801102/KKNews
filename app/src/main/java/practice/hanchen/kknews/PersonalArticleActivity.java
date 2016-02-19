package practice.hanchen.kknews;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class PersonalArticleActivity extends ArticleActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle extras = getIntent().getExtras();
		int id = extras.getInt("id");
		RecyclerView listViewChannelData = (RecyclerView) findViewById(R.id.listview_article_data);
		LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
		listViewChannelData.setLayoutManager(mLayoutManager);

		DBHelper dbHelper = DBHelper.getInstance(getApplicationContext());
		PersonalArticleAdapter personalArticleAdapter =
				new PersonalArticleAdapter(getApplicationContext(), dbHelper.getPersonalArticle(dbHelper.getPersonalListByFolderId(id)));
		listViewChannelData.setAdapter(personalArticleAdapter);
	}

}
