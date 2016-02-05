package practice.hanchen.kknews;

import android.content.Context;

import de.greenrobot.dao.async.AsyncSession;

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
}
