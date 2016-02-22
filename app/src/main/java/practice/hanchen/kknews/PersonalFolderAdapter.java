package practice.hanchen.kknews;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HanChen on 2016/2/19.
 */
public class PersonalFolderAdapter extends FolderAdapter {
	protected boolean selectedMode;
	protected ArrayList<Boolean> isSelected;

	public PersonalFolderAdapter(Context context, List<PersonalFolder> personalFolders) {
		super(context, personalFolders);
		this.selectedMode = false;
		isSelected = new ArrayList<>();
		for (int i = 0; i < personalFolders.size(); i++) {
			this.isSelected.add(false);
		}
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, final int position) {
		super.onBindViewHolder(holder, position);
		holder.getView().setSelected(isSelected.get(position));
		holder.getView().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (selectedMode) {
					if (isSelected.get(position)) {
						isSelected.set(position, false);
					} else {
						isSelected.set(position, true);
					}
					notifyDataSetChanged();
				} else {
					Intent intent = new Intent(mContext, PersonalArticleActivity.class);
					Bundle bundle = new Bundle();
					bundle.putInt("id", personalFolderList.get(position).getId().intValue());
					bundle.putString("title", personalFolderList.get(position).getFolderName());
					intent.putExtras(bundle);
					mContext.startActivity(intent);
				}
			}
		});

		holder.getView().setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				LayoutInflater layoutInflater = LayoutInflater.from(v.getContext());
				View layoutPersonalFolderDialog = layoutInflater.inflate(R.layout.layout_personal_folder_dialog, null);

				final AlertDialog.Builder personalFolderDialog = new AlertDialog.Builder(v.getContext());
				personalFolderDialog.setTitle("編輯資料夾");
				personalFolderDialog.setView(layoutPersonalFolderDialog);

				DBHelper dbHelper = DBHelper.getInstance(v.getContext());
				RecyclerView listViewPersonalFolder = (RecyclerView) layoutPersonalFolderDialog.findViewById(R.id.listview_folder_cover);
				GridLayoutManager gridLayoutManager = new GridLayoutManager(v.getContext(), 2);
				listViewPersonalFolder.setLayoutManager(gridLayoutManager);
				List<PersonalList> personalLists = dbHelper.getPersonalListByFolderName(personalFolderList.get(position).getFolderName());
				FolderPictureAdapter folderPictureAdapter = new FolderPictureAdapter(mContext, dbHelper.getPersonalArticle(personalLists));
				listViewPersonalFolder.setAdapter(folderPictureAdapter);

				final EditText textFolderName = (EditText) layoutPersonalFolderDialog.findViewById(R.id.text_folder_name);
				textFolderName.setText(personalFolderList.get(position).getFolderName());

				personalFolderDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
				personalFolderDialog.setPositiveButton("修改", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						RecyclerView listViewPersonalFolder = (RecyclerView) ((Dialog) dialog).findViewById(R.id.listview_folder_cover);
						String coverURL = ((FolderPictureAdapter) listViewPersonalFolder.getAdapter()).getCoverURL();
						DBHelper dbHelper = DBHelper.getInstance(mContext);

						if (!coverURL.isEmpty()) {
							personalFolderList.get(position).setDefaultPicUrl(coverURL);
							dbHelper.updatePersonalFolder(personalFolderList.get(position));
						}

						if (!personalFolderList.get(position).getFolderName().equals(textFolderName.getText().toString())) {
							personalFolderList.get(position).setFolderName(textFolderName.getText().toString());
							dbHelper.updatePersonalFolder(personalFolderList.get(position));
						}
						notifyDataSetChanged();
					}
				});
				personalFolderDialog.show();
				return true;
			}
		});
	}

	public void changeSelectedMode() {
		selectedMode = !selectedMode;
	}

	public boolean getSelectedMode() {
		return selectedMode;
	}

	public void deleteFolder() {
		DBHelper dbHelper = DBHelper.getInstance(mContext);
		int i = 0;
		while (i < isSelected.size()) {
			if (isSelected.get(i)) {
				List<PersonalList> personalLists = dbHelper.getPersonalListByFolderId(personalFolderList.get(i).getId().intValue());
				dbHelper.deletePersonalList(personalLists);
				dbHelper.deletePersonalFolder(personalFolderList.get(i));
				personalFolderList.remove(i);
				isSelected.remove(i);
			} else {
				i++;
			}
		}
		resetSelection();
	}

	public void resetSelection() {
		isSelected = new ArrayList<>();
		for (int i = 0; i < personalFolderList.size(); i++) {
			isSelected.add(false);
		}
		selectedMode = false;
	}

}
