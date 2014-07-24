/* LayoutInflator:	When we design using XML, all of our UI elements are just tags and
 * 					parameters. Before we can use these UI elements, (e.g., a TextView
 * 					or LinearLayout), we need to create the actual objects corresponding
 * 					to these xml elements. That is what the inflater is for. The inflater,
 * 					uses these tags and their corresponding parameters to create the actual
 * 					objects and set all the parameters. After this one can get a reference 
 * 					to the UI element using findViewById().
 * getView():		getView() is called every time an item in the list is drawn. Now, before
 * 					the item can be drawn, it has to be created. convertView basically is the
 * 					last used view to draw an item. In getView() we inflate the xml first and
 * 					then use findByViewID() to get the various UI elements of the Listitem.
 * 					When we check for (convertView == null) what we do is check that if a 
 * 					view is null(for the first item) then create it, else, if it already 
 * 					exists, reuse it, no need to go through the inflate process again. 
 * 					It makes it a lot more efficient.
 * parent:			The parent is a ViewGroup to which your view created by getView() is 
 * 					finally attached. In our case this would be the ListView.
 * 
 * URL:				http://developer.android.com/reference/android/widget/BaseAdapter.html
 */
package tr.edu.bilkent.ctis.team18.adapters;

import java.util.Arrays;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import tr.edu.bilkent.ctis.team18.app.R;
import tr.edu.bilkent.ctis.team18.model.Event;
import tr.edu.bilkent.ctis.team18.model.JsonResponseAbstract;
import tr.edu.bilkent.ctis.team18.model.ListItemAbstract;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListEventAdapter extends BaseAdapter {
	private List<? extends JsonResponseAbstract> data;
	private Context context;

	public ListEventAdapter(Context context, JsonResponseAbstract[] data) {
		this.context = context;
		this.data = Arrays.asList(data);
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View rowView = convertView;

		if (rowView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.listview_item, null);
		}

		ImageView ivEvListThumb = (ImageView) rowView
				.findViewById(R.id.ivEvListThumb);
		TextView tvEvListTitle = (TextView) rowView.findViewById(R.id.tvEvListTitle);
		TextView tvEvListDesc = (TextView) rowView.findViewById(R.id.tvEvListDesc);
		TextView tvEvListAttendLike = (TextView) rowView.findViewById(R.id.tvEvListAttendLike);
		TextView tvEvListElapsedTime = (TextView) rowView.findViewById(R.id.tvEvListElapsedTime);
		
		// Get individual object from ArrayList<ListData> and set ListView items
		ListItemAbstract tempData = (ListItemAbstract) data.get(position);
		if (tempData instanceof Event) {
			Event event = (Event)tempData;
			if (!event.getThumbnail().equals("")&&event.getThumbnail() != null) {
				byte[] imageAsBytes = Base64.decode(event
						.getThumbnail().getBytes(), Base64.DEFAULT);
				ivEvListThumb.setImageBitmap(BitmapFactory.decodeByteArray(
						imageAsBytes, 0, imageAsBytes.length));
			}
			else
				ivEvListThumb.setImageResource(R.drawable.default_event);
			tvEvListTitle.setText(event.getTitle());
			tvEvListDesc.setText(event.getExplanation());
			tvEvListAttendLike.setText("rate: "+String.format( "%.2f", Double.parseDouble(event.getRate())));
			tvEvListElapsedTime.setText(getTimeElapsed(event.getStartDate()+" "+event.getStartTime()));
		}

		return rowView;
	}
	
	private String getTimeElapsed(String dateStr){
		
		//Log.d(TAG,date.getYear()+1900+" "+date.getMonth()+" "+date.getDay()+" "+date.getHours()+" "+date.getMinutes()+" "+date.getSeconds()+" "+0);
		DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		DateTime commentDate =dtf.parseDateTime(dateStr);
		
		DateTime now = new DateTime();
		Period period = null;
		
		String suffix = null;
		if(commentDate.isBeforeNow()){
			period = new Period(commentDate, now);
			suffix = " ago";
		}
		else{
			period = new Period(now,commentDate);
			suffix = " remains";
		}
			
		
		PeriodFormatter formatter = null;
		if(period.getYears()>0){
			formatter = new PeriodFormatterBuilder()
		    .appendYears().appendSuffix(" years"+suffix)
		    .printZeroNever()
		    .toFormatter();
		}
		else if(period.getMonths()>0){
			formatter = new PeriodFormatterBuilder()
			.appendMonths().appendSuffix(" months"+suffix)
		    .printZeroNever()
		    .toFormatter();
		}
		else if(period.getWeeks()>0){
			formatter = new PeriodFormatterBuilder()
			.appendWeeks().appendSuffix(" weeks"+suffix)
		    .printZeroNever()
		    .toFormatter();
		}
		else if(period.getDays()>0){
			formatter = new PeriodFormatterBuilder()
			.appendDays().appendSuffix(" days"+suffix)
		    .printZeroNever()
		    .toFormatter();
		}
		else if(period.getHours()>0){
			formatter = new PeriodFormatterBuilder()
			.appendHours().appendSuffix(" hours"+suffix)
		    .printZeroNever()
		    .toFormatter();
		}
		
		else if(period.getMinutes()>0){
			formatter = new PeriodFormatterBuilder()
			.appendMinutes().appendSuffix(" minutes"+suffix)
		    .printZeroNever()
		    .toFormatter();
		}
		
		return formatter.print(period)==null?"just now":formatter.print(period);
	}
}
