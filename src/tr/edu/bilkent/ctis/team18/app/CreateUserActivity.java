package tr.edu.bilkent.ctis.team18.app;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;

import com.google.gson.GsonBuilder;

import tr.edu.bilkent.ctis.team18.adapters.HttpAdapter;
import tr.edu.bilkent.ctis.team18.model.Comment;
import tr.edu.bilkent.ctis.team18.model.Event;
import tr.edu.bilkent.ctis.team18.model.JsonResponseAbstract;
import tr.edu.bilkent.ctis.team18.model.User;
import tr.edu.bilkent.ctis.team18.util.IntentConstants;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class CreateUserActivity extends Activity implements OnClickListener {

	static final String TAG = "CreateUserActivity";

	/********************************
	 * Layout Component Variables *
	 * ******************************/
	private EditText etCrUsrPass;
	private EditText etCrUsrUserName;
	private EditText etCrUsrPassCont;
	private EditText etCrUsrLoginName;
	

	private Button btnCrUsrCreate;
	private Button btnCrUsrCheck;

	private User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.create_user);
		this.user = (User) getIntent().getSerializableExtra("user");

		fetchLayoutComponents();

	}

	private void fetchLayoutComponents() {
		etCrUsrPass = (EditText) findViewById(R.id.etCrUsrPass);
		etCrUsrUserName = (EditText) findViewById(R.id.etCrUsrUserName);
		etCrUsrPassCont = (EditText) findViewById(R.id.etCrUsrPassCont);
		etCrUsrLoginName = (EditText) findViewById(R.id.etCrUsrLoginName);

		btnCrUsrCreate = (Button) findViewById(R.id.btnCrUsrCreate);
		btnCrUsrCheck = (Button) findViewById(R.id.btnCrUsrCheck);

		btnCrUsrCheck.setOnClickListener(this);
		btnCrUsrCreate.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnCrUsrCheck:
			if(!checkAvailable())
				etCrUsrLoginName.setError("Login name not available!");
			break;

		case R.id.btnCrUsrCreate:
			if(!etCrUsrLoginName.getText().toString().equals("")){
				if(!etCrUsrUserName.getText().toString().equals("")){
					if(!etCrUsrPass.getText().toString().equals("")){	
						if(checkAvailable()){
							if(checkPassword())
								createUser();
							else
								etCrUsrPassCont.setError("Password does not matches!");
						}else{
							etCrUsrLoginName.setError("Login name not available!");
						}
					}else{
						etCrUsrPass.setError("Password cannot be empty!");
					}
				}else{
					etCrUsrUserName.setError("Name Surname cannot be empty!");
				}
			}else{
				etCrUsrLoginName.setError("Login name cannot be empty!");
			}
			break;
		default:
			break;
		}

	}
	
	private boolean createUser(){
		String loginName = etCrUsrLoginName.getText().toString();
		String[] params = { loginName + "" };
		HttpAdapter httpAdapter = new HttpAdapter(getApplicationContext());
		JsonResponseAbstract[] response = null;
		try {
			
			User[] users = (User[]) httpAdapter.doHttpRequest(
					prepareAddCommentRequestBody(),
					getString(R.string.ws_users_create), null,
					User[].class);
			user = users[0];
			Intent intent = new Intent(CreateUserActivity.this,MainActivity.class);
			intent.putExtra("user", user);
			startActivity(intent);
			finish();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private HttpPost prepareAddCommentRequestBody()
			throws UnsupportedEncodingException {
		user = new User();

		user.setLoginName(etCrUsrLoginName.getText().toString());
		user.setName(etCrUsrUserName.getText().toString());
		user.setPassword(etCrUsrPass.getText().toString());
		

		String jComment = new GsonBuilder().disableHtmlEscaping().create()
				.toJson(user);
		HttpPost post = new HttpPost();

		post.setHeader("Content-Type", "application/json;charset=utf-8");
		StringEntity entity = new StringEntity(jComment, HTTP.UTF_8);
		entity.setContentType("utf-8");
		post.setEntity(entity);
		Log.d(TAG + " -- Comment Parameter", jComment);
		return post;
	}
	
	private boolean checkAvailable(){
		String loginName = etCrUsrLoginName.getText().toString();
		String[] params = { loginName + "" };
		HttpAdapter httpAdapter = new HttpAdapter(getApplicationContext());
		JsonResponseAbstract[] response = null;
		try {
			response = (JsonResponseAbstract[]) httpAdapter.doHttpRequest(new HttpGet(),
					getString(R.string.ws_users_check_available), params, JsonResponseAbstract[].class);
			String res = response[0].getNmRequestStatus();
			if(res.contains("Not Available"))
				return false;
			else
				return true;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private boolean checkPassword(){		
		return etCrUsrPass.getText().toString().equals(etCrUsrPassCont.getText().toString());
		
	}

}
