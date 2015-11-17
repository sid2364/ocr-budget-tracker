package edu.sfsu.cs.orange.ocr;


import java.util.ArrayList;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends Activity {

	ArrayList<String> list=new ArrayList<String>();
    ArrayAdapter<String> adapter;
    ListView lv;
    Context appCon;
    AlertDialog alert, alert2;
    AlertDialog.Builder builder, builder2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list);
        lv= (ListView)findViewById(R.id.listView1);
        lv.setAdapter(adapter);
        
        adapter.add("Start a new budget");//0
        adapter.add("View average daily expenditure");
        adapter.add("Clear all expenditure logs");//2
        adapter.add("Main Menu");
        adapter.add("About Budget Tracker");
        adapter.add("Exit");
        
        appCon = getApplicationContext();
        builder = new AlertDialog.Builder(this);
	    builder.setTitle("Confirm");
	    builder.setMessage("Are you sure you wish to exit?");
	    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) {
	        	finish();
        		System.exit(0);
	        	dialog.dismiss();
	        }
	    });
	    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) {
	        	dialog.dismiss();
	        }
	    });
	    alert = builder.create();
	    
	    builder2 = new AlertDialog.Builder(this);
	    builder2.setTitle("Confirm");
	    builder2.setMessage("Are you sure you wish to clear all your accounts so far?");
	    builder2.setPositiveButton("YES", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) {
	        	SimpleDatabaseHelper db = new SimpleDatabaseHelper(appCon);
	        	db.clearExpenditures();
	        }
	    });
	    builder2.setNegativeButton("NO", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) {
	        	dialog.dismiss();
	        }
	    });
	    alert2 = builder2.create();


        lv.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
            	SimpleDatabaseHelper db = new SimpleDatabaseHelper(appCon);
            	
            	String clicked = (String)lv.getItemAtPosition(position);
            	if(position==0){
            		Intent i = new Intent(getApplicationContext(), ResetActivity.class);
            		startActivity(i);
            	}
            	else if(position==1)
            	{
            		
            		String a[]=db.getAverage();
            		final Toast tag = Toast.makeText(getApplicationContext(),"You have spent Rs. "+a[2]+" per day!\nIt has "+(Integer.parseInt(a[0])==1?"only ":"")+"been "+a[0]+" "+(Integer.parseInt(a[0])==1?"day":"days")+" since you updated your budget. \nAnd the amount you have spent since is Rs. "+a[1],Toast.LENGTH_LONG);
            		tag.show();
            		new CountDownTimer(10000, 1000)
            		{
            		    public void onTick(long millisUntilFinished) {tag.show();}
            		    public void onFinish() {tag.show();}

            		}.start();
            	}else if(position==3){
            		finish();
            	}
            	else if(position==4)
            	{
            		Intent i = new Intent(getApplicationContext(), AboutActivity.class);
            		startActivity(i);
            	}
            	else if(position==2){
            		alert2.show();
            	}
            	else{
            		alert.show();
            	}
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        setTitle("Settings");
        return true;
    }
}
