package practice.hanchen.kknews;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import java.util.List;

import de.greenrobot.dao.async.AsyncSession;
import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by HanChen on 2016/2/5.
 */
public class ChannelArticleAdapter extends ArticleAdapter {

	public ChannelArticleAdapter(Context context, List<PersonalList> personalList) {
		super(context, personalList);
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, final int position) {
		super.onBindViewHolder(holder, position);
		holder.getView().setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				LayoutInflater layoutInflater = LayoutInflater.from(v.getContext());
				final View layoutPersonalFolderDialog = layoutInflater.inflate(R.layout.layout_personal_folder_dialog, null);

				AlertDialog.Builder personalFolderDialog = new AlertDialog.Builder(v.getContext());
				personalFolderDialog.setTitle("加入個人精選");
				personalFolderDialog.setView(layoutPersonalFolderDialog);

				GridView listviewPersonalFolder = (GridView) layoutPersonalFolderDialog.findViewById(R.id.listview_personal_folder);
				DialogFolderAdapter folderAdapter = new DialogFolderAdapter(mContext, getFolder());
				listviewPersonalFolder.setAdapter(folderAdapter);

				EditText textFolderName = (EditText) layoutPersonalFolderDialog.findViewById(R.id.text_folder_name);
				TextView lableChannelTitle = (TextView) v.getRootView().findViewById(R.id.label_page_title);
				textFolderName.setText(lableChannelTitle.getText());

				personalFolderDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
				personalFolderDialog.setPositiveButton("加入", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						DBHelper dbHelper = DBHelper.getInstance(mContext);
						AsyncSession asyncSession = dbHelper.getAsyncSession();
						GridView listviewPersonalFolder = (GridView) ((Dialog) dialog).findViewById(R.id.listview_personal_folder);
						String folderName = ((DialogFolderAdapter) listviewPersonalFolder.getAdapter()).getSelectedFolder();
						if (folderName.isEmpty()) {
							EditText textFolderName = (EditText) ((Dialog) dialog).findViewById(R.id.text_folder_name);
							folderName = textFolderName.getText().toString();
						}
						List<PersonalFolder> listFolder = getFolderFromDB(folderName);
						if (listFolder.size() == 0) {
							asyncSession.insert(new PersonalFolder(null, folderName, personalList.get(position).getPicURL()))
									.waitForCompletion();
						}

						asyncSession.insert(new PersonalList(null, getFolderIdFromDB(folderName), personalList.get(position).getTitle(),
								personalList.get(position).getPicURL(), personalList.get(position).getDescription()));
					}
				});
				personalFolderDialog.show();
				return true;
			}
		});
		holder.getView().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (holder.getLableAlbumDescription().getVisibility() == View.GONE) {
					holder.getLableAlbumDescription().setVisibility(View.VISIBLE);
				} else {
					holder.getLableAlbumDescription().setVisibility(View.GONE);
				}
			}
		});
	}

	private List<PersonalFolder> getFolderFromDB(String folderName) {
		QueryBuilder<PersonalFolder> queryBuilder = DBHelper.getInstance(mContext).getPersonalFolderDao().queryBuilder();
		queryBuilder.where(PersonalFolderDao.Properties.FolderName.eq(folderName));
		return queryBuilder.list();
	}

	private int getFolderIdFromDB(String folderName) {
		QueryBuilder<PersonalFolder> queryBuilder = DBHelper.getInstance(mContext).getPersonalFolderDao().queryBuilder();
		queryBuilder.where(PersonalFolderDao.Properties.FolderName.eq(folderName));
		return queryBuilder.list().get(0).getId().intValue();
	}

	private List<PersonalFolder> getFolder() {
		DBHelper dbHelper = DBHelper.getInstance(mContext);
		PersonalFolderDao personalFolderDao = dbHelper.getPersonalFolderDao();
		return personalFolderDao.loadAll();
	}
}
