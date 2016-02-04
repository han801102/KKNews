package practice.hanchen.kknews;

/**
 * Created by HanChen on 2016/2/3.
 */
public class ChannelData {
	private String title;


	private String pictureURL;

	private String description;
	private String link;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPictureURL() {
		return pictureURL;
	}

	public void setPictureURL(String pictureURL) {
		this.pictureURL = pictureURL;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public ChannelData(String title, String description, String link, String pictureURL) {
		this.title = title;
		this.description = description;
		this.link = link;
		this.pictureURL = pictureURL;
	}
}
