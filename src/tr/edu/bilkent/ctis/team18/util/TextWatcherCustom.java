package tr.edu.bilkent.ctis.team18.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class TextWatcherCustom implements TextWatcher{

	EditText view;
	public TextWatcherCustom(EditText view){
		this.view = view;
	}
	
	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		 if (view.getText().toString().length() <= 0)
			 view.setError(null);
	    
	}

}
