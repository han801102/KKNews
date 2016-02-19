package practice.hanchen.kknews;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Checkable;
import android.widget.FrameLayout;
import android.widget.GridView;

import java.util.List;

/**
 * Created by HanChen on 2016/2/2.
 */
public class FragmentPersonal extends Fragment {
	private GridView gridView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		DBHelper dbHelper = DBHelper.getInstance(getContext());
		View view = inflater.inflate(R.layout.fragment_personal, container, false);
		gridView = (GridView) view.findViewById(R.id.listview_folder);
		PersonalFolderAdapter personalFolderAdapter = new PersonalFolderAdapter(getContext(), dbHelper.getPersonalFolderAll());
		gridView.setAdapter(personalFolderAdapter);
		setHasOptionsMenu(true);
		return view;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_personal, menu);
		super.onCreateOptionsMenu(menu, inflater);
		if (((PersonalFolderAdapter) gridView.getAdapter()).getSelectedMode()) {
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
				((PersonalFolderAdapter) gridView.getAdapter()).changeSelectedMode();
				getActivity().invalidateOptionsMenu();
				return true;
			case R.id.action_accept:
				((PersonalFolderAdapter) gridView.getAdapter()).deleteFolder();
				((PersonalFolderAdapter) gridView.getAdapter()).notifyDataSetChanged();
				getActivity().invalidateOptionsMenu();
				return true;
			case R.id.action_cancel:
				((PersonalFolderAdapter) gridView.getAdapter()).resetSelection();
				((PersonalFolderAdapter) gridView.getAdapter()).notifyDataSetChanged();
				getActivity().invalidateOptionsMenu();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
