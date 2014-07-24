package tr.edu.bilkent.ctis.team18.adapters;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;

import tr.edu.bilkent.ctis.team18.app.R;
import tr.edu.bilkent.ctis.team18.model.JsonResponseAbstract;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

public class HttpAdapter extends AsyncTask<HttpRequestBase, String, String> {

	static final String TAG="HttpAdapter";
	private String URL = null;
	private Context context;
	
	public HttpAdapter(Context context){
		URL = context.getString(R.string.ws_webservice_ip_emulator);
	}
	
	@Override
	public String doInBackground(HttpRequestBase... httpRequestType) {
		HttpClient httpclient = new DefaultHttpClient();
		HttpResponse response;
		String responseString = null;
		try {
			response = httpclient.execute(httpRequestType[0]);
			StatusLine statusLine = response.getStatusLine();
			if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				out.close();
				responseString = out.toString();
			} else {
				// Closes the connection.
				response.getEntity().getContent().close();
				throw new IOException(statusLine.getReasonPhrase());
			}
		} catch (ClientProtocolException e) {
			try {
				throw e;
			} catch (ClientProtocolException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (IOException e) {
			try {
				throw e;
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		return responseString;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
	}

	public JsonResponseAbstract[] doHttpRequest(HttpRequestBase httpRequestType,
			String serviceName, String[] params,
			Class<? extends JsonResponseAbstract[]> clazz) throws Exception {

		String tempUrl = URL + serviceName;

		if (params != null){
			String paramStr = "";
			for (int i = 0; i < params.length; i++)
				if(params[i]!=null)
					tempUrl += "/" + params[i].replace(" ", "+");
				
		}
		
		httpRequestType.setURI(new URI(tempUrl));

		
		Log.d(TAG, tempUrl);
		String response = this.execute(httpRequestType).get();
		Log.d(TAG+"--response", response);
		
		return new Gson().fromJson(response, clazz);
	}
	
	

}
