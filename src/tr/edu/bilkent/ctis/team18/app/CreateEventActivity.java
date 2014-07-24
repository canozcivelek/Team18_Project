package tr.edu.bilkent.ctis.team18.app;

import java.io.ByteArrayOutputStream;
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
import tr.edu.bilkent.ctis.team18.model.CreateRequestResult;
import tr.edu.bilkent.ctis.team18.model.Event;
import tr.edu.bilkent.ctis.team18.model.JsonResponseAbstract;
import tr.edu.bilkent.ctis.team18.model.User;
import tr.edu.bilkent.ctis.team18.util.TextWatcherCustom;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.GsonBuilder;

public class CreateEventActivity extends Activity implements OnClickListener {

	private static final int RESULT_LOAD_IMAGE = 1;
	static final String TAG = "CreateEventActivity";
	SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

	/********************************
	 * Layout Component Variables *
	 * ******************************/
	private EditText etCrEvTitle;
	private EditText etCrEvDesc;
	private EditText etCrEvStartDate;
	private EditText etCrEvStartTime;
	private EditText etCrEvEndDate;
	private EditText etCrEvEndTime;

	private Button btnCrEvShowAddrDialog;
	private Button btnCrEvStartDate;
	private Button btnCrEvStartTime;
	private Button btnCrEvEndDate;
	private Button btnCrEvEndTime;
	private Button btnCrEvSetThumbnail;
	private Button btnCrEvCreate;

	private Spinner spCrEvCategory;

