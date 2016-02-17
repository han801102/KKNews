package practice.hanchen.kknews;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class ChannelArticleActivity extends ArticleActivity {
	private RecyclerView listviewChannelData;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle extras = getIntent().getExtras();
		String url = extras.getString("url");

		new GetResponseTack().execute(url);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		new MenuInflater(this).inflate(R.menu.menu_channel, menu);
		if (listviewChannelData != null && ((ChannelArticleAdapter) listviewChannelData.getAdapter()).getSelectedMode()) {
			menu.findItem(R.id.action_accept).setVisible(true);
			menu.findItem(R.id.action_cancel).setVisible(true);
			menu.findItem(R.id.action_accept).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
			menu.findItem(R.id.action_cancel).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		} else {
			menu.findItem(R.id.action_accept).setVisible(false);
			menu.findItem(R.id.action_cancel).setVisible(false);
		}
		return (super.onCreateOptionsMenu(menu));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		ChannelArticleAdapter channelArticleAdapter = (ChannelArticleAdapter)listviewChannelData.getAdapter();
		switch (item.getItemId()) {
			case R.id.action_add:
				channelArticleAdapter.changeSelectedMode();
				invalidateOptionsMenu();
				return true;
			case R.id.action_accept:
				if( channelArticleAdapter.hasSelectedItem() ) {
					showDialog();
				}
				invalidateOptionsMenu();
				return true;
			case R.id.action_cancel:
				channelArticleAdapter.resetSelection();
				listviewChannelData.getAdapter().notifyDataSetChanged();
				invalidateOptionsMenu();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private void showDialog() {
		DBHelper dbHelper = DBHelper.getInstance(getApplicationContext());
		LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
		final View layoutPersonalFolderDialog = layoutInflater.inflate(R.layout.layout_personal_folder_dialog, null);

		AlertDialog.Builder personalFolderDialog = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AppTheme_NoActionBar));
		personalFolderDialog.setTitle("加入個人精選");
		personalFolderDialog.setView(layoutPersonalFolderDialog);

		GridView listviewPersonalFolder = (GridView) layoutPersonalFolderDialog.findViewById(R.id.listview_personal_folder);
		DialogFolderAdapter folderAdapter = new DialogFolderAdapter(getApplicationContext(), dbHelper.getPersonalFolderAll());
		listviewPersonalFolder.setAdapter(folderAdapter);

		EditText textFolderName = (EditText) layoutPersonalFolderDialog.findViewById(R.id.text_folder_name);
		textFolderName.setTextColor(Color.BLACK);
		TextView lableChannelTitle = (TextView)findViewById(R.id.label_page_title);
		textFolderName.setText(lableChannelTitle.getText());

		personalFolderDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		personalFolderDialog.setPositiveButton("加入", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				DBHelper dbHelper = DBHelper.getInstance(getApplicationContext());
				GridView listviewPersonalFolder = (GridView) ((Dialog) dialog).findViewById(R.id.listview_personal_folder);
				String folderName = ((DialogFolderAdapter) listviewPersonalFolder.getAdapter()).getSelectedFolder();
				if (folderName.isEmpty()) {
					EditText textFolderName = (EditText) ((Dialog) dialog).findViewById(R.id.text_folder_name);
					folderName = textFolderName.getText().toString();
				}

				if (!dbHelper.isFolderInDB(folderName)) {
					String firstSelectedItemPicURL = ((ChannelArticleAdapter)listviewChannelData.getAdapter()).getFirstSelectedItemPicURL();
					dbHelper.insertPersonalFolder(new PersonalFolder(null, folderName, firstSelectedItemPicURL));
				}
				int folderId = dbHelper.getFolderByName(folderName).getId().intValue();
				((ChannelArticleAdapter)listviewChannelData.getAdapter()).insertSelectedItem(folderId);
			}
		});
		personalFolderDialog.show();

	}

	private class GetResponseTack extends AsyncTask<String, Void, ArrayList<PersonalList>> {

		@Override
		protected ArrayList<PersonalList> doInBackground(String... params) {
			return getResponse(params[0]);
		}

		@Override
		protected void onPostExecute(ArrayList<PersonalList> personalList) {
			listviewChannelData = (RecyclerView) findViewById(R.id.listview_article_data);
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
