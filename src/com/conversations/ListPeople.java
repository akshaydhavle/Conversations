package com.conversations;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ListPeople extends ListActivity {
	private static final int MENU_NEW_PERSON = 1;

	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ConversationsDbAdaptor dbAdaptor = new ConversationsDbAdaptor(this);
		dbAdaptor.open();
				
		Cursor cursor = dbAdaptor.fetchAllPeople();
		startManagingCursor(cursor);
		String[] from = new String[]{ConversationsDbAdaptor.T2_NAME, ConversationsDbAdaptor.T2_PK};
        int[] to = new int[]{android.R.id.text1, android.R.id.text2};
		ListAdapter listAdaptor = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, cursor, from, to);
		
		setListAdapter(listAdaptor);	
	}
	
	public void onListItemClick(ListView l, View v, int position, long id) {
		Log.v("ListItem", "clicked");
		Intent intent = new Intent(ListPeople.this, EditNote.class);
		intent.putExtra("ID", id);
		Log.v("ID : ", Long.toString(id));		
		startActivity(intent);
	}
	
	/* Creates the menu items */
	public boolean onCreateOptionsMenu(Menu menu) {
	    menu.add(0, MENU_NEW_PERSON, 0, "New Person");
	    return true;
	}

	/* Handles item selections */
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case MENU_NEW_PERSON:
	        newPerson();
	        return true;
	    }
	    return false;
	}

	private void newPerson() {
		Intent newPerson = new Intent(this, NewPerson.class);
		startActivityForResult(newPerson, RESULT_OK);
	}
}
