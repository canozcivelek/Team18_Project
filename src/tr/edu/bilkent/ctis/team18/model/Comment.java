package tr.edu.bilkent.ctis.team18.model;

import com.google.gson.annotations.SerializedName;

public class Comment extends JsonResponseAbstract implements ListItemAbstract {

	@SerializedName("cdcomment")
	private int code;
	
	@SerializedName("cdevent")
	private int eventCode;
	
	@SerializedName("cduser")
	private int ownerCode;
	
	@SerializedName("nmuser")
	private String ownerName;
	
	@SerializedName("bluserthumb")
	private String ownerThumbnail;
	
	@SerializedName("nmcomment")
	private String text;
	
	@SerializedName("dtcommentdate")
	private String date;
	
	@SerializedName("numupvote")
	private int upVote = 0;
	
	@SerializedName("numdownvote")
	private int downVote = 0;


	public void setEventCode(int eventCode) {
		this.eventCode = eventCode;
	}

	public int getOwnerCode() {
		return ownerCode;
	}

	public void setOwnerCode(int ownerCode) {
		this.ownerCode = ownerCode;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getUpVote() {
		return upVote;
	}

	public void setUpVote(int upVote) {
		this.upVote = upVote;
	}

	public int getDownVote() {
		return downVote;
	}

	public void setDownVote(int downVote) {
		this.downVote = downVote;
	}

	public String getOwnerThumbnail() {
		return ownerThumbnail;
	}

	public void setOwnerThumbnail(String ownerThumbnail) {
		this.ownerThumbnail = ownerThumbnail;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public int getEventCode() {
		return eventCode;
	}
	
}
