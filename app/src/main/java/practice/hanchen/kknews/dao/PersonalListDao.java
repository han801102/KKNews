package practice.hanchen.kknews.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import practice.hanchen.kknews.dao.PersonalList;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "PERSONAL_LIST".
*/
public class PersonalListDao extends AbstractDao<PersonalList, Long> {

    public static final String TABLENAME = "PERSONAL_LIST";

    /**
     * Properties of entity PersonalList.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property FolderId = new Property(1, long.class, "folderId", false, "FOLDER_ID");
        public final static Property ArticleId = new Property(2, long.class, "articleId", false, "ARTICLE_ID");
    };


    public PersonalListDao(DaoConfig config) {
        super(config);
    }
    
    public PersonalListDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"PERSONAL_LIST\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"FOLDER_ID\" INTEGER NOT NULL ," + // 1: folderId
                "\"ARTICLE_ID\" INTEGER NOT NULL );"); // 2: articleId
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"PERSONAL_LIST\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, PersonalList entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getFolderId());
        stmt.bindLong(3, entity.getArticleId());
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public PersonalList readEntity(Cursor cursor, int offset) {
        PersonalList entity = new PersonalList( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getLong(offset + 1), // folderId
            cursor.getLong(offset + 2) // articleId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, PersonalList entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setFolderId(cursor.getLong(offset + 1));
        entity.setArticleId(cursor.getLong(offset + 2));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(PersonalList entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(PersonalList entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
