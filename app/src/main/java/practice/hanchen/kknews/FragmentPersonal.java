package practice.hanchen.kknews;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.List;

/**
 * Created by HanChen on 2016/2/2.
 */
public class FragmentPersonal extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_personal, container, false);
		GridView gridView = (GridView) view.findViewById(R.id.listview_folder);
		PersonalFolderAdapter personalFolderAdapter = new PersonalFolderAdapter(getContext(), getFolder());
		gridView.setAdapter(personalFolderAdapter);
		return view;
	}

	private List<PersonalFolder> getFolder() {
		DBHelper dbHelper = DBHelper.getInstance(getContext());
		PersonalFolderDao personalFolderDao = dbHelper.getPersonalFolderDao();
		return personalFolderDao.loadAll();
	}
}
