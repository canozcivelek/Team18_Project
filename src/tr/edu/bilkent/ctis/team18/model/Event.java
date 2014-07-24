package tr.edu.bilkent.ctis.team18.model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.google.gson.annotations.SerializedName;

public class Event extends JsonResponseAbstract implements ListItemAbstract {
	
	
	@SerializedName("cdevent")
	public int code;
	
	@SerializedName("nmevent")
	public String title;

	@SerializedName("nmexplanation")
	public String explanation;
	
	@SerializedName("cdowner")
	public int cdOwner;

	@SerializedName("dtstartdate")
	public String startDate;

	@SerializedName("tmstarttime")
	public String startTime;

	@SerializedName("dtfinishdate")
	public String finishDate;

	@SerializedName("tmfinishtime")
	public String finishTime;

	@SerializedName("nmaddress")
	public String address;

	@SerializedName("numofattend")
	public int numOfAttend;

	@SerializedName("numoflike")
	public int numOfLike;
	
	@SerializedName("numofunlike")
	public int numOfUnlike;
	
	@SerializedName("cdcity")
	public int city;
	
	@SerializedName("cdcounty")
	public int county;

	@SerializedName("cdcategory")
	public int category;
	
	@SerializedName("blthumbnail")
	public String thumbnail;
	
	@SerializedName("rate")
	public String rate;
	
	
	public int getCode() {
		return code;
	}	

	public void setCode(int code) {
		this.code = code;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getExplanation() {
		return explanation;
	}

	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getFinishDate() {
		return finishDate;
	}

	public void setFinishDate(String finishDate) {
		this.finishDate = finishDate;
	}

	public String getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getNumOfAttend() {
		return numOfAttend;
	}

	public void setNumOfAttend(int numOfAttend) {
		this.numOfAttend = numOfAttend;
	}

	public int getNumOfLike() {
		return numOfLike;
	}

	public void setNumOfLike(int numOfLike) {
		this.numOfLike = numOfLike;
	}

	public int getCity() {
		return city;
	}

	public void setCity(int city) {
		this.city = city;
	}

	public int getCounty() {
		return county;
	}

	public void setCounty(int county) {
		this.county = county;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public int getCdOwner() {
		return cdOwner;
	}

	public void setCdOwner(int cdOwner) {
		this.cdOwner = cdOwner;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public int getNumOfUnlike() {
		return numOfUnlike;
	}

	public void setNumOfUnlike(int numOfUnlike) {
		this.numOfUnlike = numOfUnlike;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}
	
}
