package tr.edu.bilkent.ctis.team18.app;

import java.util.List;

import org.apache.http.client.methods.HttpGet;

import tr.edu.bilkent.ctis.team18.adapters.ListEventAdapter;
import tr.edu.bilkent.ctis.team18.adapters.HttpAdapter;
import tr.edu.bilkent.ctis.team18.model.Event;
import tr.edu.bilkent.ctis.team18.model.SerializableContainer;
import tr.edu.bilkent.ctis.team18.model.User;
import tr.edu.bilkent.ctis.team18.util.ActivityConstants;
import tr.edu.bilkent.ctis.team18.util.IntentConstants;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

public class ListEventsActivity extends ListActivity {

	private HttpAdapter httpAdapter;
	private User user;
	Event[] events;
	int callerActivity = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.user = (User) getIntent().getSerializableExtra(IntentConstants.USER);
		this.callerActivity = getIntent().getIntExtra(IntentConstants.CALLER_ACTIVITY,0);
		httpAdapter = new HttpAdapter(getApplicationContext());
		initialize();
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		if (callerActivity!=ActivityConstants.SEARCH_EVENT) {
			HttpAdapter httpAdapter = new HttpAdapter(getApplicationContext());

			try {
				events = (Event[]) httpAdapter
						.doHttpRequest(new HttpGet(),
								getString(R.string.ws_events_list), null,
								Event[].class);
				ListEventAdapter customAdapter = new ListEventAdapter(this,
						events);

				setListAdapter(customAdapter);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	protected void initialize() {
		try {

			events = (Event[]) ((SerializableContainer) getIntent()
					.getSerializableExtra(IntentConstants.EVENTS)).getList();
			ListEventAdapter customAdapter = new ListEventAdapter(this, events);

			setListAdapter(customAdapter);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		Intent intent = new Intent(this, EventDetailActivity.class);
		intent.putExtra(IntentConstants.USER, user);
		intent.putExtra(IntentConstants.EVENT_CODE, events[position].getCode());
		startActivity(intent);

	}

}
