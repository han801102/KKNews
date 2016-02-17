package practice.hanchen.kknews;

import android.content.Context;

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

	public PersonalFolderDao getPersonalFolderDao() {
		return daoSession.getPersonalFolderDao();
	}

	public PersonalListDao getPersonalListDao() {
		return daoSession.getPersonalListDao();
	}

	public AsyncSession getAsyncSession() {
		return asyncSession;
	}

	public List<PersonalFolder> getPersonalFolderAll() {
		return daoSession.getPersonalFolderDao().loadAll();
	}

	public PersonalFolder getFolderByName(String folderName) {
		QueryBuilder<PersonalFolder> queryBuilder = daoSession.getPersonalFolderDao().queryBuilder();
		queryBuilder.where(PersonalFolderDao.Properties.FolderName.eq(folderName));
		return queryBuilder.unique();
	}

	public boolean isFolderInDB(String folderName) {
		return (getFolderByName(folderName) != null);
	}

	public void insertPersonalFolder(PersonalFolder personalFolder) {
		asyncSession.insert(personalFolder).waitForCompletion();
	}

	public List<PersonalList> getPersonalListByFolderId(int id) {
		QueryBuilder<PersonalList> queryBuilder = daoSession.getPersonalListDao().queryBuilder();
		queryBuilder.where(PersonalListDao.Properties.FolderId.eq(id));
		return queryBuilder.list();
	}

	public List<PersonalList> getPersonalListByFolderName(String folderName) {
		int folderId = getFolderByName(folderName).getId().intValue();
		return getPersonalListByFolderId(folderId);
	}

	public void insertPersonalList(PersonalList personalList) {
		if(!isPersonalListInDB(personalList)) {
			asyncSession.insert(personalList).waitForCompletion();
		}
	}

	public boolean isPersonalListInDB(PersonalList personalList) {
		QueryBuilder<PersonalList> queryBuilder = daoSession.getPersonalListDao().queryBuilder();
		int personalListCount = queryBuilder.where(PersonalListDao.Properties.Title.eq(personalList.getTitle()))
				.where(PersonalListDao.Properties.FolderId.eq(personalList.getFolderId())).list().size();
		return (personalListCount > 0);
	}

	public void updatePersonalFolder(PersonalFolder personalFolder) {
		asyncSession.update(personalFolder).waitForCompletion();
	}

	public void deletePersonalList(List<PersonalList> personalLists) {
		for(int i = 0; i < personalLists.size(); i++) {
			asyncSession.delete(personalLists.get(i));
		}
	}

	public void deletePersonalFolder(PersonalFolder personalFolder) {
		asyncSession.delete(personalFolder);
	}
}
