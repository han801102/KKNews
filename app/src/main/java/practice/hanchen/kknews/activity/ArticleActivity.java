package practice.hanchen.kknews.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import practice.hanchen.kknews.R;

public class ArticleActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_article);
		Bundle extras = getIntent().getExtras();
		String pageTitle = extras.getString("title");

		TextView labelChannelTitle = (TextView) findViewById(R.id.label_page_title);
		labelChannelTitle.setText(pageTitle);
	}
}
