package practice.hanchen.kknews;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ChannelArticleActivity extends ArticleActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle extras = getIntent().getExtras();
		String url = extras.getString("url");

		new GetResponseTack().execute(url);
	}

	private class GetResponseTack extends AsyncTask<String, Void, ArrayList<PersonalList>> {

		@Override
		protected ArrayList<PersonalList> doInBackground(String... params) {
			return getResponse(params[0]);
		}

		@Override
		protected void onPostExecute(ArrayList<PersonalList> personalList) {
			RecyclerView listviewChannelData = (RecyclerView) findViewById(R.id.listview_article_data);
			LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
			listviewChannelData.setLayoutManager(mLayoutManager);
			ChannelArticleAdapter channelArticleAdapter = new ChannelArticleAdapter(getApplicationContext(), personalList);
			listviewChannelData.setAdapter(channelArticleAdapter);
		}

		public ArrayList<PersonalList> getResponse(String urlStr) {
			boolean isInsideItem = false;
			URL url;
			ArrayList<PersonalList> personalList = new ArrayList<PersonalList>();
			String[] description = new String[]{};
			String title = "";
			try {
				url = new URL(urlStr);
				XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
				factory.setNamespaceAware(false);
				XmlPullParser xpp = factory.newPullParser();
				xpp.setInput(getInputStream(url), "UTF_8");
				int eventType = xpp.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT) {
					if (eventType == XmlPullParser.START_TAG) {
						if (xpp.getName().equalsIgnoreCase("item")) {
							isInsideItem = true;
						} else if (xpp.getName().equalsIgnoreCase("description") && isInsideItem) {
							description = splitDescriptionAndPictureURL(xpp.nextText());
						} else if (xpp.getName().equalsIgnoreCase("title") && isInsideItem) {
							title = xpp.nextText();
						}
					} else if (eventType == XmlPullParser.END_TAG) {
						if (xpp.getName().equals("item")) {
							isInsideItem = false;
							personalList.add(new PersonalList(null, 0, title, description[0], description[1]));
						}
					}

					eventType = xpp.next();
				}
			} catch (XmlPullParserException | IOException e) {
				e.printStackTrace();
			}
			return personalList;
		}

		public String[] splitDescriptionAndPictureURL(String description) {
			String[] result = description.split("</a>");
			Document xmlDoc = Jsoup.parse(description);
			Elements imgSrc = xmlDoc.select("img");
			result[0] = imgSrc.get(0).attr("src");
			return result;
		}

		public InputStream getInputStream(URL url) {
			try {
				return url.openConnection().getInputStream();
			} catch (IOException e) {
				return null;
			}
		}
	}
}
