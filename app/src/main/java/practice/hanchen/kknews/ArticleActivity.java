package practice.hanchen.kknews;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ArticleActivity extends AppCompatActivity {
	private String pageTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_article);
		Bundle extras = getIntent().getExtras();
		pageTitle = extras.getString("title");

		TextView labelChannelTitle = (TextView) findViewById(R.id.label_page_title);
		labelChannelTitle.setText(pageTitle);
	}
}
