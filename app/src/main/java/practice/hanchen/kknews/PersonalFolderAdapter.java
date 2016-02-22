package practice.hanchen.kknews;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

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
					bundle.putLong("id", personalFolderList.get(position).getId());
					bundle.putString("title", personalFolderList.get(position).getFolderName());
					intent.putExtras(bundle);
					mContext.startActivity(intent);
				}
			}
		});

		holder.getView().setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				Long folderId = personalFolderList.get(position).getId();
				String folderName = personalFolderList.get(position).getFolderName();
				final FolderAlertDialog folderAlertDialog = new FolderAlertDialog(mContext, "編輯資料夾", folderId, folderName);
				folderAlertDialog.setLabelEditTextTitle("修改名稱");
				folderAlertDialog.setLabelGridViewTitle("修改封面");
				folderAlertDialog.setPositiveButton("修改", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						RecyclerView listViewPersonalFolder = (RecyclerView) ((Dialog) dialog).findViewById(R.id.listview_folder_cover);
						String coverURL = ((FolderPictureAdapter) listViewPersonalFolder.getAdapter()).getCoverURL();
						DBHelper dbHelper = DBHelper.getInstance(mContext);

						if (!coverURL.isEmpty()) {
							personalFolderList.get(position).setDefaultPicUrl(coverURL);
							dbHelper.updatePersonalFolder(personalFolderList.get(position));
						}

						if (!personalFolderList.get(position).getFolderName().equals(folderAlertDialog.getTextFormEditText())) {
							personalFolderList.get(position).setFolderName(folderAlertDialog.getTextFormEditText());
							dbHelper.updatePersonalFolder(personalFolderList.get(position));
						}
						notifyDataSetChanged();
					}
				});
				folderAlertDialog.show();
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
				List<PersonalList> personalLists = dbHelper.getPersonalListByFolderId(personalFolderList.get(i).getId());
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
