package edu.sfsu.cs.orange.ocr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
//import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.Toast;

public class ViewExpenses extends Activity {

	ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    Context appCon;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_expenses);

        expListView = (ExpandableListView) findViewById(R.id.expandableListView1);
        appCon = getApplicationContext();
        prepareListData();
        listAdapter = new ExpandableListAdapterSid(getApplicationContext(), listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);
        
        expListView.setOnGroupExpandListener(new OnGroupExpandListener() {

            public void onGroupExpand(int groupPosition) {
                int len = listAdapter.getGroupCount();

                for(int i=0; i<len; i++) {
                    if(i != groupPosition) {
                    	expListView.collapseGroup(i);
                    }
                }
            }

        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_expenses, menu);
		setTitle("All Expenses");
		return true;
	}
	private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
 
        // Adding child data
        listDataHeader.add("Miscellaneous");
        listDataHeader.add("Food");
        listDataHeader.add("Home");
        listDataHeader.add("Entertainment");
        listDataHeader.add("Electronics");
        listDataHeader.add("Health");
 
        // Adding child data
        List<String> misc = new ArrayList<String>();
        List<String> food = new ArrayList<String>();
        List<String> home = new ArrayList<String>();
        List<String> entertainment = new ArrayList<String>();
        List<String> electronics = new ArrayList<String>();
        List<String> health = new ArrayList<String>();
        
        SimpleDatabaseHelper db = new SimpleDatabaseHelper(appCon);
        String a[];
        a= db.getExpenditureCategory("miscellaneous");
        misc = addAllItemsTo(a, misc);

        a= db.getExpenditureCategory("food");
        food = addAllItemsTo(a, food);

        a= db.getExpenditureCategory("home");
        home = addAllItemsTo(a, home);

        a= db.getExpenditureCategory("entertainment");
        entertainment = addAllItemsTo(a, entertainment);

        a= db.getExpenditureCategory("electronics");
        electronics = addAllItemsTo(a, electronics);

        a= db.getExpenditureCategory("health");
        health = addAllItemsTo(a, health);

 
        listDataChild.put(listDataHeader.get(0), misc); // Header, Child data
        listDataChild.put(listDataHeader.get(1), food);
        listDataChild.put(listDataHeader.get(2), home);
        listDataChild.put(listDataHeader.get(3), entertainment);
        listDataChild.put(listDataHeader.get(4), electronics);
        listDataChild.put(listDataHeader.get(5), health);
        


    }
	public List<String> addAllItemsTo(String arr[], List<String> l){
		for(String item:arr){
			l.add(item);
		}
		return l;
	}
	public void back(View v){
		Intent i = new Intent(getApplicationContext(),HomePage.class); 
		finish();
		startActivity(i);
	}

}
//

//
