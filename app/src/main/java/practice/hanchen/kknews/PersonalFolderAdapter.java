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

import de.greenrobot.dao.async.AsyncSession;
import de.greenrobot.dao.query.QueryBuilder;

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
				final View layoutPersonalFolderDialog = layoutInflater.inflate(R.layout.layout_personal_folder_dialog, null);

				AlertDialog.Builder personalFolderDialog = new AlertDialog.Builder(v.getContext());
				personalFolderDialog.setTitle("編輯資料夾");
				personalFolderDialog.setView(layoutPersonalFolderDialog);

				RecyclerView listviewPersonalFolder = (RecyclerView) layoutPersonalFolderDialog.findViewById(R.id.listview_folder_cover);
				GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2);
				listviewPersonalFolder.setLayoutManager(gridLayoutManager);
				final String folderName = personalFolders.get(position).getFolderName();
				final List<PersonalList> personalLists = getPersonalList(getFolderIdFromDB(folderName));
				FolderPictureAdapter folderPictureAdapter = new FolderPictureAdapter(mContext, personalLists);
				listviewPersonalFolder.setAdapter(folderPictureAdapter);

				final EditText textFolderName = (EditText) layoutPersonalFolderDialog.findViewById(R.id.text_folder_name);
				textFolderName.setText(folderName);

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
						AsyncSession asyncSession = dbHelper.getAsyncSession();
						Integer folderId = getFolderIdFromDB(folderName);

						if (!coverURL.isEmpty()) {
							asyncSession.update(new PersonalFolder(folderId.longValue(), folderName, coverURL));
							personalFolders.get(position).setDefaultPicUrl(coverURL);
						} else {
							coverURL = personalFolders.get(position).getDefaultPicUrl();
						}

						if (!folderName.equals(textFolderName.getText().toString())) {
							asyncSession.update(new PersonalFolder(folderId.longValue(), textFolderName.getText().toString(), coverURL));
							personalFolders.get(position).setFolderName(textFolderName.getText().toString());
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
						numTotalSelected--;
					} else {
						isSelected.set(position, true);
						numTotalSelected++;
					}
					for (int i = 0; i < isSelected.size(); i++) {
						Log.d("han", i + ": " + isSelected.get(i).toString());
					}
					Log.d("han", "total = " + numTotalSelected);
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

	private List<PersonalList> getPersonalList(int id) {
		QueryBuilder<PersonalList> queryBuilder = DBHelper.getInstance(mContext).getPersonalListDao().queryBuilder();
		queryBuilder.where(PersonalListDao.Properties.FolderId.eq(id));
		return queryBuilder.list();
	}

	private int getFolderIdFromDB(String folderName) {
		QueryBuilder<PersonalFolder> queryBuilder = DBHelper.getInstance(mContext).getPersonalFolderDao().queryBuilder();
		queryBuilder.where(PersonalFolderDao.Properties.FolderName.eq(folderName));
		return queryBuilder.list().get(0).getId().intValue();
	}

	public void deleteFolder() {
		DBHelper dbHelper = DBHelper.getInstance(mContext);
		AsyncSession asyncSession = dbHelper.getAsyncSession();
		for(int i = 0; i < isSelected.size(); i++) {
			if(isSelected.get(i)) {
				List<PersonalList> personalLists = getPersonalList(personalFolders.get(i).getId().intValue());
				for(int j = 0; j < personalLists.size(); j++) {
					//dbHelper.getPersonalListDao().delete(personalLists.get(j));
					asyncSession.delete(personalLists.get(j));
				}
				asyncSession.delete(personalFolders.get(i)).waitForCompletion();
				personalFolders.remove(i);
				isSelected.remove(i);
			}
		}
		resetSelection();
	}
}
