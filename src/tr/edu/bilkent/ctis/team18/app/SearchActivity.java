package tr.edu.bilkent.ctis.team18.app;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;

import tr.edu.bilkent.ctis.team18.adapters.AutoCompleteAdapter;
import tr.edu.bilkent.ctis.team18.adapters.HttpAdapter;
import tr.edu.bilkent.ctis.team18.model.Event;
import tr.edu.bilkent.ctis.team18.model.JsonResponseAbstract;
import tr.edu.bilkent.ctis.team18.model.SerializableContainer;
import tr.edu.bilkent.ctis.team18.model.User;
import tr.edu.bilkent.ctis.team18.util.ActivityConstants;
import tr.edu.bilkent.ctis.team18.util.IntentConstants;
import tr.edu.bilkent.ctis.team18.util.TextWatcherCustom;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.GsonBuilder;

public class SearchActivity extends Activity implements OnClickListener {

	static final String TAG = "SearchActivity";
	private User user;
	private Event event;
	
	
	SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
	

	/********************************
	 * Layout Component Variables *
	 * ******************************/
	private EditText etSrcEvTitle;
	private EditText etSrcEvDesc;
	private EditText etSrcEvAddress;
	private EditText etSrcEvStartDate;
	private EditText etSrcEvStartTime;
	private EditText etSrcEvEndDate;
	private EditText etSrcEvEndTime;
	

	private Spinner spSrcEvStartDate;
	private Spinner spSrcEvStartTime;
	private Spinner spSrcEvEndDate;
	private Spinner spSrcEvEndTime;
	
	private String spSrcEvStartDateComp;
	private String spSrcEvStartTimeComp;
	private String spSrcEvEndDateComp;
	private String spSrcEvEndTimeComp;
	
	private CheckBox cbSrcEvMyEvents;

	private Button btnSrcEvStartDate;
	private Button btnSrcEvStartTime;
	private Button btnSrcEvEndDate;
	private Button btnSrcEvEndTime;
	private Button btnSrcEvSearch;

	private Spinner spSrcEvCategory;

	private AutoCompleteTextView acSrcEvCity;
	private AutoCompleteTextView acSrcEvCounty;

