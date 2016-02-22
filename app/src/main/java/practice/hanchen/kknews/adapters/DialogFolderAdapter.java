package practice.hanchen.kknews.adapters;

import android.content.Context;
import android.view.View;

import java.util.List;

import practice.hanchen.kknews.dao.PersonalFolder;

/**
 * Created by HanChen on 2016/2/22.
 */
public class DialogFolderAdapter extends FolderAdapter {
	protected int selectedPosition;

	public DialogFolderAdapter(Context context, List<PersonalFolder> personalFolders) {
		super(context, personalFolders);
		selectedPosition = -1;
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, final int position) {
		super.onBindViewHolder(holder, position);
		holder.getView().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				selectedPosition = position;
				notifyDataSetChanged();
			}
		});
		holder.getView().setSelected(selectedPosition == position);
	}

	public String getSelectedFolderName() {
		if (selectedPosition == -1) {
			return "";
		} else {
			return personalFolderList.get(selectedPosition).getFolderName();
		}
	}
}
