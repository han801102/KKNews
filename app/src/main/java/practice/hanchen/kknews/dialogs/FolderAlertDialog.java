package practice.hanchen.kknews.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import practice.hanchen.kknews.R;
import practice.hanchen.kknews.adapters.DialogFolderAdapter;
import practice.hanchen.kknews.adapters.FolderPictureAdapter;
import practice.hanchen.kknews.dao.PersonalList;
import practice.hanchen.kknews.helpers.DBHelper;

/**
 * Created by HanChen on 2016/2/22.
 */
public class FolderAlertDialog extends AlertDialog.Builder {
	private RecyclerView listViewPersonalFolder;
	TextView labelEditTextTitle;
	TextView labelGridViewTitle;
	private EditText textFolderName;
	public FolderAlertDialog(Context context, String title, Long folderId, String folderName) {
		super(context);
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		View layoutPersonalFolderDialog = layoutInflater.inflate(R.layout.layout_personal_folder_dialog, null);
		setView(layoutPersonalFolderDialog);
		setTitle(title);
		labelEditTextTitle = (TextView) layoutPersonalFolderDialog.findViewById(R.id.label_edit_text_title);
		labelGridViewTitle = (TextView) layoutPersonalFolderDialog.findViewById(R.id.label_grid_view_title);

		DBHelper dbHelper = DBHelper.getInstance(context);
		listViewPersonalFolder = (RecyclerView) layoutPersonalFolderDialog.findViewById(R.id.listview_folder_cover);
		GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
		listViewPersonalFolder.setLayoutManager(gridLayoutManager);
		if (folderId > 0) {
			List<PersonalList> personalLists = dbHelper.getPersonalListByFolderId(folderId);
			FolderPictureAdapter folderPictureAdapter = new FolderPictureAdapter(context, dbHelper.getPersonalArticle(personalLists));
			listViewPersonalFolder.setAdapter(folderPictureAdapter);
		} else {
			DialogFolderAdapter recyclerDialogFolderAdapter = new DialogFolderAdapter(context, dbHelper.getPersonalFolderAll());
			listViewPersonalFolder.setAdapter(recyclerDialogFolderAdapter);
		}
		textFolderName = (EditText) layoutPersonalFolderDialog.findViewById(R.id.text_folder_name);
		textFolderName.setText(folderName);
		setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
	}

	public String getTextFormEditText() {
		return textFolderName.getText().toString();
	}

	public String getSelectedFolderName() {
		return ((DialogFolderAdapter)listViewPersonalFolder.getAdapter()).getSelectedFolderName();
	}

	public void setLabelEditTextTitle(String title) {
		labelEditTextTitle.setText(title);
	}

	public void setLabelGridViewTitle(String title) {
		labelGridViewTitle.setText(title);
	}
}