	private AutoCompleteAdapter acCityAdapter;
	private AutoCompleteAdapter acCountyAdapter;

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.search_event);
		event = new Event();
		user = (User)getIntent().getSerializableExtra("user");
		fetchLayoutComponents();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnSrcEvSearch:
			
			try {
				Event[] eventsResult;
				HttpAdapter httpAdapter = new HttpAdapter(getApplicationContext());
				eventsResult = (Event[]) httpAdapter
						.doHttpRequest(prepareRequestBody(),
								getString(R.string.ws_events_search), null,
								Event[].class);
				Toast.makeText(getApplicationContext(),
						((JsonResponseAbstract[])eventsResult)[0].getNmRequestStatus(),
						Toast.LENGTH_SHORT).show();
				if(eventsResult[0].getCode()!=0){
					SerializableContainer sContainer = new SerializableContainer(eventsResult);
					Intent intent = new Intent(this, ListEventsActivity.class);
			    	intent.putExtra(IntentConstants.USER, user);
			    	intent.putExtra(IntentConstants.EVENTS, sContainer);
			    	intent.putExtra(IntentConstants.CALLER_ACTIVITY, ActivityConstants.SEARCH_EVENT);
			    	startActivity(intent);
					finish();
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.d(TAG, "onClick");
			break;
		case R.id.btnSrcEvStartDate:
			showAndSaveDatePicker(etSrcEvStartDate);
			break;
		case R.id.btnSrcEvStartTime:
			showAndSaveTimePicker(etSrcEvStartTime);
			break;
		case R.id.btnSrcEvEndDate:
			showAndSaveDatePicker(etSrcEvEndDate);
			break;
		case R.id.btnSrcEvEndTime:
			showAndSaveTimePicker(etSrcEvEndTime);
			break;
		default:
			// (User) ( (Spinner) findViewById(R.id.user) ).getSelectedItem();
			break;
		}
	}
	
	private HttpPost prepareRequestBody() throws UnsupportedEncodingException {


		event.setTitle(etSrcEvTitle.getText().toString());
		// event.setAddress(acEtSrcEvAddress.getText().toString());
		event.setExplanation(etSrcEvDesc.getText().toString());
		event.setStartDate(etSrcEvStartDate.getText().toString());
		event.setStartTime(etSrcEvStartTime.getText().toString());
		
		if(cbSrcEvMyEvents.isChecked())
			event.setCdOwner(user.getCode());
		
		String str="";
		str = etSrcEvStartDate.getText().toString()==null?"":spSrcEvStartDateComp+"___"+etSrcEvStartDate.getText().toString();
		event.setStartDate(str);
		
		str = etSrcEvStartTime.getText().toString()==null?"":spSrcEvStartTimeComp+"___"+etSrcEvStartTime.getText().toString();
		event.setStartTime(str);
		
		str = etSrcEvEndDate.getText().toString()==null?"":spSrcEvEndDateComp+"___"+etSrcEvEndDate.getText().toString();
		event.setFinishDate(str);
		
		str = etSrcEvEndTime.getText().toString()==null?"":spSrcEvEndTimeComp+"___"+etSrcEvEndTime.getText().toString();
		event.setFinishTime(str);
		
		
		
		event.setAddress(etSrcEvAddress.getText().toString());
		
		
		String jEvent = new GsonBuilder().disableHtmlEscaping().create().toJson(event);
		HttpPost post = new HttpPost();
		post.setHeader("Content-Type", "application/json;charset=utf-8");
		StringEntity entity = new StringEntity(jEvent,HTTP.UTF_8);
		entity.setContentType("utf-8");
		post.setEntity(entity);
		Log.d(TAG+" -- Event Parameter", jEvent);
		return post;
	}

	private void fetchLayoutComponents() {

		Log.d(TAG, "fetchLayoutComponents");

		etSrcEvTitle = (EditText) findViewById(R.id.etSrcEvTitle);
		spSrcEvCategory = (Spinner) findViewById(R.id.spSrcEvCategory);
		etSrcEvAddress = (EditText) findViewById(R.id.etSrcEvAddress);
		etSrcEvDesc = (EditText) findViewById(R.id.etSrcEvDesc);
		etSrcEvStartDate = (EditText) findViewById(R.id.etSrcEvStartDate);
		etSrcEvStartTime = (EditText) findViewById(R.id.etSrcEvStartTime);
		etSrcEvEndDate = (EditText) findViewById(R.id.etSrcEvEndDate);
		etSrcEvEndTime = (EditText) findViewById(R.id.etSrcEvEndTime);
		
		cbSrcEvMyEvents = (CheckBox) findViewById(R.id.cbSrcEvMyEvents);

		etSrcEvTitle
				.addTextChangedListener(new TextWatcherCustom(etSrcEvTitle));
		etSrcEvDesc.addTextChangedListener(new TextWatcherCustom(etSrcEvDesc));
		etSrcEvStartDate.addTextChangedListener(new TextWatcherCustom(
				etSrcEvStartDate));
		etSrcEvStartTime.addTextChangedListener(new TextWatcherCustom(
				etSrcEvStartTime));
		etSrcEvEndDate.addTextChangedListener(new TextWatcherCustom(
				etSrcEvEndDate));
		etSrcEvEndTime.addTextChangedListener(new TextWatcherCustom(
				etSrcEvEndTime));

		btnSrcEvStartDate = (Button) findViewById(R.id.btnSrcEvStartDate);
		btnSrcEvStartTime = (Button) findViewById(R.id.btnSrcEvStartTime);
		btnSrcEvEndDate = (Button) findViewById(R.id.btnSrcEvEndDate);
		btnSrcEvEndTime = (Button) findViewById(R.id.btnSrcEvEndTime);
		
		spSrcEvStartTime = (Spinner) findViewById(R.id.spSrcEvStartTime);
		spSrcEvEndDate = (Spinner) findViewById(R.id.spSrcEvEndDate);
		spSrcEvEndTime = (Spinner) findViewById(R.id.spSrcEvEndTime);
		spSrcEvStartDate = (Spinner) findViewById(R.id.spSrcEvStartDate);

		btnSrcEvSearch = (Button) findViewById(R.id.btnSrcEvSearch);

		btnSrcEvSearch.setOnClickListener((OnClickListener) this);

		btnSrcEvStartDate.setOnClickListener((OnClickListener) this);
		btnSrcEvStartTime.setOnClickListener((OnClickListener) this);
		btnSrcEvEndDate.setOnClickListener((OnClickListener) this);
		btnSrcEvEndTime.setOnClickListener((OnClickListener) this);
		
		spSrcEvStartDate.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				spSrcEvStartDateComp =(String)spSrcEvStartDate.getSelectedItem();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		spSrcEvStartTime.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				spSrcEvStartTimeComp =(String)spSrcEvStartTime.getSelectedItem();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		spSrcEvEndDate.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				spSrcEvEndDateComp =(String)spSrcEvEndDate.getSelectedItem();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		spSrcEvEndTime.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				spSrcEvEndTimeComp =(String)spSrcEvEndTime.getSelectedItem();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		spSrcEvCategory.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				event.setCategory(((JsonResponseAbstract) spSrcEvCategory
						.getAdapter().getItem(arg2)).getCdAutocomplete());

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		String[] comparisons  = getResources().getStringArray(R.array.array_comparisons); 
		final ArrayAdapter<String> comparisonAdapter = new ArrayAdapter<String>(
				this, R.layout.autocomplete, Arrays.asList(comparisons));
		
		spSrcEvStartTime.setAdapter(comparisonAdapter);
		spSrcEvEndDate.setAdapter(comparisonAdapter);
		spSrcEvEndTime.setAdapter(comparisonAdapter);
		spSrcEvStartDate.setAdapter(comparisonAdapter);

		fillCategorySpinner();

		acSrcEvCity = (AutoCompleteTextView) findViewById(R.id.acSrcEvCity);
		acSrcEvCounty = (AutoCompleteTextView) findViewById(R.id.acSrcEvCounty);
		addressAuthComp();

	}

	private void fillCategorySpinner() {
		ArrayList<JsonResponseAbstract> resultList = null;
		JsonResponseAbstract[] jsonResponseAbstracts;
		try {
			HttpAdapter httpAdapter = new HttpAdapter(getApplicationContext());
			jsonResponseAbstracts = (JsonResponseAbstract[]) httpAdapter
					.doHttpRequest(new HttpGet(),
							((Activity) SearchActivity.this)
									.getString(R.string.ws_category_list),
							null, JsonResponseAbstract[].class);
			resultList = new ArrayList<JsonResponseAbstract>(
					Arrays.asList(jsonResponseAbstracts));
			JsonResponseAbstract emptyItem = new JsonResponseAbstract();
			emptyItem.setCdAutocomplete(0);
			emptyItem.setNmAutocomplete("Select Category");
			resultList.add(emptyItem);
			final ArrayAdapter<JsonResponseAbstract> categoryAdapter = new ArrayAdapter<JsonResponseAbstract>(
					this, R.layout.autocomplete, resultList);

			spSrcEvCategory.setAdapter(categoryAdapter);
			spSrcEvCategory.setSelection(categoryAdapter.getCount() - 1);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void addressAuthComp() {
		// --------------------------COUNTY Autocomplete Adapter and OnItemClick Settings-------------------
		acCountyAdapter = new AutoCompleteAdapter(getApplicationContext(),
				R.string.ws_autocomplete_county, new String[] { event.getCity()
						+ "" });
		acSrcEvCounty.setAdapter(acCountyAdapter);
		acSrcEvCounty.setThreshold(10000);
		acSrcEvCounty.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				JsonResponseAbstract county = acCountyAdapter
						.getSelectedItem(position);
				event.setCounty(county.getCdAutocomplete());

				Log.d(TAG, "acSrcEvCounty.setOnItemClickListener");
			}
		});

		// --------------------------CITY Autocomplete Adapter and OnItemClick
		// Settings-------------------
		acCityAdapter = new AutoCompleteAdapter(getApplicationContext(),
				R.string.ws_autocomplete_city, null);
		acSrcEvCity.setAdapter(acCityAdapter);
		acSrcEvCity.setThreshold(2);
		acSrcEvCity.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				JsonResponseAbstract city = acCityAdapter
						.getSelectedItem(position);
				event.setCity(city.getCdAutocomplete());
				acCountyAdapter.setRequestParams(new String[] { event.getCity()
						+ "" });
				acSrcEvCounty.setThreshold(1);

				Log.d(TAG, "acSrcEvCity.setOnItemClickListener");
			}
		});
	}

	private void showAndSaveDatePicker(final EditText et) {
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);

		DatePickerDialog dpd = new DatePickerDialog(this,
				new DatePickerDialog.OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int yearA,
							int monthOfYear, int dayOfMonth) {
						et.setText(dateFormat.format(new Date(yearA-1900,monthOfYear,dayOfMonth)));
					}
				}, year, month, day);
		dpd.show();
	}

	private void showAndSaveTimePicker(final EditText et) {
		int hour = 0;
		int minute = 0;
		TimePickerDialog tpd = new TimePickerDialog(this,
				new TimePickerDialog.OnTimeSetListener() {

					@Override
					public void onTimeSet(TimePicker view, int hourOfDay,
							int minute) {
						et.setText(timeFormat.format(new Date(0,0,0,hourOfDay,minute,0)));
						
					}
				}, hour, minute, false);
		tpd.show();
	}

}
