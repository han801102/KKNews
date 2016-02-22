package practice.hanchen.kknews.utils;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.async.AsyncSession;
import de.greenrobot.dao.query.QueryBuilder;
import practice.hanchen.kknews.dao.Article;
import practice.hanchen.kknews.dao.ArticleDao;
import practice.hanchen.kknews.dao.Channel;
import practice.hanchen.kknews.dao.DaoMaster;
import practice.hanchen.kknews.dao.DaoSession;
import practice.hanchen.kknews.dao.PersonalFolder;
import practice.hanchen.kknews.dao.PersonalFolderDao;
import practice.hanchen.kknews.dao.PersonalList;
import practice.hanchen.kknews.dao.PersonalListDao;

/**
 * Created by HanChen on 2016/2/4.
 */
public class DBHelper {
	private static DBHelper INSTANCE = null;

	public static DBHelper getInstance(Context context) {
		if (INSTANCE == null)
			INSTANCE = new DBHelper(context);
		return INSTANCE;
	}

	private static final String DB_NAME = "kknews.db";
	private DaoSession daoSession;
	private AsyncSession asyncSession;

	private DBHelper(Context context) {
		DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, DB_NAME, null);

		DaoMaster daoMaster = new DaoMaster(helper.getWritableDatabase());

		daoSession = daoMaster.newSession();
		asyncSession = daoSession.startAsyncSession();
	}

	public void insertArticle(Article article) {
		asyncSession.insert(article).waitForCompletion();
	}

	public void updateArticle(Article article) {
		asyncSession.update(article);
	}

	public void deleteArticle(Article article) {
		asyncSession.delete(article);
	}

	public List<Article> getArticleAll() {
		return daoSession.getArticleDao().loadAll();
	}

	public List<Article> getArticleByChannelId(Long id) {
		QueryBuilder<Article> queryBuilder = daoSession.getArticleDao().queryBuilder();
		queryBuilder.where(ArticleDao.Properties.ChannelId.eq(id));
		return queryBuilder.list();
	}

	public Article getArticleById(Long id) {
		QueryBuilder<Article> queryBuilder = daoSession.getArticleDao().queryBuilder();
		queryBuilder.where(ArticleDao.Properties.Id.eq(id));
		return queryBuilder.unique();
	}

	public List<Article> getPersonalArticle(List<PersonalList> personalList) {
		List<Article> articles = new ArrayList<>();
		for (PersonalList item : personalList) {
			articles.add(getArticleById(item.getArticleId()));
		}
		return articles;
	}

	public List<PersonalFolder> getPersonalFolderAll() {
		return daoSession.getPersonalFolderDao().loadAll();
	}

	public PersonalFolder getFolderByName(String folderName) {
		QueryBuilder<PersonalFolder> queryBuilder = daoSession.getPersonalFolderDao().queryBuilder();
		queryBuilder.where(PersonalFolderDao.Properties.FolderName.eq(folderName));
		return queryBuilder.unique();
	}

	public int getChannelsCount() {
		return daoSession.getChannelDao().loadAll().size();
	}

	public List<Channel> getChannels() {
		return daoSession.getChannelDao().loadAll();
	}

	public void insertChannel(Channel channel) {
		asyncSession.insert(channel).waitForCompletion();
	}

	public boolean isFolderInDB(String folderName) {
		return (getFolderByName(folderName) != null);
	}

	public void insertPersonalFolder(PersonalFolder personalFolder) {
		asyncSession.insert(personalFolder).waitForCompletion();
	}

	public List<PersonalList> getPersonalListByFolderId(Long id) {
		QueryBuilder<PersonalList> queryBuilder = daoSession.getPersonalListDao().queryBuilder();
		queryBuilder.where(PersonalListDao.Properties.FolderId.eq(id));
		return queryBuilder.list();
	}

	public List<PersonalList> getPersonalListByFolderName(String folderName) {
		Long folderId = getFolderByName(folderName).getId();
		return getPersonalListByFolderId(folderId);
	}

	public void insertPersonalList(PersonalList personalList) {
		if (!isPersonalListInDB(personalList)) {
			asyncSession.insert(personalList).waitForCompletion();
		}
	}

	public boolean isPersonalListInDB(PersonalList personalList) {
		QueryBuilder<PersonalList> queryBuilder = daoSession.getPersonalListDao().queryBuilder();
		int personalListCount = queryBuilder.where(PersonalListDao.Properties.ArticleId.eq(personalList.getArticleId()))
				.where(PersonalListDao.Properties.FolderId.eq(personalList.getFolderId())).list().size();
		return (personalListCount > 0);
	}

	public void updatePersonalFolder(PersonalFolder personalFolder) {
		asyncSession.update(personalFolder).waitForCompletion();
	}

	public void deletePersonalList(List<PersonalList> personalLists) {
		for (int i = 0; i < personalLists.size(); i++) {
			asyncSession.delete(personalLists.get(i));
		}
	}

	public void deletePersonalFolder(PersonalFolder personalFolder) {
		asyncSession.delete(personalFolder);
	}

	public static class RSSCrawlerService extends Service {
		private List<Channel> channels;
		private List<Article> articles;

		@Override
		public void onCreate() {
			DBHelper dbHelper = getInstance(getApplicationContext());
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
				DBHelper dbHelper = getInstance(getApplicationContext());
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
				return !article1.getDescription().equals(article2.getDescription());

			}
		}
	}
}
