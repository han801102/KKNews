package practice.hanchen.kknews;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

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
		if (selectedItem.get(position)) {
			holder.getView().setBackgroundColor(Color.BLUE);
		} else {
			holder.getView().setBackgroundColor(Color.WHITE);
		}
		holder.getView().setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				DBHelper dbHelper = DBHelper.getInstance(v.getContext());
				LayoutInflater layoutInflater = LayoutInflater.from(v.getContext());
				final View layoutPersonalFolderDialog = layoutInflater.inflate(R.layout.layout_personal_folder_dialog, null);

				AlertDialog.Builder personalFolderDialog = new AlertDialog.Builder(v.getContext());
				personalFolderDialog.setTitle("加入個人精選");
				personalFolderDialog.setView(layoutPersonalFolderDialog);

				GridView listViewPersonalFolder = (GridView) layoutPersonalFolderDialog.findViewById(R.id.listview_personal_folder);
				DialogFolderAdapter folderAdapter = new DialogFolderAdapter(v.getContext(), dbHelper.getPersonalFolderAll());
				listViewPersonalFolder.setAdapter(folderAdapter);

				EditText textFolderName = (EditText) layoutPersonalFolderDialog.findViewById(R.id.text_folder_name);
				TextView labelChannelTitle = (TextView) v.getRootView().findViewById(R.id.label_page_title);
				textFolderName.setText(labelChannelTitle.getText());

				personalFolderDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
				personalFolderDialog.setPositiveButton("加入", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						DBHelper dbHelper = DBHelper.getInstance(mContext);
						GridView listViewPersonalFolder = (GridView) ((Dialog) dialog).findViewById(R.id.listview_personal_folder);
						String folderName = ((DialogFolderAdapter) listViewPersonalFolder.getAdapter()).getSelectedFolder();
						if (folderName.isEmpty()) {
							EditText textFolderName = (EditText) ((Dialog) dialog).findViewById(R.id.text_folder_name);
							folderName = textFolderName.getText().toString();
						}

						if (!dbHelper.isFolderInDB(folderName)) {
							dbHelper.insertPersonalFolder(new PersonalFolder(null, folderName, articleList.get(position).getPicURL()));
						}
						Long folderId = dbHelper.getFolderByName(folderName).getId();
						dbHelper.insertPersonalList(new PersonalList(null, folderId, articleList.get(position).getId()));
					}
				});
				personalFolderDialog.show();
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

	public void insertSelectedItem(int folderId) {
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

	public void setArticleData(List<Article> newArticles) {
		this.selectedMode = false;
		articleList = newArticles;
		selectedItem = new ArrayList<>();
		for (int i = 0; i < articleList.size(); i++) {
			selectedItem.add(false);
		}
	}
}
