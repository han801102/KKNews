package practice.hanchen.kknews.daogenerator;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class KKNewsDaoGenerator {
	public static void main(String[] args) throws Exception {
		Schema schema = new Schema(1, "practice.hanchen.kknews.dao");
		addEntity(schema);
		new DaoGenerator().generateAll(schema, "../KKNews/app/src/main/java");
	}

	private static void addEntity(Schema schema) {
		Entity personalFolder = schema.addEntity("PersonalFolder");
		personalFolder.addIdProperty().primaryKey().autoincrement();
		personalFolder.addStringProperty("folderName").notNull();
		personalFolder.addStringProperty("defaultPicUrl").notNull();

		Entity personalList = schema.addEntity("PersonalList");
		personalList.addIdProperty().primaryKey().autoincrement();
		personalList.addLongProperty("folderId").notNull();
		personalList.addLongProperty("articleId").notNull();

		Entity channels = schema.addEntity("Channel");
		channels.addIdProperty().primaryKey().autoincrement();
		channels.addStringProperty("title").notNull();
		channels.addStringProperty("URL").notNull();

		Entity article = schema.addEntity("Article");
		article.addIdProperty().primaryKey().autoincrement();
		article.addLongProperty("channelId").notNull();
		article.addStringProperty("title").notNull();
		article.addStringProperty("picURL").notNull();
		article.addStringProperty("description").notNull();
	}
}
