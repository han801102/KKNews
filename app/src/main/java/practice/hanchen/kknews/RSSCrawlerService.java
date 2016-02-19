package practice.hanchen.kknews;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RSSCrawlerService extends Service {
	private List<Channel> channels;
	private List<Article> articles;

	@Override
	public void onCreate() {
		DBHelper dbHelper = DBHelper.getInstance(getApplicationContext());
		channels = dbHelper.getChannels();
		while (channels.size() == 0) {
			try {
				Thread.sleep(500);
				channels = dbHelper.getChannels();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		articles = dbHelper.getArticleAll();
	}

	@Override
	public void onDestroy() {
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		new GetRSSTask().execute(channels.toArray(new Channel[channels.size()]));
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO: Return the communication channel to the service.
		throw new UnsupportedOperationException("Not yet implemented");
	}

	private class GetRSSTask extends AsyncTask<Channel, Void, Void> {

		@Override
		protected Void doInBackground(Channel... params) {
			ArrayList<Article> articleFromDB = new ArrayList<>();
			for (Channel param : params) {
				try {
					URL url = new URL(param.getURL());
					Document xmlDoc = Jsoup.parse(url, 3000);
					Elements elements = xmlDoc.select("channel");
					Elements items = elements.select("item");
					for (Element item : items) {
						String itemTitle = item.select("title").get(0).text();
						String description = item.select("description").get(0).text();
						String[] descriptionAndPic = splitDescriptionAndPictureURL(description);
						String picURL = descriptionAndPic[0];
						description = descriptionAndPic[1];
						articleFromDB.add(new Article(null, param.getId(), itemTitle, picURL, description));
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
			updateArticles(articleFromDB);
			return null;
		}

		private String[] splitDescriptionAndPictureURL(String description) {
			String[] result = description.split("</a>");
			Document xmlDoc = Jsoup.parse(description);
			Elements imgSrc = xmlDoc.select("img");
			result[0] = imgSrc.get(0).attr("src");
			return result;
		}

		private void updateArticles(ArrayList<Article> articleFromDB) {
			DBHelper dbHelper = DBHelper.getInstance(getApplicationContext());
			boolean articleChanged = false;
			int maxArticleLength = articleFromDB.size() > articles.size() ? articleFromDB.size() : articles.size();
			for (int i = 1; i < maxArticleLength + 1; i++) {
				if (i <= articleFromDB.size() && i > articles.size()) {
					articleChanged = true;
					dbHelper.insertArticle(articleFromDB.get(i - 1));
				} else if (i > articleFromDB.size() && i <= articles.size()) {
					articleChanged = true;
					dbHelper.deleteArticle(articles.get(i - 1));
				} else {
					if (isSameArticle(articleFromDB.get(i - 1), articles.get(i - 1))) {
						articleChanged = true;
						dbHelper.updateArticle(articleFromDB.get(i - 1));
					}
				}
			}

			if (articleChanged) {
				articles = articleFromDB;
				Intent intent = new Intent("UPDATE_DATA");
				intent.putExtra("update", true);
				sendBroadcast(intent);
			}
		}

		private boolean isSameArticle(Article article1, Article article2) {
			if (article2.getId() != article1.getId()) {
				return false;
			}
			if (article1.getChannelId() != article2.getChannelId()) {
				return false;
			}
			if (article1.getTitle().equals(article2.getTitle())) {
				return false;
			}
			if (article1.getPicURL().equals(article2.getPicURL())) {
				return false;
			}
			if (article1.getDescription().equals(article2.getDescription())) {
				return false;
			}
			return true;

		}
	}
}
