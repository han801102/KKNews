package practice.hanchen.kknews;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

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
		convertView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, PersonalArticleActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("id", personalFolders.get(position).getId().intValue());
				bundle.putString("title", personalFolders.get(position).getFolderName());
				intent.putExtras(bundle);
				mContext.startActivity(intent);
			}
		});
		return convertView;
	}
}
