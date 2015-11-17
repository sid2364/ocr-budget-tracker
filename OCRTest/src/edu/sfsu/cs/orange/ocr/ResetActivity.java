package edu.sfsu.cs.orange.ocr;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ResetActivity extends Activity {
	EditText amt;
	EditText date;
	Calendar myCalendar;
	DatePickerDialog.OnDateSetListener date2;
	AlertDialog alert;
	AlertDialog.Builder builder;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reset);
		amt=(EditText)findViewById(R.id.editText1);
		 date=(EditText)findViewById(R.id.editText2);

		 SimpleDatabaseHelper db = new SimpleDatabaseHelper(getApplicationContext());
		    
			builder = new AlertDialog.Builder(this);
		    builder.setTitle("Confirm");
		    if(db.getStartDate().equals("0"))builder.setMessage("Proceed to add your first budget?");
		    else builder.setMessage("Are you sure you wish to end the current budget and start a new one?");
		    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) {
		        	String a, d;
		    		a = amt.getText().toString();
		    		d = date.getText().toString();
		    		if(d.equals("")||d.equals("0")){
		    			Toast.makeText(getApplicationContext(), "Please select a valid date!", Toast.LENGTH_SHORT).show();
		    			return;
		    		}
		    		if(a.equals("")){
		    			Toast.makeText(getApplicationContext(), "Please specify a valid amount!", Toast.LENGTH_SHORT).show();
		    			return;
		    		}
		    		Toast.makeText(getApplicationContext(), "Adding amount starting on "+d+" for Rs. "+a+"!", Toast.LENGTH_SHORT).show();
		    		SimpleDatabaseHelper db = new SimpleDatabaseHelper(getApplicationContext());
		    		Budget b = new Budget();
		    		b.budget = Integer.parseInt(a);
		    		b.startdate = d;
		    		
		    		db.newBudget(b);
		    		Intent i = new Intent(getApplicationContext(), HomePage.class);
		    		startActivity(i);
		    		finish();
		        	dialog.dismiss();
		        }
		    });
		    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) {
		        	dialog.dismiss();
		        }
		    });
		    alert = builder.create();

		 myCalendar = Calendar.getInstance();

		 date2 = new DatePickerDialog.OnDateSetListener() {

		     @Override
		     public void onDateSet(DatePicker view, int year, int monthOfYear,
		             int dayOfMonth) {
		         // TODO Auto-generated method stub
		         myCalendar.set(Calendar.YEAR, year);
		         myCalendar.set(Calendar.MONTH, monthOfYear);
		         myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		         updateLabel();
		     }
		     private void updateLabel() {

			     String myFormatS = "dd/MM/yyyy"; //In which you need put here
			     SimpleDateFormat sdf = new SimpleDateFormat(myFormatS, Locale.US);
			    
			     SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
			     Calendar c = Calendar.getInstance();
			     
			     String selected = sdf.format(myCalendar.getTime());
			     String rightNow = myFormat.format(c.getTime());
			     	try{
		            Date date1 = myFormat.parse(rightNow);
		            Date date2 = myFormat.parse(selected);
		            long diff = date1.getTime() - date2.getTime();
		            long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
		            if(days<0){
		            	date.setText("");
		            	Toast.makeText(getApplicationContext(), "You cannot select a date in the future! Try again.", Toast.LENGTH_SHORT).show();
		            	return;
		            }
		            }catch(Exception e){
			     		e.printStackTrace();
			     	}       		            
		            
			     date.setText(sdf.format(myCalendar.getTime()));
			     }


		     
		 };

		    date.setOnClickListener(new OnClickListener() {

		         @Override
		         public void onClick(View v) {
		        	 
		             new DatePickerDialog(ResetActivity.this, date2, myCalendar
		                     .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
		                     myCalendar.get(Calendar.DAY_OF_MONTH)).show();
		         }
		     });

		      
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.reset, menu);
		setTitle("Start New Budget");
		return true;
	}
	public void addNewBudget(View v){

	    alert.show();
					
	}

}
