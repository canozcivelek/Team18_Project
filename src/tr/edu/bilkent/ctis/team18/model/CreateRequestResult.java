package tr.edu.bilkent.ctis.team18.model;

import com.google.gson.annotations.SerializedName;

public class CreateRequestResult extends JsonResponseAbstract{
	@SerializedName("status")
	public String status;

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
