package tr.edu.bilkent.ctis.team18.model;

import java.io.Serializable;
import java.util.List;

public class SerializableContainer implements Serializable{
	JsonResponseAbstract[] list;
	JsonResponseAbstract event;
	
	public SerializableContainer(JsonResponseAbstract[] list){
		this.list = list;
	}
	
	public SerializableContainer(JsonResponseAbstract event){
		this.event = event;
	}

	public JsonResponseAbstract[] getList() {
		return list;
	}

	public void setList(JsonResponseAbstract[] list) {
		this.list = list;
	}

	public JsonResponseAbstract getEvent() {
		return event;
	}

	public void setEvent(JsonResponseAbstract event) {
		this.event = event;
	}
	
}
