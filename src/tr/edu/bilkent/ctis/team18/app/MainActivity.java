package tr.edu.bilkent.ctis.team18.app;

import org.apache.http.client.methods.HttpGet;

import tr.edu.bilkent.ctis.team18.adapters.DBAdapter;
import tr.edu.bilkent.ctis.team18.adapters.HttpAdapter;
import tr.edu.bilkent.ctis.team18.adapters.ListCommentAdapter;
import tr.edu.bilkent.ctis.team18.adapters.ListEventAdapter;
import tr.edu.bilkent.ctis.team18.model.Event;
import tr.edu.bilkent.ctis.team18.model.JsonResponseAbstract;
import tr.edu.bilkent.ctis.team18.model.SerializableContainer;
import tr.edu.bilkent.ctis.team18.model.User;
import tr.edu.bilkent.ctis.team18.util.ActivityConstants;
import tr.edu.bilkent.ctis.team18.util.IntentConstants;
import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity implements OnItemClickListener {

	String APP_ID = "347798612038640";
	ListView lvMainUpcoming;
	TextView tv, userNameSurname;
	private DBAdapter db;
	Event[] events;
	private ListEventAdapter eventAdapter;
	private User user;
	private TextView tvMainUserName;
	private ImageView ivMainUserPic;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.main);
		lvMainUpcoming = (ListView) findViewById(R.id.lvMainUpcoming);
		lvMainUpcoming.setOnItemClickListener(this);
		
		this.user = (User) getIntent().getSerializableExtra("user");
		setUpcomingList();
		
		tvMainUserName = (TextView) findViewById(R.id.tvMainUserName);
		ivMainUserPic = (ImageView) findViewById(R.id.ivMainUserPic);
		setUserProfile();
	}

	public void clickList(View v) {
		HttpAdapter httpAdapter = new HttpAdapter(getApplicationContext());
		Event[] events = null;
		try {
			events = (Event[]) httpAdapter.doHttpRequest(new HttpGet(),
					getString(R.string.ws_events_list), null, Event[].class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SerializableContainer sContainer = new SerializableContainer(
				(JsonResponseAbstract[]) events);
		Intent intent = new Intent(this, ListEventsActivity.class);
		intent.putExtra(IntentConstants.USER, user);
		intent.putExtra(IntentConstants.EVENTS, sContainer);
		intent.putExtra(IntentConstants.CALLER_ACTIVITY, ActivityConstants.MAIN);
		startActivity(intent);
	}

	public void clickSearch(View v) {
		Intent intent = new Intent(this, SearchActivity.class);
		intent.putExtra(IntentConstants.USER, user);
		startActivity(intent);
	}

	public void clickCreate(View v) {
		Intent intent = new Intent(this, CreateEventActivity.class);
		intent.putExtra(IntentConstants.USER, user);
		startActivity(intent);
	}
	@Override
	protected void onResume(){
		super.onResume();
		setUpcomingList();
	}
	
	private void setUserProfile(){
		if ((!user.getThumbnail().equals(""))&&(user.getThumbnail() != null)) {
			byte[] imageAsBytes = Base64.decode(user
					.getThumbnail().getBytes(), Base64.DEFAULT);
			ivMainUserPic.setImageBitmap(BitmapFactory.decodeByteArray(
					imageAsBytes, 0, imageAsBytes.length));
		}
		
		tvMainUserName.setText(user.getName());
	}

	private void setUpcomingList() {
		HttpAdapter httpAdapter = new HttpAdapter(getApplicationContext());
		String[] params = {user.getCode()+""};
		try {
			events = (Event[]) httpAdapter
					.doHttpRequest(new HttpGet(),
							getString(R.string.ws_events_attended), params,
							Event[].class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(events.length!=0){
			eventAdapter = new ListEventAdapter(this, events);
			lvMainUpcoming.setAdapter(eventAdapter);
		}
	}
	
	

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		Intent intent = new Intent(this, EventDetailActivity.class);
    	intent.putExtra(IntentConstants.USER, user);
    	intent.putExtra(IntentConstants.EVENT_CODE, events[position].getCode());
    	intent.putExtra(IntentConstants.CALLER_ACTIVITY, ActivityConstants.MAIN);
    	startActivity(intent);
		
	}

}
