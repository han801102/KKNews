package practice.hanchen.kknews;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by HanChen on 2016/2/2.
 */
public class FragmentChannels extends Fragment {
	ArrayList<ChannelInfo> channelInfoArrayList = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (channelInfoArrayList == null) {
			channelInfoArrayList = new ArrayList<ChannelInfo>();
			try {
				AssetManager assetManager = getActivity().getAssets();
				InputStream inputStream;
				inputStream = assetManager.open("channels_list.xml");
				parseChannelInfoFromXML(inputStream);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_channels, container, false);
		RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.listview_channels);
		LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
		recyclerView.setLayoutManager(mLayoutManager);

		ChannelsAdapter channelsAdapter = new ChannelsAdapter(getActivity(), channelInfoArrayList);
		recyclerView.setAdapter(channelsAdapter);
		return view;
	}

	private void parseChannelInfoFromXML(InputStream inputStream) {
		String url = "";
		String title = "";
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = null;
			builder = factory.newDocumentBuilder();
			Document document = builder.parse(inputStream);
			Element root = document.getDocumentElement();
			NodeList nodes = root.getElementsByTagName("channel");
			for (int i = 0; i < nodes.getLength(); i++) {
				Element channelElement = (Element) nodes.item(i);
				NodeList channelNodeList = channelElement.getChildNodes();
				for (int j = 0; j < channelNodeList.getLength(); j++) {
					Node channelNode = channelNodeList.item(j);
					String nodeName = channelNode.getNodeName();
					if (nodeName.equals("title")) {
						title = channelNode.getFirstChild().getNodeValue();
					} else if (nodeName.equals("url")) {
						url = channelNode.getFirstChild().getNodeValue();
					}
				}
				channelInfoArrayList.add(new ChannelInfo(title, url));
			}
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}

	}
}
