package com.conversations;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class EditNote extends Activity {
	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		final String person_id = Long.toString(getIntent().getExtras().getLong("ID"));
		Log.v("user id", person_id);
		
		final ConversationsDbAdaptor dbAdaptor = new ConversationsDbAdaptor(this);
		dbAdaptor.open();
		
		Cursor cursor = dbAdaptor.fetchNoteForPerson(person_id);	
		final int count = cursor.getCount();
		startManagingCursor(cursor);
		cursor.moveToFirst();

		String notes;
		final String rowid;
		if(count > 0){
			notes = cursor.getString(0);
			rowid = cursor.getString(1);
			Log.v("Row Id from the DB: "+rowid,"Need to use this for save");
		}else{
			notes = " ";
			rowid = person_id;
			Log.v("Row Id same as User ID : "+rowid,"Need to use this for save");
			
		}
		
		setContentView(R.layout.notes_layout);		
	     
		final EditText noteText = (EditText) findViewById(R.id.EditText01);
		noteText.setText(notes.toCharArray(),0,notes.length());
		
		Button save = (Button) findViewById(R.id.Button01);
		Button back = (Button) findViewById(R.id.Button02);
		
		save.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ContentValues data = new ContentValues();
				data.put(ConversationsDbAdaptor.T1_PERSON_ID, person_id);
				data.put(ConversationsDbAdaptor.T1_NOTES, noteText.getText().toString());

				Intent intent = updateOrInsertNote(count, rowid, v, data);
				startActivity(intent);		
			}

			private Intent updateOrInsertNote(final int count,
					final String rowid, View v, ContentValues data) {
				
				long success = 0;
				
				if(count > 0){
					success = dbAdaptor.updateNote(data, ConversationsDbAdaptor.T1_PK + "=" + rowid, null);
				}else{
					success = dbAdaptor.insertNote(null, data);
				}
				
				Intent intent = new Intent(EditNote.this, ListPeople.class);
				
				intent.putExtra("success", success!=0);
				return intent;
			}
		});
		
		back.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(EditNote.this, ListPeople.class);
				startActivity(intent);
			}
		});
	}
}
