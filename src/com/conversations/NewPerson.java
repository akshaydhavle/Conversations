package com.conversations;

import android.app.Activity;
import android.content.ContentValues;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class NewPerson extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		final ConversationsDbAdaptor dbAdaptor = new ConversationsDbAdaptor(this);
		dbAdaptor.open();
		
		setContentView(R.layout.new_person);
		
		final EditText personName = (EditText) findViewById(R.id.PersonName);
		Button save = (Button) findViewById(R.id.Save);
		
		save.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ContentValues values = new ContentValues();
				values.put(ConversationsDbAdaptor.T2_NAME, personName.getText().toString());
				long success = dbAdaptor.addNewPerson(values);
				Log.v("Success? ",Long.toString(success));
				if(success!=0){
					setResult(RESULT_OK);
				}else{
					setResult(RESULT_FIRST_USER);
				}
				finish();
			}
		});
	}
}
