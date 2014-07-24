package tr.edu.bilkent.ctis.team18.app;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import tr.edu.bilkent.ctis.team18.adapters.DBAdapter;
import tr.edu.bilkent.ctis.team18.model.User;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;
import com.google.gson.Gson;

public class LoginActivity extends Activity implements OnClickListener {

	private String APP_ID = "347798612038640";
	private Button btnLogin;
	private Button btnFacebookLogin;
	private Button btnSignIn;
	private Intent intent;
	private DBAdapter db;
	Facebook fb;
	User user = new User();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.login);
		fb =  new Facebook(APP_ID);
		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnFacebookLogin = (Button) findViewById(R.id.btnFacebookLogin);
		btnSignIn = (Button) findViewById(R.id.btnSignIn);
		
		
		btnSignIn.setOnClickListener(this);
		btnLogin.setOnClickListener(this);
		btnFacebookLogin.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.btnFacebookLogin:
			if (fb.isSessionValid()) {
				try {
					fb.logout(getApplicationContext());
					btnFacebookLogin.setText("Logout");
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			else {
				fb.authorize(LoginActivity.this, new DialogListener() {

					@Override
					public void onFacebookError(FacebookError e) {
						// TODO Auto-generated method stub
						Toast.makeText(LoginActivity.this, "An error occured while retreiving user info from facebook",
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onError(DialogError e) {
						// TODO Auto-generated method stub
						Toast.makeText(LoginActivity.this, "An erro occured in application",
								Toast.LENGTH_SHORT).show();
					}

					// If successfully logged in, then this method works.
					@Override
					public void onComplete(Bundle values) {
						updatePersonInfo();

					}

					@Override
					public void onCancel() {
						// TODO Auto-generated method stub
						Toast.makeText(LoginActivity.this, "Facebook login cancelled",
								Toast.LENGTH_SHORT).show();

					}
				});
			}
			break;
		case R.id.btnLogin:
			Intent intent = new Intent(LoginActivity.this,
					NormalLoginActivity.class);
			startActivity(intent);
			finish();
			break;

		case R.id.btnSignIn:
			Intent intent2 = new Intent(LoginActivity.this,
					CreateUserActivity.class);
			startActivity(intent2);
			break;
		default:
			break;
		}

	}

	private void updatePersonInfo() {

		Thread t = new Thread(new Runnable() {
			public void run() {
				JSONObject obj = null;
				URL imgURL = null;

				try {

					String jsonUser = fb.request("me");
					obj = Util.parseJson(jsonUser);

					String id = obj.optString("id");
					String name = obj.optString("name");
					user.setName(name);

					intent = new Intent(LoginActivity.this, MainActivity.class);
					intent.putExtra("user", user);
					startActivity(intent);
					finish();

				} catch (FacebookError e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		t.start();

	}

	private HttpPost prepareRequestBody()
			throws UnsupportedEncodingException {
		
		String jEvent = new Gson().toJson(user);
		HttpPost post = new HttpPost();
		post.setHeader("Content-type", "application/json");
		post.setEntity(new StringEntity(jEvent));
		Log.d("Event Parameter", jEvent);
		return post;
	}
}
