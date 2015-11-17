package edu.sfsu.cs.orange.ocr;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ShowDebts extends Activity {
	ArrayAdapter<String> ad1;
	ArrayList<String> al;
	ListView lvDebts;
	Context dbContext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_debts);
		al = new ArrayList<String>();
		lvDebts = (ListView)findViewById(R.id.listViewAllDebts);
		al = fillIntoArrayList();
		ad1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_multiple_choice, al);
		lvDebts.setAdapter(ad1);
		dbContext = this;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.show_debts, menu);
		setTitle("Debt Details");
		return true;
	}
	public ArrayList<String> fillIntoArrayList(){
		al.clear();
		SimpleDatabaseHelper db = new SimpleDatabaseHelper(getApplicationContext());
		String a[] = db.getAllDebts();
		String item;
		for(int i=0;i<a.length;i++){
			item = a[i];
			al.add(item);
			//Toast.makeText(getApplicationContext(), item, Toast.LENGTH_SHORT).show();
		}
		return al;
	}
	public void goToMenu(View v){
		Intent i = new Intent(getApplicationContext(), HomePage.class);
		finish();
		startActivity(i);
	}
	public void goToDebts(View v){
		Intent i = new Intent(getApplicationContext(), Debts.class);
		finish();
		startActivity(i);
	}

	public void deleteDebt(View v){
		SparseBooleanArray checked = lvDebts.getCheckedItemPositions();
        ArrayList<String> selectedItems = new ArrayList<String>();
        if(checked.size()==0)
        	return;
        for (int i = 0; i < checked.size(); i++) {
            int position = checked.keyAt(i);
            if (checked.valueAt(i))
                selectedItems.add(ad1.getItem(position));
        }
        String msg;
        SimpleDatabaseHelper db = new SimpleDatabaseHelper(getApplicationContext());
        for(String item: selectedItems){
        	if(item.equals("You have no debts! Keep it up!")){
        		Toast.makeText(getApplicationContext(), "You have no debts!", Toast.LENGTH_SHORT).show();
        		return;
        	}
        	String arr[] = extractMoneyNameDate(item);
        	msg = db.deleteDebtWithDets(arr[1]);
			Toast.makeText(getApplicationContext(), msg+"", Toast.LENGTH_LONG).show();	
        }
        al = fillIntoArrayList();
		ad1.notifyDataSetChanged();
	}
	public static String[] extractMoneyNameDate(String line){
        String money, name, date;
        int money_position = line.indexOf("Rs. ");
        money = line.substring(money_position+4);
        
        if(line.contains("lent to")){
            int name_position = line.indexOf("to");
            name = line.substring(name_position+3, money_position-1);
        }else{
            int name_position = line.indexOf("from");
            name = line.substring(name_position+5, money_position-1);
        }
        
        date = line.substring(3, 19);
        return new String[]{money, name, date};
    }

}
