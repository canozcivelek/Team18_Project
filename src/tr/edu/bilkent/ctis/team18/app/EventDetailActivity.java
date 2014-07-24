package tr.edu.bilkent.ctis.team18.app;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;

import tr.edu.bilkent.ctis.team18.adapters.HttpAdapter;
import tr.edu.bilkent.ctis.team18.adapters.ListCommentAdapter;
import tr.edu.bilkent.ctis.team18.model.Comment;
import tr.edu.bilkent.ctis.team18.model.Event;
import tr.edu.bilkent.ctis.team18.model.JsonResponseAbstract;
import tr.edu.bilkent.ctis.team18.model.User;
import tr.edu.bilkent.ctis.team18.util.IntentConstants;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.GsonBuilder;

public class EventDetailActivity extends Activity implements OnClickListener,
		OnItemClickListener {

	static final String TAG = "EventDetailActivity";

	private Comment[] comments;

	private Event event;
	private int eventCode;
	private User user;
	private Button btnEvDeAttend;
	private Button btnEvDeLike;
	private Button btnEvDeUnlike;
	private Button btnEvDeComments;

	private ListView listView;
	private EditText etEvDeComment;
	private ImageView ivEvDeEndThumbnail;
	
	private LinearLayout linearEvDeDelete;
	private Button btnEvDeDelete;

	private boolean isUserAttend = false;
	private boolean isUserLike = false;
	private boolean isUserUnlike = false;

	private PopupWindow popWindow;
	private ListCommentAdapter commentAdapter;
	private int deviceHeight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.event_detail);
		eventCode = getIntent().getIntExtra(IntentConstants.EVENT_CODE, 0);
		user = (User) getIntent().getSerializableExtra(IntentConstants.USER);
		updateEventValues();
		fetchLayoutComponents();
		fillLayoutComponent();
	}

	private void fetchLayoutComponents() {
		ivEvDeEndThumbnail = (ImageView) findViewById(R.id.ivEvDeEndThumbnail);
		btnEvDeAttend = (Button) findViewById(R.id.btnEvDeAttend);
		btnEvDeLike = (Button) findViewById(R.id.btnEvDeLike);
		btnEvDeUnlike = (Button) findViewById(R.id.btnEvDeUnlike);
		btnEvDeComments = (Button) findViewById(R.id.btnEvDeComments);
		btnEvDeDelete = (Button) findViewById(R.id.btnEvDeDelete);
		
		linearEvDeDelete  = (LinearLayout) findViewById(R.id.linearEvDeDelete);
		if(user.getCode()==eventCode)
			linearEvDeDelete.setVisibility(View.VISIBLE);
		
		btnEvDeDelete.setOnClickListener(this);
		btnEvDeAttend.setOnClickListener(this);
		btnEvDeUnlike.setOnClickListener(this);
		btnEvDeLike.setOnClickListener(this);
		btnEvDeComments.setOnClickListener(this);
		

	}

	private void fillLayoutComponent() {

		int resourceId;
		String code;

		((TextView) findViewById(R.id.tvEvDeTitle)).setText(event.getTitle());
		((TextView) findViewById(R.id.tvEvDeRate)).setText(String.format( "%.2f", Double.parseDouble(event.getRate())*100));
		((TextView) findViewById(R.id.tvEvDeDesc)).setText(event
				.getExplanation());

		resourceId = R.string.ws_nicename_category;
		code = event.getCategory() + "";
		((TextView) findViewById(R.id.tvEvDeCategory))
				.setText(getNiceNameByCode(resourceId, code));

		resourceId = R.string.ws_nicename_city;
		code = event.getCity() + "";
		String city = getNiceNameByCode(resourceId, code);

		resourceId = R.string.ws_nicename_county;
		code = event.getCounty() + "";
		String county = getNiceNameByCode(resourceId, code);
		((TextView) findViewById(R.id.tvEvDeAddress)).setText(event
				.getAddress() + "\n" + city + " / " + county);
		
		((TextView) findViewById(R.id.tvEvDeStartDate)).setText(event
				.getStartDate() + " - " + event.getStartTime());
		((TextView) findViewById(R.id.tvEvDeEndDate)).setText(event
				.getFinishDate() + " - " + event.getFinishTime());

		if (!event.getThumbnail().equals("") && event.getThumbnail() != null) {
			byte[] imageAsBytes = Base64.decode(
					event.getThumbnail().getBytes(), Base64.DEFAULT);
			ivEvDeEndThumbnail.setImageBitmap(BitmapFactory.decodeByteArray(
					imageAsBytes, 0, imageAsBytes.length));
		} else
			ivEvDeEndThumbnail.setImageResource(R.drawable.default_event);

		setBtnIsUserLikeOrAttend();

	}

	private void updateButtonText() {

		String attend = isUserAttend ? "Give Up" : "Attend";
		String like = isUserLike ? "You liked" : "Like";
		String unlike = isUserUnlike ? "You unliked" : "Unlike";

		btnEvDeAttend.setText(attend + " (" + event.getNumOfAttend() + ")");
		btnEvDeLike.setText(like + " (" + event.getNumOfLike() + ")");
		btnEvDeUnlike.setText(unlike + " (" + event.getNumOfUnlike() + ")");
		((TextView) findViewById(R.id.tvEvDeRate)).setText(String.format( "%.2f", Double.parseDouble(event.getRate())));
	}

	private void setBtnIsUserLikeOrAttend() {
		String[] params = { event.getCode() + "", user.getCode() + "" };

		JsonResponseAbstract[] events = null;
		try {
			HttpAdapter httpAdapter = new HttpAdapter(getApplicationContext());
			events = (JsonResponseAbstract[]) httpAdapter.doHttpRequest(
					new HttpGet(), getString(R.string.ws_events_is_attend),
					params, JsonResponseAbstract[].class);
			if (events.length == 0)
				isUserAttend = false;
			else
				isUserAttend = true;

			HttpAdapter httpAdapter2 = new HttpAdapter(getApplicationContext());
			events = (JsonResponseAbstract[]) httpAdapter2.doHttpRequest(
					new HttpGet(), getString(R.string.ws_events_is_like),
					params, JsonResponseAbstract[].class);
			if (events.length == 0)
				isUserLike = false;
			else
				isUserLike = true;
			
			HttpAdapter httpAdapter3 = new HttpAdapter(getApplicationContext());
			events = (JsonResponseAbstract[]) httpAdapter3.doHttpRequest(
					new HttpGet(), getString(R.string.ws_events_is_unlike),
					params, JsonResponseAbstract[].class);
			if (events.length == 0)
				isUserUnlike = false;
			else
				isUserUnlike = true;

		} catch (Exception e) {
			e.printStackTrace();
		}
		updateButtonText();
	}

	private String getNiceNameByCode(int serviceResId, String code) {
		String[] params = { code };
		HttpAdapter httpAdapter = new HttpAdapter(getApplicationContext());
		JsonResponseAbstract[] jsonResponseAbstracts = null;
		try {
			jsonResponseAbstracts = (JsonResponseAbstract[]) httpAdapter
					.doHttpRequest(new HttpGet(), getString(serviceResId),
							params, JsonResponseAbstract[].class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonResponseAbstracts[0].getNmAutocomplete();
	}

	private void updateAttendOrLike(int serviceResId) {
		String[] params = { event.getCode() + "", user.getCode() + "" };
		HttpAdapter httpAdapter = new HttpAdapter(getApplicationContext());
		Event[] events = null;
		try {
			events = (Event[]) httpAdapter.doHttpRequest(new HttpGet(),
					getString(serviceResId), params, Event[].class);
			event = events[0];

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void updateEventValues() {
		String[] params = { eventCode + "" };
		HttpAdapter httpAdapter = new HttpAdapter(getApplicationContext());
		Event[] events = null;
		try {
			events = (Event[]) httpAdapter.doHttpRequest(new HttpGet(),
					getString(R.string.ws_events_list), params, Event[].class);
			event = events[0];

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.btnEvDeAttend:
			updateAttendOrLike(R.string.ws_events_attend);
			break;
		case R.id.btnEvDeLike:
			updateAttendOrLike(R.string.ws_events_like);
			break;
		case R.id.btnEvDeUnlike:
			updateAttendOrLike(R.string.ws_events_unlike);
			break;
		case R.id.btnEvDeComments:
			onShowPopup();
			break;
		case R.id.btnEvDeAddComment:
			addComment();
			break;
		case R.id.btnEvDeDelete:
			onDeleteEvent();
			break;
		default:
			break;
		}
		setBtnIsUserLikeOrAttend();
	}
	
	private void onDeleteEvent(){
		new AlertDialog.Builder(this)
		.setIcon(android.R.drawable.ic_dialog_alert)
		.setTitle("Deleting Event")
		.setMessage("Are you sure you want to delete this event?")
		.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						deleteEvent();
					}

				}).setNegativeButton("No", null).show();
	}
	private void deleteEvent(){
		JsonResponseAbstract responseAbstract[];

		HttpAdapter httpAdapter = new HttpAdapter(getApplicationContext());
		String params[] = { eventCode+""};
		try {
			responseAbstract = httpAdapter.doHttpRequest(new HttpDelete(),
					getString(R.string.ws_events_delete), params,
					JsonResponseAbstract[].class);
			Toast.makeText(getApplicationContext(), responseAbstract[0].getNmRequestStatus(), Toast.LENGTH_SHORT).show();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finish();
	}

	// call this method when required to show popup
	public void onShowPopup() {

		LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// inflate the custom popup layout
		final View inflatedView = layoutInflater.inflate(
				R.layout.comment_popup_layout, null, false);
		// find the ListView in the popup layout
		listView = (ListView) inflatedView.findViewById(R.id.commentsListView);
		listView.setOnItemClickListener((OnItemClickListener) this);

		Button btnEvDeAddComment = (Button) inflatedView
				.findViewById(R.id.btnEvDeAddComment);
		btnEvDeAddComment.setOnClickListener((OnClickListener) this);

		etEvDeComment = (EditText) inflatedView
				.findViewById(R.id.etEvDeComment);

		// get device size
		Display display = getWindowManager().getDefaultDisplay();
		final Point size = new Point();
		display.getSize(size);
		deviceHeight = size.y;

		// fill the data to the list items
		setSimpleList();

		LinearLayout layout = new LinearLayout(listView.getContext());
		layout.clearFocus();

		// set height depends on the device size
		popWindow = new PopupWindow(inflatedView, size.x - 50, size.y - 350,
				true);

		// make it focusable to show the keyboard to enter in `EditText`
		popWindow
				.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		popWindow.setFocusable(true);
		popWindow.update();

		// set a background drawable with rounders corners
		popWindow.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.comment_popup_bg));

		// make it outside touchable to dismiss the popup window
		popWindow.setOutsideTouchable(true);

		// show the popup at bottom of the screen and set some margin at bottom
		// ie,

		popWindow.showAtLocation(layout, Gravity.BOTTOM, 0, 100);

		popWindow.setFocusable(true);
		popWindow.update();
	}

	void setSimpleList() {

		HttpAdapter httpAdapter = new HttpAdapter(getApplicationContext());
		String params[] = { event.getCode() + "" };
		try {
			comments = (Comment[]) httpAdapter.doHttpRequest(new HttpGet(),
					getString(R.string.ws_events_comment_list), params,
					Comment[].class);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		commentAdapter = new ListCommentAdapter(this, comments, user);

		listView.setAdapter(commentAdapter);
	}

	private void addComment() {
		HttpAdapter httpAdapter = new HttpAdapter(getApplicationContext());
		try {
			if (etEvDeComment.getText().toString().trim().equalsIgnoreCase("")) {
				etEvDeComment.setHint("This field can not be blank!");

			} else {
				comments = (Comment[]) httpAdapter.doHttpRequest(
						prepareAddCommentRequestBody(),
						getString(R.string.ws_events_comment_add), null,
						Comment[].class);
				commentAdapter = new ListCommentAdapter(this, comments, user);

				listView.setAdapter(commentAdapter);
				listView.invalidateViews();
				etEvDeComment.setText("");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, final int position,
			long id) {
		// TODO Auto-generated method stub
		new AlertDialog.Builder(this)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle("Deleting Comment")
				.setMessage("Are you sure you want to delete this comment?")
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								deleteComment(position);
							}

						}).setNegativeButton("No", null).show();

	}

	private void deleteComment(int position) {
		Comment comment = (Comment) commentAdapter.getItem(position);

		HttpAdapter httpAdapter = new HttpAdapter(getApplicationContext());
		String params[] = { comment.getCode() + "", comment.getEventCode() + "" };
		try {
			comments = (Comment[]) httpAdapter.doHttpRequest(new HttpGet(),
					getString(R.string.ws_events_comment_delete), params,
					Comment[].class);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		commentAdapter = new ListCommentAdapter(this, comments, user);

		listView.setAdapter(commentAdapter);
	}

	private HttpPost prepareAddCommentRequestBody()
			throws UnsupportedEncodingException {
		Comment comment = new Comment();

		comment.setOwnerCode(user.getCode());
		comment.setEventCode(event.getCode());

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		comment.setDate(dateFormat.format(date));
		comment.setText(etEvDeComment.getText().toString());

		String jComment = new GsonBuilder().disableHtmlEscaping().create()
				.toJson(comment);
		HttpPost post = new HttpPost();

		post.setHeader("Content-Type", "application/json;charset=utf-8");
		StringEntity entity = new StringEntity(jComment, HTTP.UTF_8);
		entity.setContentType("utf-8");
		post.setEntity(entity);
		Log.d(TAG + " -- Comment Parameter", jComment);
		return post;
	}

}
