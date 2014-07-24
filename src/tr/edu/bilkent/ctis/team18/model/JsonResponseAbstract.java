package tr.edu.bilkent.ctis.team18.model;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class JsonResponseAbstract implements Serializable{
	
	@SerializedName("cdautocomplete")
	public int cdAutocomplete;
	
	@SerializedName("nmautocomplete")
	public String nmAutocomplete;
	
	@SerializedName("requeststatus")
	public String nmRequestStatus;

	public int getCdAutocomplete() {
		return cdAutocomplete;
	}

	public void setCdAutocomplete(int cdAutocomplete) {
		this.cdAutocomplete = cdAutocomplete;
	}

	public String getNmAutocomplete() {
		return nmAutocomplete;
	}

	public void setNmAutocomplete(String nmAutocomplete) {
		this.nmAutocomplete = nmAutocomplete;
	}
	
	public String getNmRequestStatus() {
		return nmRequestStatus;
	}

	public void setNmRequestStatus(String nmRequestStatus) {
		this.nmRequestStatus = nmRequestStatus;
	}
	
	@Override
    public String toString() {
        return this.nmAutocomplete;  // What to display in the Spinner list.
    }

}
