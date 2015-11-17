package edu.sfsu.cs.orange.ocr;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class Debts extends Activity {
	EditText amount, person;
	TextView iOwe, theyOwe;
	AlertDialog alert;
	AlertDialog.Builder builder;

    private RadioGroup radioDebtGroup;
    private RadioButton radioDebtButton;  
  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_debts);
		amount = (EditText)findViewById(R.id.editText1);
		person = (EditText)findViewById(R.id.editText2);
		radioDebtGroup = (RadioGroup) findViewById(R.id.radioDebt);
		
		iOwe = (TextView)findViewById(R.id.textViewIOwe);
		theyOwe = (TextView)findViewById(R.id.textViewOweMe);

		updateFields();
		
		builder = new AlertDialog.Builder(this);
	    builder.setTitle("Confirm");
	    builder.setMessage("Are you sure you to add this debt?");
	    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
	        @Override
			public void onClick(DialogInterface dialog, int which) {
	        	//TODO make use of the radio button selection to determine sign of the magnitude of debt amount
	        	String amt, pers;
	    		amt = amount.getText().toString();
	    		pers = person.getText().toString();
	    		if(amt.equals("")||Integer.parseInt(amt)==0){
	    			Toast.makeText(getApplicationContext(), "Please enter an amount!", Toast.LENGTH_SHORT).show();
	    			return;
	    		}
	    		if(pers.equals("")){
	    			Toast.makeText(getApplicationContext(), "Please enter who it was, otherwise you might forget!", Toast.LENGTH_SHORT).show();
	    			return;
	    		}
	    		int selectedId = radioDebtGroup.getCheckedRadioButtonId();
	    		
	    		radioDebtButton = (RadioButton) findViewById(selectedId);
                String clicked = (String) radioDebtButton.getText();
                if(!clicked.equals("This person owes me"))
                	amt="-"+amt;
	    		SimpleDatabaseHelper db = new SimpleDatabaseHelper(getApplicationContext());
	    		Budget b = new Budget();
	    		b.debt = amt;
	    		b.person = pers;
	    		db.addDebt(b); //enable this after making the radio button functionality
	    		Toast.makeText(getApplicationContext(), "An amount of Rs. "+amt+" has been added for "+b.person+"!", Toast.LENGTH_SHORT).show();
	        	updateFields();
	    		dialog.dismiss();
	    		amount.setText("");
	    		person.setText("");
	    		
	        }
	    });
	    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
	        @Override
			public void onClick(DialogInterface dialog, int which) {
	        	dialog.dismiss();
	        }
	    });
	    alert = builder.create();

	}
	public void updateFields(){
		SimpleDatabaseHelper db = new SimpleDatabaseHelper(getApplicationContext());
		iOwe.setText("Rs. "+db.getAmountOwed());
		theyOwe.setText("Rs. "+db.getAmountExpected());
	}
	public void goToMainMenu(View v){
		Intent i = new Intent(getApplicationContext(), HomePage.class);
		finish();
		startActivity(i);
	}

	public void showDebts(View v){
		Intent i = new Intent(getApplicationContext(), ShowDebts.class);
		finish();
		startActivity(i);
	}
	public void addDebt(View v){
		alert.show();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.debts, menu);
		setTitle("Debts");
		return true;
	}

}
