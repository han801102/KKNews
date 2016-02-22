package practice.hanchen.kknews.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import practice.hanchen.kknews.utils.DBHelper;
import practice.hanchen.kknews.dialog.FolderAlertDialog;
import practice.hanchen.kknews.R;
import practice.hanchen.kknews.dao.Article;
import practice.hanchen.kknews.dao.PersonalFolder;
import practice.hanchen.kknews.dao.PersonalList;

/**
 * Created by HanChen on 2016/2/5.
 */
public class ChannelArticleAdapter extends ArticleAdapter {
	private boolean selectedMode;
	private ArrayList<Boolean> selectedItem;

	public ChannelArticleAdapter(Context context, List<Article> articles) {
		super(context, articles);
		setArticleData(articles);
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, final int position) {
		super.onBindViewHolder(holder, position);
		holder.getView().setSelected(selectedItem.get(position));
		holder.getView().setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				TextView labelChannelTitle = (TextView) v.getRootView().findViewById(R.id.label_page_title);
				String folderName = labelChannelTitle.getText().toString();
				final FolderAlertDialog folderAlertDialog = new FolderAlertDialog(v.getContext(), "加入個人精選", (long)0, folderName);
				folderAlertDialog.setLabelEditTextTitle("加入個人精選");
				folderAlertDialog.setLabelGridViewTitle("請選擇要加入的資料夾");
				folderAlertDialog.setPositiveButton("加入", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						DBHelper dbHelper = DBHelper.getInstance(mContext);
						String selectedFolderName = folderAlertDialog.getSelectedFolderName();
						if (selectedFolderName.isEmpty()) {
							EditText textFolderName = (EditText) ((Dialog) dialog).findViewById(R.id.text_folder_name);
							selectedFolderName = textFolderName.getText().toString();
						}

						if (!dbHelper.isFolderInDB(selectedFolderName)) {
							dbHelper.insertPersonalFolder(new PersonalFolder(null, selectedFolderName, articleList.get(position).getPicURL()));
						}
						Long folderId = dbHelper.getFolderByName(selectedFolderName).getId();
						dbHelper.insertPersonalList(new PersonalList(null, folderId, articleList.get(position).getId()));
					}
				});
				folderAlertDialog.show();
				return true;
			}
		});
		holder.getView().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (selectedMode) {
					if (selectedItem.get(position)) {
						selectedItem.set(position, false);
					} else {
						selectedItem.set(position, true);
					}
					notifyDataSetChanged();
				} else {
					if (holder.getLableAlbumDescription().getVisibility() == View.GONE) {
						holder.getLableAlbumDescription().setVisibility(View.VISIBLE);
					} else {
						holder.getLableAlbumDescription().setVisibility(View.GONE);
					}
				}
			}
		});
	}

	public void resetSelection() {
		selectedItem = new ArrayList<>();
		for (int i = 0; i < articleList.size(); i++) {
			selectedItem.add(false);
		}
		selectedMode = false;
	}

	public void changeSelectedMode() {
		selectedMode = !selectedMode;
	}

	public boolean getSelectedMode() {
		return selectedMode;
	}

	public String getFirstSelectedItemPicURL() {
		for (int i = 0; i < selectedItem.size(); i++) {
			if (selectedItem.get(i)) {
				return articleList.get(i).getPicURL();
			}
		}
		return Integer.toString(R.drawable.loading);
	}

	public void insertSelectedItem(Long folderId) {
		DBHelper dbHelper = DBHelper.getInstance(mContext);
		for (int i = 0; i < selectedItem.size(); i++) {
			if (selectedItem.get(i)) {
				PersonalList tempPersonalList = new PersonalList();
				tempPersonalList.setId(null);
				tempPersonalList.setFolderId(folderId);
				tempPersonalList.setArticleId(articleList.get(i).getId());
				dbHelper.insertPersonalList(tempPersonalList);
			}
		}
		resetSelection();
		notifyDataSetChanged();
	}

	public boolean hasSelectedItem() {
		for (int i = 0; i < selectedItem.size(); i++) {
			if (selectedItem.get(i)) {
				return true;
			}
		}
		return false;
	}

	public void resetArticleData(List<Article> newArticles) {
		setArticleData(newArticles);
		notifyDataSetChanged();
	}

	private void setArticleData(List<Article> newArticles) {
		this.selectedMode = false;
		articleList = newArticles;
		selectedItem = new ArrayList<>();
		for (int i = 0; i < articleList.size(); i++) {
			selectedItem.add(false);
		}
	}
}
