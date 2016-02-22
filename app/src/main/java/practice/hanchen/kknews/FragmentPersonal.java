package practice.hanchen.kknews;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by HanChen on 2016/2/2.
 */
public class FragmentPersonal extends Fragment {
	private RecyclerView listViewPersonalFolder;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		DBHelper dbHelper = DBHelper.getInstance(getContext());
		View view = inflater.inflate(R.layout.fragment_personal, container, false);
		listViewPersonalFolder = (RecyclerView) view.findViewById(R.id.listview_folder);
		GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 4);
		listViewPersonalFolder.setLayoutManager(gridLayoutManager);
		PersonalFolderAdapter recyclerPersonalFolderAdapter = new PersonalFolderAdapter(getContext(), dbHelper.getPersonalFolderAll());
		listViewPersonalFolder.setAdapter(recyclerPersonalFolderAdapter);
		setHasOptionsMenu(true);
		return view;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_personal, menu);
		super.onCreateOptionsMenu(menu, inflater);
		if (((PersonalFolderAdapter) listViewPersonalFolder.getAdapter()).getSelectedMode()) {
			menu.findItem(R.id.action_accept).setVisible(true);
			menu.findItem(R.id.action_cancel).setVisible(true);
		} else {
			menu.findItem(R.id.action_accept).setVisible(false);
			menu.findItem(R.id.action_cancel).setVisible(false);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_delete:
				((PersonalFolderAdapter) listViewPersonalFolder.getAdapter()).changeSelectedMode();
				getActivity().invalidateOptionsMenu();
				return true;
			case R.id.action_accept:
				((PersonalFolderAdapter) listViewPersonalFolder.getAdapter()).deleteFolder();
				listViewPersonalFolder.getAdapter().notifyDataSetChanged();
				getActivity().invalidateOptionsMenu();
				return true;
			case R.id.action_cancel:
				((PersonalFolderAdapter) listViewPersonalFolder.getAdapter()).resetSelection();
				listViewPersonalFolder.getAdapter().notifyDataSetChanged();
				getActivity().invalidateOptionsMenu();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
