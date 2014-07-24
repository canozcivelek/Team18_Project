package tr.edu.bilkent.ctis.team18.adapters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.client.methods.HttpGet;

import tr.edu.bilkent.ctis.team18.app.R;
import tr.edu.bilkent.ctis.team18.model.JsonResponseAbstract;
import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

public class AutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {
	private List<JsonResponseAbstract> resultList;
	private List<String> suggestion;
	private HttpAdapter httpAdapter;
	private Context context;
	private int serviceResourceId;
	private String[] requestParams;
	
	public AutoCompleteAdapter(Context context, int serviceResourceId, String[] requestParams) {
		super(context, R.layout.autocomplete);
		this.context = context;
		this.suggestion = new ArrayList<String>();
		this.serviceResourceId = serviceResourceId;
		this.requestParams = requestParams;
	}

	@Override
	public int getCount() {
		return resultList.size();
	}

	@Override
	public String getItem(int index) {
		return resultList.get(index).getNmAutocomplete();
	}

	@Override
	public Filter getFilter() {
		Filter filter = new Filter() {
			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				FilterResults filterResults = new FilterResults();
				if (constraint != null) {
					// Retrieve the autocomplete results.
					resultList = autocomplete(constraint.toString());
					suggestion.clear();
					for (int i = 0; i < resultList.size(); i++)
						suggestion.add(resultList.get(i).getNmAutocomplete());
					// Assign the data to the FilterResults
					filterResults.values = suggestion;
					filterResults.count = suggestion.size();
				}
				return filterResults;
			}

			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
				if (results != null && results.count > 0) {

					for (String s : (List<String>) results.values) {
						add(s);
					}
					notifyDataSetChanged();
				} else {
					notifyDataSetInvalidated();
				}
			}
		};
		return filter;
	}

	private List<JsonResponseAbstract> autocomplete(String input) {
		List<JsonResponseAbstract> resultList = null;
		JsonResponseAbstract[] jsonResponseAbstracts;
		httpAdapter = new HttpAdapter(context);
		String[] restriction = { input };

		String[] params = concatAll(restriction, requestParams);
		try {

			jsonResponseAbstracts = (JsonResponseAbstract[]) httpAdapter
					.doHttpRequest(new HttpGet(),
							context.getString(serviceResourceId), params,
							JsonResponseAbstract[].class);
			resultList = Arrays.asList(jsonResponseAbstracts);

			Log.d("Autocomplete Request Result",
					jsonResponseAbstracts[0].getNmAutocomplete());
			Log.d("Autocomplete Request Result", input);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultList;
	}

	public JsonResponseAbstract getSelectedItem(int position) {
		return resultList.get(position);
	}

	public static <T> T[] concatAll(T[] first, T[]... rest) {
		int totalLength = first.length;
		for (T[] array : rest) {
			if(array !=null)
				totalLength += array.length;
		}
		T[] result = Arrays.copyOf(first, totalLength);
		int offset = first.length;
		for (T[] array : rest) {
			if(array!=null){
				System.arraycopy(array, 0, result, offset, array.length);
				offset += array.length;
			}
		}
		return result;
	}

	public String[] getRequestParams() {
		return requestParams;
	}

	public void setRequestParams(String[] requestParams) {
		this.requestParams = requestParams;
	}
	
	
}