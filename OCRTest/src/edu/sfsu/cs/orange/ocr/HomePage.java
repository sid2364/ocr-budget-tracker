package edu.sfsu.cs.orange.ocr;

import java.text.SimpleDateFormat;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class HomePage extends Activity {
	TextView budget, spent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_page);
		budget = (TextView)findViewById(R.id.labelMoney);
		spent = (TextView)findViewById(R.id.labelBudget);
		TextView lblExp = (TextView)findViewById(R.id.textViewLabelTotalMoney);
		SimpleDatabaseHelper db = new SimpleDatabaseHelper(getApplicationContext());
		budget.setText("Rs. "+db.getTotalSpent());
		spent.setText("Rs. "+db.getBudget());
		//Toast.makeText(getApplicationContext(), "Your budget: "+db.getBudget()+"", Toast.LENGTH_LONG).show();
		TextView labelSinceDate = (TextView)findViewById(R.id.TextViewLabelBudget);
		String da=db.getStartDate();
		
		if(da.equals("0")){
			da="Till now you have kept track of:";
			Toast.makeText(getApplicationContext(), "Start a new budget in the settings!", Toast.LENGTH_LONG).show();
		}
		else da="Balance amount since "+da+":";
		lblExp.setText("Total expenditure accounted for: ");
		labelSinceDate.setText(da);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home_page, menu);
		setTitle("Track Your Budget");
		return true;
	}

	public void goToOCR(View v){
		Intent i = new Intent(getApplicationContext(), CaptureActivity.class);
		finish();
		startActivity(i);	
	}
	public void goToDebts(View v){
		Intent i = new Intent(getApplicationContext(), Debts.class);
		startActivity(i);	
	}
	public void goToSettings(View v){
		Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
		startActivity(i);
	}
	public void addExpenseHome(View v){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setTitle("Confirm");
	    builder.setMessage("Are you sure you wish to add this expense?");
	    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) {
	    	    EditText tv = (EditText)findViewById(R.id.editTextAmount);
	    		Spinner mySpinner=(Spinner) findViewById(R.id.spinnerCategory);
	    		String text = mySpinner.getSelectedItem().toString();
	    		if(text.equals("")){
	    			Toast.makeText(getApplicationContext(), "Please select a category and then try again.", 
	    					   Toast.LENGTH_SHORT).show();
	    			return;
	    		}
	    		//Toast.makeText(getApplicationContext(), tv.getText().toString(), Toast.LENGTH_LONG).show();
	    		int integer=0;
	    		try{ //  123.00 
	    			double d = Double.parseDouble(tv.getText().toString());
	    			integer = (int)d;
	    			Exception e = new Exception();
	    			if(d==0)throw e;
	    		}catch(Exception e){
	    			Toast.makeText(getApplicationContext(), "This is not a valid number! Please try again.", 
	    					   Toast.LENGTH_SHORT).show();
	    			tv.setText("");
	    			return;
	    		}
	    		
	    		DateToday d = new DateToday();
	    		
	    		Toast.makeText(getApplicationContext(), "Adding "+integer+" to your '"+text.toLowerCase()+"' budget on "+d.getTodayDate()+". Please wait.", 
	    				   Toast.LENGTH_SHORT).show();

	    		Budget b = new Budget();
	    		b.expense = integer;
	    		b.category = text.toLowerCase();
	    		b.date =d.getTodayDateTime();
	    		SimpleDatabaseHelper db = new SimpleDatabaseHelper(getApplicationContext());
	    		String s = db.addExpense(b);
	    		Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
	    		updateFields();
	    		tv.setText("");
	        	dialog.dismiss();
	        }
	    });
	    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) {
	        	dialog.dismiss();
	        }
	    });
	    AlertDialog alert = builder.create();
	    alert.show();
	    
	}
	void updateFields(){
		SimpleDatabaseHelper db = new SimpleDatabaseHelper(getApplicationContext());
		spent.setText("Rs. "+db.getBudget()+"");
		budget.setText("Rs. "+db.getTotalSpent()+"");
		if(db.getBudget()<0){
			Toast.makeText(getApplicationContext(), "Be careful! You might be spending too much!", Toast.LENGTH_LONG).show();
		}
	}
	public void viewExpenses(View v){
		Intent i = new Intent(getApplicationContext(), ViewExpenses.class);
		startActivity(i);
	}
}
