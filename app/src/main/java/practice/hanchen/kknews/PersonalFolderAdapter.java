package practice.hanchen.kknews;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.List;

/**
 * Created by HanChen on 2016/2/5.
 */
public class PersonalFolderAdapter extends FolderAdapter {

	public PersonalFolderAdapter(Context context, List<PersonalFolder> personalFolders) {
		super(context, personalFolders);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		convertView = super.getView(position, convertView, parent);
		convertView.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				LayoutInflater layoutInflater = LayoutInflater.from(v.getContext());
				View layoutPersonalFolderDialog = layoutInflater.inflate(R.layout.layout_personal_folder_dialog, null);

				final AlertDialog.Builder personalFolderDialog = new AlertDialog.Builder(v.getContext());
				personalFolderDialog.setTitle("編輯資料夾");
				personalFolderDialog.setView(layoutPersonalFolderDialog);

				DBHelper dbHelper = DBHelper.getInstance(v.getContext());
				RecyclerView listviewPersonalFolder = (RecyclerView) layoutPersonalFolderDialog.findViewById(R.id.listview_folder_cover);
				GridLayoutManager gridLayoutManager = new GridLayoutManager(v.getContext(), 2);
				listviewPersonalFolder.setLayoutManager(gridLayoutManager);
				List<PersonalList> personalLists = dbHelper.getPersonalListByFolderName(personalFolders.get(position).getFolderName());
				FolderPictureAdapter folderPictureAdapter = new FolderPictureAdapter(mContext, personalLists);
				listviewPersonalFolder.setAdapter(folderPictureAdapter);

				final EditText textFolderName = (EditText) layoutPersonalFolderDialog.findViewById(R.id.text_folder_name);
				textFolderName.setText(personalFolders.get(position).getFolderName());

				personalFolderDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
				personalFolderDialog.setPositiveButton("修改", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						RecyclerView listviewPersonalFolder = (RecyclerView) ((Dialog) dialog).findViewById(R.id.listview_folder_cover);
						String coverURL = ((FolderPictureAdapter) listviewPersonalFolder.getAdapter()).getCoverURL();
						DBHelper dbHelper = DBHelper.getInstance(mContext);

						if (!coverURL.isEmpty()) {
							personalFolders.get(position).setDefaultPicUrl(coverURL);
							dbHelper.updatePersonalFolder(personalFolders.get(position));
						}

						if (!personalFolders.get(position).getFolderName().equals(textFolderName.getText().toString())) {
							personalFolders.get(position).setFolderName(textFolderName.getText().toString());
							dbHelper.updatePersonalFolder(personalFolders.get(position));
						}
						notifyDataSetChanged();
					}
				});
				personalFolderDialog.show();
				return true;
			}
		});

		convertView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (selectedMode) {
					if (isSelected.get(position)) {
						isSelected.set(position, false);
						Log.d("han", position + " = " + isSelected.get(position));
					} else {
						isSelected.set(position, true);
						Log.d("han", position + " = " + isSelected.get(position));
					}
					notifyDataSetChanged();
				} else {
					Intent intent = new Intent(mContext, PersonalArticleActivity.class);
					Bundle bundle = new Bundle();
					bundle.putInt("id", personalFolders.get(position).getId().intValue());
					bundle.putString("title", personalFolders.get(position).getFolderName());
					intent.putExtras(bundle);
					mContext.startActivity(intent);
				}
			}
		});
		return convertView;
	}

	public void deleteFolder() {
		DBHelper dbHelper = DBHelper.getInstance(mContext);
		Log.d("han", "isSelected = " + isSelected.size());
		int i = 0;
		while( i < isSelected.size() ) {
			if(isSelected.get(i)) {
				List<PersonalList> personalLists = dbHelper.getPersonalListByFolderId(personalFolders.get(i).getId().intValue());
				dbHelper.deletePersonalList(personalLists);
				dbHelper.deletePersonalFolder(personalFolders.get(i));
				personalFolders.remove(i);
				isSelected.remove(i);
			} else {
				i++;
			}
		}
		resetSelection();
	}
}
