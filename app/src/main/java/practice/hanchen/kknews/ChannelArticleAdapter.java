package practice.hanchen.kknews;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
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
		holder.getView().setSelected(selectedItem.get(position));
		holder.getView().setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				DBHelper dbHelper = DBHelper.getInstance(v.getContext());
				LayoutInflater layoutInflater = LayoutInflater.from(v.getContext());
				final View layoutPersonalFolderDialog = layoutInflater.inflate(R.layout.layout_personal_folder_dialog, null);

				AlertDialog.Builder personalFolderDialog = new AlertDialog.Builder(v.getContext());
				personalFolderDialog.setTitle("加入個人精選");
				personalFolderDialog.setView(layoutPersonalFolderDialog);

				final RecyclerView listViewPersonalFolder = (RecyclerView) layoutPersonalFolderDialog.findViewById(R.id
						.listview_folder_cover);
				GridLayoutManager gridLayoutManager = new GridLayoutManager(v.getContext(), 3);
				listViewPersonalFolder.setLayoutManager(gridLayoutManager);
				DialogFolderAdapter recyclerDialogFolderAdapter = new DialogFolderAdapter(mContext, dbHelper.getPersonalFolderAll());
				listViewPersonalFolder.setAdapter(recyclerDialogFolderAdapter);

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
						String selectedFolderName = ((DialogFolderAdapter) listViewPersonalFolder.getAdapter()).getSelectedFolderName();
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

	public void setArticleData(List<Article> newArticles) {
		this.selectedMode = false;
		articleList = newArticles;
		selectedItem = new ArrayList<>();
		for (int i = 0; i < articleList.size(); i++) {
			selectedItem.add(false);
		}
	}
}
