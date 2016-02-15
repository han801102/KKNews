package practice.hanchen.kknews;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by HanChen on 2016/2/15.
 */
public class DialogFolderAdapter extends FolderAdapter {
	private int selectedPosition;

	public DialogFolderAdapter(Context context, List<PersonalFolder> personalFolders) {
		super(context, personalFolders);
		selectedPosition = -1;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		convertView = super.getView(position, convertView, parent);
		convertView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (selectedPosition == -1) {
					setSelected(v, position, true);
					selectedPosition = position;
				} else if (selectedPosition == position) {
					setSelected(v, position, false);
					selectedPosition = -1;
				} else {
					setSelected(v, position, true);
					setSelected(v, selectedPosition, false);
					selectedPosition = position;
				}
			}
		});
		return convertView;
	}

	private void setSelected(View v, int position, boolean isSelect) {
		GridView gridView = (GridView) v.getParent();
		int previousPosition = position - gridView.getFirstVisiblePosition();
		ImageView viewFolderCover = (ImageView) gridView.getChildAt(previousPosition).findViewById(R.id.view_folder_cover);
		if (isSelect) {
			viewFolderCover.setBackgroundColor(Color.BLUE);
		} else {
			viewFolderCover.setBackgroundColor(Color.WHITE);
		}
	}

	public String getSelectedFolder() {
		if (selectedPosition == -1) {
			return "";
		} else {
			return super.personalFolders.get(selectedPosition).getFolderName();
		}
	}
}
