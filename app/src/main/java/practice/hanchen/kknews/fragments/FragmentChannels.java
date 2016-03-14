package practice.hanchen.kknews.fragments;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.leakcanary.RefWatcher;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import practice.hanchen.kknews.KKNewsApplication;
import practice.hanchen.kknews.activities.MainActivity;
import practice.hanchen.kknews.adapters.ChannelsAdapter;
import practice.hanchen.kknews.dao.Channel;
import practice.hanchen.kknews.helpers.DBHelper;
import practice.hanchen.kknews.R;

/**
 * Created by HanChen on 2016/2/2.
 */
public class FragmentChannels extends Fragment {
	DBHelper dbHelper;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dbHelper = DBHelper.getInstance(getContext());

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_channels, container, false);
		RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.listview_channels);
		LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
		recyclerView.setLayoutManager(mLayoutManager);

		ChannelsAdapter channelsAdapter = new ChannelsAdapter(getActivity(), loadChannelsFormDB());
		recyclerView.setAdapter(channelsAdapter);
		return view;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		RefWatcher refWatcher = KKNewsApplication.getRefWatcher(getActivity());
		refWatcher.watch(this);
	}

	private List<Channel> loadChannelsFormDB() {
		if (isChannelsInDB()) {
			return dbHelper.getChannels();
		} else {
			AssetManager assetManager = getActivity().getAssets();
			InputStream inputStream;
			try {
				inputStream = assetManager.open("channels_list.xml");
				BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
				StringBuilder total = new StringBuilder();
				String line;
				while ((line = r.readLine()) != null) {
					total.append(line);
				}
				return parseChannelInfoFromXML(total.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return new ArrayList<>();
	}

	private List<Channel> parseChannelInfoFromXML(String XMLContents) {
		Document xmlDoc = Jsoup.parse(XMLContents);
		Elements channel = xmlDoc.select("channel");
		List<Channel> channels = new ArrayList<>();
		for (Element item : channel) {
			Channel tempChannel = new Channel(null, item.select("title").get(0).text(), item.select("url").get(0).text());
			dbHelper.insertChannel(tempChannel);
			channels.add(tempChannel);
		}
		return channels;
	}

	private boolean isChannelsInDB() {
		return dbHelper.getChannelsCount() > 0;
	}
}
