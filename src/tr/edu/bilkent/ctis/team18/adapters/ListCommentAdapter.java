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
import tr.edu.bilkent.ctis.team18.model.Comment;
import tr.edu.bilkent.ctis.team18.model.JsonResponseAbstract;
import tr.edu.bilkent.ctis.team18.model.ListItemAbstract;
import tr.edu.bilkent.ctis.team18.model.User;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListCommentAdapter extends BaseAdapter {
	static final String TAG = "ListCommentAdapter";
	private List<? extends JsonResponseAbstract> data;
	private Context context;
	User user;

	public ListCommentAdapter(Context context, JsonResponseAbstract[] data,
			User user) {
		this.context = context;
		this.data = Arrays.asList(data);
		this.user = user;
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
			rowView = inflater.inflate(R.layout.listview_item_comment, null);
		}

		ImageView ivListThumb = (ImageView) rowView
				.findViewById(R.id.ivComListUserIcon);

		ImageView ivComListDelete = (ImageView) rowView
				.findViewById(R.id.ivComListDelete);

		TextView tvListName = (TextView) rowView
				.findViewById(R.id.tvComListComment);

		TextView tvComListDuration = (TextView) rowView
				.findViewById(R.id.tvComListDuration);

		TextView tvComListUserName = (TextView) rowView
				.findViewById(R.id.tvComListUserName);

		// Get individual object from ArrayList<ListData> and set ListView items
		ListItemAbstract tempData = (ListItemAbstract) data.get(position);
		if (tempData instanceof Comment) {
			Comment comment = (Comment) tempData;
			if ((!comment.getOwnerThumbnail().equals(""))
					&& (comment.getOwnerThumbnail() != null)) {
				byte[] imageAsBytes = Base64.decode(comment.getOwnerThumbnail()
						.getBytes(), Base64.DEFAULT);
				ivListThumb.setImageBitmap(BitmapFactory.decodeByteArray(
						imageAsBytes, 0, imageAsBytes.length));
			} else
				ivListThumb.setImageResource(R.drawable.default_event);
			tvListName.setText(comment.getText());

			if ((comment.getDate() != null) && (!comment.getDate().equals(""))) {

				tvComListDuration.setText(getTimeElapsed(comment.getDate()));
			} else
				tvComListDuration.setText("no time");

			if (comment.getOwnerCode() != user.getCode()) {
				Log.d(TAG + " not owner",
						user.getCode() + " " + comment.getOwnerCode());
				ivComListDelete.setVisibility(View.INVISIBLE);
				tvComListUserName.setText(comment.getOwnerName());
			} else {
				Log.d(TAG + " owner",
						user.getCode() + " " + comment.getOwnerCode());
				tvComListUserName.setText("You");
			}
		}

		return rowView;
	}

	private String getTimeElapsed(String dateStr) {

		// Log.d(TAG,date.getYear()+1900+" "+date.getMonth()+" "+date.getDay()+" "+date.getHours()+" "+date.getMinutes()+" "+date.getSeconds()+" "+0);
		DateTimeFormatter dtf = DateTimeFormat
				.forPattern("yyyy-MM-dd HH:mm:ss");
		DateTime commentDate = dtf.parseDateTime(dateStr);

		DateTime now = new DateTime();
		Period period = new Period(commentDate, now);
		PeriodFormatter formatter = new PeriodFormatterBuilder().toFormatter();
		;
		if (period.getYears() > 0) {
			formatter = new PeriodFormatterBuilder().appendYears()
					.appendSuffix(" years ago").printZeroNever().toFormatter();
		} else if (period.getMonths() > 0) {
			formatter = new PeriodFormatterBuilder().appendMonths()
					.appendSuffix(" months ago").printZeroNever().toFormatter();
		} else if (period.getWeeks() > 0) {
			formatter = new PeriodFormatterBuilder().appendWeeks()
					.appendSuffix(" weeks ago").printZeroNever().toFormatter();
		} else if (period.getDays() > 0) {
			formatter = new PeriodFormatterBuilder().appendDays()
					.appendSuffix(" days ago").printZeroNever().toFormatter();
		} else if (period.getHours() > 0) {
			formatter = new PeriodFormatterBuilder().appendHours()
					.appendSuffix(" hours ago").printZeroNever().toFormatter();
		}

		else if (period.getMinutes() > 0) {
			formatter = new PeriodFormatterBuilder().appendMinutes()
					.appendSuffix(" minutes ago").printZeroNever()
					.toFormatter();
		} else if (period.getSeconds() > 0) {
			formatter = new PeriodFormatterBuilder().appendSeconds()
					.appendSuffix(" seconds ago").printZeroNever()
					.toFormatter();
		}

		return formatter.print(period) == null ? "just now" : formatter
				.print(period);
	}

	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}
	
	@Override
	public boolean isEnabled(int position) {
		// return false if position == position you want to disable
		Comment comment = (Comment)data.get(position);
		if (comment.getOwnerCode() != user.getCode()) {
			return false;
		} else {
			return true;
		}
	}
}