	private User user;
	private Event event;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.create_event);
		this.user = (User) getIntent().getSerializableExtra("user");
		event = new Event();

		fetchLayoutComponents();

	}

	public void clickSave(View v) {

	}

	private HttpPost prepareRequestBody() throws UnsupportedEncodingException {

		event.setTitle(etCrEvTitle.getText().toString());
		// event.setAddress(acEtCrEvAddress.getText().toString());
		event.setExplanation(etCrEvDesc.getText().toString());
		event.setStartDate(etCrEvStartDate.getText().toString());
		event.setStartTime(etCrEvStartTime.getText().toString());
		event.setFinishDate(etCrEvEndDate.getText().toString());
		event.setFinishTime(etCrEvEndTime.getText().toString());

		event.setFinishTime(etCrEvEndTime.getText().toString());
		event.setCdOwner(user.getCode());

		String jEvent = new GsonBuilder().disableHtmlEscaping().create()
				.toJson(event);
		HttpPost post = new HttpPost();

		post.setHeader("Content-Type", "application/json;charset=utf-8");
		StringEntity entity = new StringEntity(jEvent, HTTP.UTF_8);
		entity.setContentType("utf-8");
		post.setEntity(entity);
		Log.d(TAG + " -- Event Parameter", jEvent);
		return post;
	}

	private void fetchLayoutComponents() {

		Log.d(TAG, "fetchLayoutComponents");

		etCrEvTitle = (EditText) findViewById(R.id.etCrEvTitle);
		spCrEvCategory = (Spinner) findViewById(R.id.spCrEvCategory);
		etCrEvDesc = (EditText) findViewById(R.id.etCrEvDesc);
		etCrEvStartDate = (EditText) findViewById(R.id.etCrEvStartDate);
		etCrEvStartTime = (EditText) findViewById(R.id.etCrEvStartTime);
		etCrEvEndDate = (EditText) findViewById(R.id.etCrEvEndDate);
		etCrEvEndTime = (EditText) findViewById(R.id.etCrEvEndTime);

		etCrEvTitle.addTextChangedListener(new TextWatcherCustom(etCrEvTitle));
		etCrEvDesc.addTextChangedListener(new TextWatcherCustom(etCrEvDesc));
		etCrEvStartDate.addTextChangedListener(new TextWatcherCustom(
				etCrEvStartDate));
		etCrEvStartTime.addTextChangedListener(new TextWatcherCustom(
				etCrEvStartTime));
		etCrEvEndDate.addTextChangedListener(new TextWatcherCustom(
				etCrEvEndDate));
		etCrEvEndTime.addTextChangedListener(new TextWatcherCustom(
				etCrEvEndTime));

		btnCrEvStartDate = (Button) findViewById(R.id.btnCrEvStartDate);
		btnCrEvStartTime = (Button) findViewById(R.id.btnCrEvStartTime);
		btnCrEvEndDate = (Button) findViewById(R.id.btnCrEvEndDate);
		btnCrEvEndTime = (Button) findViewById(R.id.btnCrEvEndTime);
		btnCrEvShowAddrDialog = (Button) findViewById(R.id.btnCrEvShowAddrDialog);
		btnCrEvSetThumbnail = (Button) findViewById(R.id.btnCrEvSetThumbnail);
		btnCrEvCreate = (Button) findViewById(R.id.btnCrEvCreate);

		btnCrEvShowAddrDialog.setOnClickListener((OnClickListener) this);
		btnCrEvCreate.setOnClickListener((OnClickListener) this);

		btnCrEvStartDate.setOnClickListener((OnClickListener) this);
		btnCrEvStartTime.setOnClickListener((OnClickListener) this);
		btnCrEvEndDate.setOnClickListener((OnClickListener) this);
		btnCrEvEndTime.setOnClickListener((OnClickListener) this);
		btnCrEvSetThumbnail.setOnClickListener((OnClickListener) this);

		spCrEvCategory.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				event.setCategory(((JsonResponseAbstract) spCrEvCategory
						.getAdapter().getItem(arg2)).getCdAutocomplete());

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		fillCategorySpinner();

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnCrEvShowAddrDialog:
			openDialog();
			break;
		case R.id.btnCrEvCreate:
			CreateRequestResult[] createRequestResult;
			if (checkFieldsBeforeCreate()) {
				try {

					HttpAdapter httpAdapter = new HttpAdapter(
							getApplicationContext());
					createRequestResult = (CreateRequestResult[]) httpAdapter
							.doHttpRequest(prepareRequestBody(),
									getString(R.string.ws_events_create), null,
									CreateRequestResult[].class);
					Toast.makeText(getApplicationContext(),
							createRequestResult[0].getStatus(),
							Toast.LENGTH_SHORT).show();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			break;
		case R.id.btnCrEvStartDate:
			showAndSaveDatePicker(etCrEvStartDate);
			break;
		case R.id.btnCrEvStartTime:
			showAndSaveTimePicker(etCrEvStartTime);
			break;
		case R.id.btnCrEvEndDate:
			showAndSaveDatePicker(etCrEvEndDate);
			break;

		case R.id.btnCrEvEndTime:
			showAndSaveTimePicker(etCrEvEndTime);
			break;
		case R.id.btnCrEvSetThumbnail:
			Intent intent = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

			startActivityForResult(intent, RESULT_LOAD_IMAGE);
			break;
		default:
			// (User) ( (Spinner) findViewById(R.id.user) ).getSelectedItem();
			break;
		}
	}

	private boolean checkFieldsBeforeCreate() {
		boolean flag = true;
		etCrEvTitle = (EditText) findViewById(R.id.etCrEvTitle);
		spCrEvCategory = (Spinner) findViewById(R.id.spCrEvCategory);
		etCrEvDesc = (EditText) findViewById(R.id.etCrEvDesc);
		etCrEvStartDate = (EditText) findViewById(R.id.etCrEvStartDate);
		etCrEvStartTime = (EditText) findViewById(R.id.etCrEvStartTime);
		etCrEvEndDate = (EditText) findViewById(R.id.etCrEvEndDate);
		etCrEvEndTime = (EditText) findViewById(R.id.etCrEvEndTime);

		if (etCrEvTitle.getText().toString().trim().equalsIgnoreCase("")) {
			etCrEvTitle.setError("This field can not be blank");
			flag = false;
		}
		if (etCrEvDesc.getText().toString().trim().equalsIgnoreCase("")) {
			etCrEvDesc.setError("This field can not be blank");
			flag = false;
		}
		if (etCrEvStartDate.getText().toString().trim().equalsIgnoreCase("")) {
			etCrEvStartDate.setError("This field can not be blank");
			flag = false;
		}
		if (etCrEvStartTime.getText().toString().trim().equalsIgnoreCase("")) {
			etCrEvStartTime.setError("This field can not be blank");
			flag = false;
		}
		if (etCrEvEndDate.getText().toString().trim().equalsIgnoreCase("")) {
			etCrEvEndDate.setError("This field can not be blank");
			flag = false;
		}
		if (etCrEvEndTime.getText().toString().trim().equalsIgnoreCase("")) {
			etCrEvEndTime.setError("This field can not be blank");
			flag = false;
		}

		if (event.getAddress() == null || event.getCity() == 0
				|| event.getCounty() == 0) {
			btnCrEvShowAddrDialog.setText("Set Address (Must be set !!!)");
			flag = false;
		}
		if (((JsonResponseAbstract) spCrEvCategory.getSelectedItem())
				.getCdAutocomplete() == 0) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Category must be selected!")
					.setCancelable(false)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									// do things
								}
							});
			AlertDialog alert = builder.create();
			alert.show();
			flag = false;
		}
		return flag;
	}

	private void fillCategorySpinner() {
		ArrayList<JsonResponseAbstract> resultList = null;
		JsonResponseAbstract[] jsonResponseAbstracts;
		try {
			HttpAdapter httpAdapter = new HttpAdapter(getApplicationContext());
			jsonResponseAbstracts = (JsonResponseAbstract[]) httpAdapter
					.doHttpRequest(new HttpGet(),
							((Activity) CreateEventActivity.this)
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

			spCrEvCategory.setAdapter(categoryAdapter);
			spCrEvCategory.setSelection(categoryAdapter.getCount() - 1);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void openDialog() {

		/*********************************************
		 * Address Dialog Layout Component Variables *
		 * *******************************************/
		final EditText etDiAdAddress;
		final AutoCompleteTextView acDiAdCity;
		final AutoCompleteTextView acDiAdCounty;
		Button btnDiAdCreate;

		/************************************
		 * AutoCompleteAdapters for Address *
		 * **********************************/
		final AutoCompleteAdapter acCityAdapter;
		final AutoCompleteAdapter acCountyAdapter;

		final Dialog dialog = new Dialog((Activity) CreateEventActivity.this);
		dialog.setContentView(R.layout.dialog_address);
		dialog.setTitle("Address Dialog");

		// fetch layout components
		etDiAdAddress = (EditText) dialog.findViewById(R.id.etDiAdAddress);
		acDiAdCity = (AutoCompleteTextView) dialog
				.findViewById(R.id.acDiAdCity);
		acDiAdCounty = (AutoCompleteTextView) dialog
				.findViewById(R.id.acDiAdCounty);
		btnDiAdCreate = (Button) dialog.findViewById(R.id.btnDiAdCreate);

		// --------------------------COUNTY Autocomplete Adapter and OnItemClick
		// Settings-------------------
		acCountyAdapter = new AutoCompleteAdapter(dialog.getContext(),
				R.string.ws_autocomplete_county, new String[] { event.getCity()
						+ "" });
		acDiAdCounty.setAdapter(acCountyAdapter);
		acDiAdCounty.setThreshold(10000);
		acDiAdCounty.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				JsonResponseAbstract county = acCountyAdapter
						.getSelectedItem(position);
				event.setCounty(county.getCdAutocomplete());

				Log.d(TAG, "acDiAdCounty.setOnItemClickListener");
			}
		});

		// --------------------------CITY Autocomplete Adapter and OnItemClick
		// Settings-------------------
		acCityAdapter = new AutoCompleteAdapter(dialog.getContext(),
				R.string.ws_autocomplete_city, null);
		acDiAdCity.setAdapter(acCityAdapter);
		acDiAdCity.setThreshold(2);
		acDiAdCity.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				JsonResponseAbstract city = acCityAdapter
						.getSelectedItem(position);
				event.setCity(city.getCdAutocomplete());
				acCountyAdapter.setRequestParams(new String[] { event.getCity()
						+ "" });
				acDiAdCounty.setThreshold(1);

				Log.d(TAG, "acDiAdCity.setOnItemClickListener");
			}
		});

		// ----------------------------DIALOG BUTTON
		// Create-------------------------------
		btnDiAdCreate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				boolean flag = true;

				if (etDiAdAddress.getText().toString().trim()
						.equalsIgnoreCase("")) {
					etDiAdAddress.setError("This field can not be blank");
					flag = false;
				}
				if (acDiAdCity.getText().toString().trim().equalsIgnoreCase("")) {
					acDiAdCity.setError("City must be selected");
					flag = false;
				}
				if (acDiAdCounty.getText().toString().trim()
						.equalsIgnoreCase("")) {
					acDiAdCounty.setError("County must be selected");
					flag = false;
				}

				if (flag) {
					event.setAddress(etDiAdAddress.getText().toString());
					btnCrEvShowAddrDialog.setText("Address has ben set");
					dialog.dismiss();
				}
			}
		});

		dialog.show();
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
						et.setText(dateFormat.format(new Date(yearA - 1900,
								monthOfYear, dayOfMonth)));

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
						et.setText(timeFormat.format(new Date(0, 0, 0,
								hourOfDay, minute, 0)));
					}
				}, hour, minute, false);
		tpd.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
				&& null != data) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String picturePath = cursor.getString(columnIndex);
			cursor.close();

			ImageView imageView = (ImageView) findViewById(R.id.ivCrEvThumbnail);
			Bitmap bitmap = shrinkBitmap(picturePath,100,100);
			imageView.setImageBitmap(bitmap);
			event.setThumbnail(encodeTobase64(bitmap));

			longInfo(event.getThumbnail());
		}

	}

	Bitmap shrinkBitmap(String file, int width, int height) {

		BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
		bmpFactoryOptions.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);

		int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight
				/ (float) height);
		int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth
				/ (float) width);

		if (heightRatio > 1 || widthRatio > 1) {
			if (heightRatio > widthRatio) {
				bmpFactoryOptions.inSampleSize = heightRatio;
			} else {
				bmpFactoryOptions.inSampleSize = widthRatio;
			}
		}

		bmpFactoryOptions.inJustDecodeBounds = false;
		bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);
		return bitmap;
	}

	public static void longInfo(String str) {
		if (str.length() > 4000) {
			Log.d("--response", str.substring(0, 4000));
			longInfo(str.substring(4000));
		} else
			Log.d("--response", str);
	}

	private String encodeTobase64(Bitmap image) {
		// RegistrationDetailToUpload.getInstance().mIsImageAttached = true;
		Bitmap immagex = image;
		if (image != null) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			byte[] b = baos.toByteArray();
			String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
			return imageEncoded;
		} else
			return null;
	}

}
