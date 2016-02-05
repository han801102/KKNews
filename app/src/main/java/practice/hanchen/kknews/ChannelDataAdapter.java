package practice.hanchen.kknews;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.opengl.Visibility;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.async.AsyncSession;
import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by HanChen on 2016/2/2.
 */
//public class ChannelsAdapter extends BaseAdapter {
public class ChannelDataAdapter extends RecyclerView.Adapter<ChannelDataAdapter.ViewHolder> {
	private ArrayList<ChannelData> channelDataArrayList;
	private Context mContext;
	private static final String LOG_TAG = "ChannelDataAdapter";
	public static class ViewHolder extends RecyclerView.ViewHolder {
		private final TextView lableAlbumName;
		private final ImageView viewAlbum;
		private final TextView lableAlbumDescription;
		private View view;

		public ViewHolder(View v) {
			super(v);
			view = v;
			lableAlbumName = (TextView) v.findViewById(R.id.lable_album_name);
			viewAlbum = (ImageView) v.findViewById(R.id.view_album);
			lableAlbumDescription = (TextView) v.findViewById(R.id.lable_album_description);
		}

		public TextView getLableAlbumName() {
			return lableAlbumName;
		}

		public ImageView getViewAlbum() {
			return viewAlbum;
		}

		public TextView getLableAlbumDescription() {
			return lableAlbumDescription;
		}

		public View getView(){
			return view;
		}
	}

	public ChannelDataAdapter(Context context, ArrayList<ChannelData> channelList) {
		this.channelDataArrayList = channelList;
		this.mContext = context;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.listview_item_channel_data, parent, false);
		Log.d("han", Integer.toString(parent.getChildCount()));
		return new ViewHolder(v);
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, final int position) {
		holder.getLableAlbumName().setText(channelDataArrayList.get(position).getTitle());
		holder.getLableAlbumDescription().setText(Html.fromHtml(channelDataArrayList.get(position).getDescription()));
		holder.getLableAlbumDescription().setMovementMethod(LinkMovementMethod.getInstance());
		holder.getView().setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				LayoutInflater layoutInflater = LayoutInflater.from(v.getContext());
				final View layoutPersonalFolderDialog = layoutInflater.inflate(R.layout.layout_personal_folder_dialog, null);

				AlertDialog.Builder personalFolderDialog = new AlertDialog.Builder(v.getContext());
				personalFolderDialog.setTitle("加入個人精選");
				personalFolderDialog.setView(layoutPersonalFolderDialog);
				EditText textFolderName = (EditText) layoutPersonalFolderDialog.findViewById(R.id.text_folder_name);
				TextView lableChannelTitle = (TextView) v.getRootView().findViewById(R.id.lable_channel_title);
				textFolderName.setText(lableChannelTitle.getText());
				personalFolderDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
				personalFolderDialog.setPositiveButton("加入", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Log.d("han", "add");
						DBHelper dbHelper = DBHelper.getInstance(mContext);
						AsyncSession asyncSession = dbHelper.getAsyncSession();
						EditText textFolderName = (EditText) ((Dialog) dialog).findViewById(R.id.text_folder_name);
						String folderName = textFolderName.getText().toString();
						List<PersonalFolder> listFolder = getFolderFromDB(folderName);
						if( listFolder.size() == 0 ) {
							asyncSession.insert(new PersonalFolder(null, folderName, channelDataArrayList.get(position).getPictureURL() ));
							Log.d("han", "size = 0");
						} else {
							Log.d("han", "size > 0");
						}
						asyncSession.insert(new PersonalList(getFolderIdFromDB(folderName), channelDataArrayList.get(position).getTitle(), channelDataArrayList.get(position).getPictureURL()));
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
		Picasso.with(mContext)
				.load(channelDataArrayList.get(position).getPictureURL())
				.resize(400, 400)
				.into(holder.getViewAlbum());
	}

	@Override
	public int getItemCount() {
		return channelDataArrayList.size();
	}

	public List<PersonalFolder> getFolderFromDB(String folderName ){
		QueryBuilder<PersonalFolder> queryBuilder = DBHelper.getInstance(mContext).getPersonalFolderDao().queryBuilder();
		queryBuilder.where(PersonalFolderDao.Properties.FolderName.eq(folderName));
		return queryBuilder.list();
	}


	public int getFolderIdFromDB(String folderName) {
		QueryBuilder<PersonalFolder> queryBuilder = DBHelper.getInstance(mContext).getPersonalFolderDao().queryBuilder();
		queryBuilder.where(PersonalFolderDao.Properties.FolderName.eq(folderName));
		return queryBuilder.list().get(0).getId().intValue();
	}
}
