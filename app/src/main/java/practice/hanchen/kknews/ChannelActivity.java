package practice.hanchen.kknews;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.os.Bundle;

import android.util.Log;

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

public class ChannelActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_channel);
		Bundle extras = getIntent().getExtras();
		String url = extras.getString("url");
		new GetResponseTack().execute(url);
	}

	private class GetResponseTack extends AsyncTask<String, Void, ArrayList<ChannelData>> {

		@Override
		protected ArrayList<ChannelData> doInBackground(String... params) {
			return getResponse(params[0]);
		}

		@Override
		protected void onPostExecute(ArrayList<ChannelData> channelDatas) {
			RecyclerView listviewChannelData = (RecyclerView) findViewById(R.id.listview_channel_data);
			LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
			listviewChannelData.setLayoutManager(mLayoutManager);

			ChannelDataAdapter channelDataAdapter = new ChannelDataAdapter(getApplicationContext(), channelDatas);
			listviewChannelData.setAdapter(channelDataAdapter);
		}

		public ArrayList<ChannelData> getResponse(String urlStr) {
			boolean isInsideItem = false;
			URL url;
			ArrayList<ChannelData> channelDatas = new ArrayList<ChannelData>();
			String link = "";
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
						} else if (xpp.getName().equalsIgnoreCase("link") && isInsideItem) {
							link = xpp.nextText();
						} else if (xpp.getName().equalsIgnoreCase("description") && isInsideItem) {
							description = splitDescriptionAndPictureURL(xpp.nextText());
						} else if (xpp.getName().equalsIgnoreCase("title") && isInsideItem) {
							title = xpp.nextText();
						}
					} else if (eventType == XmlPullParser.END_TAG) {
						if (xpp.getName().equals("item")) {
							isInsideItem = false;
							channelDatas.add(new ChannelData(title, description[1], link, description[0]));
						}
					}

					eventType = xpp.next(); //move to next element
				}
			} catch (MalformedURLException | XmlPullParserException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return channelDatas;
		}

		public String[] splitDescriptionAndPictureURL(String description) {
			String[] result = description.split("</a>");
			Document xmlDoc =  Jsoup.parse(description);
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
