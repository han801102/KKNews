package practice.hanchen.kknews;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

public class PersonalArticleActivity extends ArticleActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle extras = getIntent().getExtras();
		int id = extras.getInt("id");
		RecyclerView listviewChannelData = (RecyclerView) findViewById(R.id.listview_article_data);
		LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
		listviewChannelData.setLayoutManager(mLayoutManager);

		DBHelper dbHelper = DBHelper.getInstance(getApplicationContext());
		PersonalArticleAdapter personalArticleAdapter = new PersonalArticleAdapter(getApplicationContext(), dbHelper.getPersonalListByFolderId(id));
		listviewChannelData.setAdapter(personalArticleAdapter);
	}
}
