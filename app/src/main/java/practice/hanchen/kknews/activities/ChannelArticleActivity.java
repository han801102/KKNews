package practice.hanchen.kknews.activities;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import practice.hanchen.kknews.adapters.ChannelArticleAdapter;
import practice.hanchen.kknews.dao.Article;
import practice.hanchen.kknews.dao.PersonalFolder;
import practice.hanchen.kknews.helpers.DBHelper;
import practice.hanchen.kknews.adapters.DialogFolderAdapter;
import practice.hanchen.kknews.dialogs.FolderAlertDialog;
import practice.hanchen.kknews.R;

public class ChannelArticleActivity extends ArticleActivity {
	private RecyclerView listViewChannelData;
	private long id;

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(broadcastReceiver);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle extras = getIntent().getExtras();
		id = extras.getLong("id");

		listViewChannelData = (RecyclerView) findViewById(R.id.listview_article_data);
		LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
		listViewChannelData.setLayoutManager(mLayoutManager);
		ChannelArticleAdapter channelArticleAdapter = new ChannelArticleAdapter(getApplicationContext(), getArticleByChannelId(id));
		listViewChannelData.setAdapter(channelArticleAdapter);

		IntentFilter intentFilter = new IntentFilter("UPDATE_DATA");
		registerReceiver(broadcastReceiver, intentFilter);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		new MenuInflater(this).inflate(R.menu.menu_channel, menu);
		if (listViewChannelData != null && ((ChannelArticleAdapter) listViewChannelData.getAdapter()).getSelectedMode()) {
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
		ChannelArticleAdapter channelArticleAdapter = (ChannelArticleAdapter) listViewChannelData.getAdapter();
		switch (item.getItemId()) {
			case R.id.action_add:
				channelArticleAdapter.changeSelectedMode();
				invalidateOptionsMenu();
				return true;
			case R.id.action_accept:
				if (channelArticleAdapter.hasSelectedItem()) {
					showDialog();
				}
				invalidateOptionsMenu();
				return true;
			case R.id.action_cancel:
				channelArticleAdapter.resetSelection();
				listViewChannelData.getAdapter().notifyDataSetChanged();
				invalidateOptionsMenu();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			switch (intent.getAction()) {
				case "UPDATE_DATA":
					((ChannelArticleAdapter) listViewChannelData.getAdapter()).resetArticleData(getArticleByChannelId(id));
					break;
			}
		}
	};

	private List<Article> getArticleByChannelId(Long id) {
		DBHelper dbHelper = DBHelper.getInstance(getApplicationContext());
		return dbHelper.getArticleByChannelId(id);
	}

	private void showDialog() {
		TextView labelChannelTitle = (TextView)findViewById(R.id.label_page_title);
		String folderName = labelChannelTitle.getText().toString();
		final FolderAlertDialog folderAlertDialog = new FolderAlertDialog(this, "加入個人精選", (long)0, folderName);
		folderAlertDialog.setLabelEditTextTitle("加入個人精選");
		folderAlertDialog.setLabelGridViewTitle("請選擇要加入的資料夾");
		folderAlertDialog.setPositiveButton("加入", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				DBHelper dbHelper = DBHelper.getInstance(getApplicationContext());
				RecyclerView listViewFolderCover = (RecyclerView) ((Dialog) dialog).findViewById(R.id.listview_folder_cover);
				String folderName = ((DialogFolderAdapter) listViewFolderCover.getAdapter()).getSelectedFolderName();
				if (folderName.isEmpty()) {
					EditText textFolderName = (EditText) ((Dialog) dialog).findViewById(R.id.text_folder_name);
					folderName = textFolderName.getText().toString();
				}

				if (!dbHelper.isFolderInDB(folderName)) {
					String firstSelectedItemPicURL =
							((ChannelArticleAdapter) listViewChannelData.getAdapter()).getFirstSelectedItemPicURL();
					dbHelper.insertPersonalFolder(new PersonalFolder(null, folderName, firstSelectedItemPicURL));
				}
				Long folderId = dbHelper.getFolderByName(folderName).getId();
				((ChannelArticleAdapter) listViewChannelData.getAdapter()).insertSelectedItem(folderId);
				invalidateOptionsMenu();
			}
		});
		folderAlertDialog.show();
	}
}
