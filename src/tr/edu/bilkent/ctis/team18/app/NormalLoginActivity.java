package tr.edu.bilkent.ctis.team18.app;

import java.io.UnsupportedEncodingException;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import tr.edu.bilkent.ctis.team18.adapters.DBAdapter;
import tr.edu.bilkent.ctis.team18.adapters.HttpAdapter;
import tr.edu.bilkent.ctis.team18.model.JsonResponseAbstract;
import tr.edu.bilkent.ctis.team18.model.User;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

public class NormalLoginActivity extends Activity implements OnClickListener {

	
	private Button btnNrLoLogin;
	private EditText etNrLoPassword;
	private EditText etNrLoLoginName;
	private Intent intent;
	private DBAdapter db;
	
	User user = new User();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.normal_login);
		fetchLayoutComponents();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.btnNrLoLogin:
			User[] userArr;
			user.setPassword(etNrLoPassword.getText().toString());
			user.setLoginName(etNrLoLoginName.getText().toString());
			HttpAdapter httpAdapter = new HttpAdapter(getApplicationContext());
			try {
				userArr = (User[]) httpAdapter
						.doHttpRequest(prepareRequestBody(),
								getString(R.string.ws_users_authenticate), null,
								User[].class);
				Toast.makeText(getApplicationContext(), ((JsonResponseAbstract)userArr[0]).getNmRequestStatus(), Toast.LENGTH_SHORT).show();
				if(((JsonResponseAbstract)userArr[0]).getNmRequestStatus().equalsIgnoreCase("successful")){
					this.user = userArr[0];
					Intent intent = new Intent(NormalLoginActivity.this,MainActivity.class);
					intent.putExtra("user", user);
					startActivity(intent);
					finish();
				}
				
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		default:
			break;
		}

	}
	
	private void fetchLayoutComponents(){
		btnNrLoLogin = (Button)findViewById(R.id.btnNrLoLogin);
		etNrLoLoginName = (EditText)findViewById(R.id.etNrLoLoginName);
		etNrLoPassword = (EditText)findViewById(R.id.etNrLoPassword);
		btnNrLoLogin.setOnClickListener(this);
	}

	private void updatePersonInfo() {

		
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
