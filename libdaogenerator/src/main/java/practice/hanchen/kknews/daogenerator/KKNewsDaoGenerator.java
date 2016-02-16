package practice.hanchen.kknews.daogenerator;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class KKNewsDaoGenerator {
	public static void main(String[] args) throws Exception {
		Schema schema = new Schema(1, "practice.hanchen.kknews");
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
		personalList.addIntProperty("folderId");
		personalList.addStringProperty("title").notNull();
		personalList.addStringProperty("picURL").notNull();
		personalList.addStringProperty("description").notNull();
	}
}
