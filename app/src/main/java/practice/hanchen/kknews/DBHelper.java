package practice.hanchen.kknews;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.async.AsyncSession;
import de.greenrobot.dao.query.QueryBuilder;

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
}
