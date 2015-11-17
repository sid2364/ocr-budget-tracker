package edu.sfsu.cs.orange.ocr;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Class that wraps the most common database operations. This example assumes you want a single table and data entity
 * with two properties: a title and a priority as an integer. Modify in all relevant locations if you need other/more
 * properties for your data and/or additional tables.
 */
public class SimpleDatabaseHelper {

	private SQLiteOpenHelper _openHelper;
    
	private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "BudgetTrackerThree";

    private static final String TABLE_NAME_BUDGET = "budgetTab";
    private static final String TABLE_NAME_EXPENSES = "expenses";
    private static final String TABLE_NAME_DEBTS = "debts";

    private static final String KEY_BUDGET = "budget";
    private static final String KEY_STARTDATE = "startdate";
    
    private static final String KEY_EXPENSE = "expense";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_DATE = "date";
    
    private static final String KEY_DEBTDATE = "debtdate";
    private static final String KEY_DEBTAMOUNT = "amount";
    private static final String KEY_DEBTPERSON = "person";
    


    public SimpleDatabaseHelper(Context context) {
        _openHelper = new SimpleSQLiteOpenHelper(context);
    }

    /**
     * This is an internal class that handles the creation of all database tables
     */
    class SimpleSQLiteOpenHelper extends SQLiteOpenHelper {
        SimpleSQLiteOpenHelper(Context context) {
            super(context, DATABASE_NAME+".db", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
        	String CREATE_TABLE_BUDGET = "create table if not exists " + TABLE_NAME_BUDGET + "("
                    + KEY_BUDGET + " integer," + KEY_STARTDATE + " text)";
            String CREATE_TABLE_EXPENSES = "create table if not exists " + TABLE_NAME_EXPENSES + "("
                    + KEY_EXPENSE + " integer," + KEY_CATEGORY + " text, "+ KEY_DATE +" text)";
            String CREATE_TABLE_DEBTS = "create table if not exists " + TABLE_NAME_DEBTS + "("
                    + KEY_DEBTAMOUNT + " text," + KEY_DEBTPERSON + " text, "+ KEY_DEBTDATE +" text)";
            db.execSQL(CREATE_TABLE_BUDGET);
            db.execSQL(CREATE_TABLE_EXPENSES);
            db.execSQL(CREATE_TABLE_DEBTS);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }

    
    public int getAmountExpected(){
    	SQLiteDatabase db = _openHelper.getReadableDatabase();
    	
    	Cursor cursor = db.rawQuery("Select * from "+TABLE_NAME_DEBTS, null);
    	if(cursor.getCount()==0)return 0;
    	
    	cursor = db.rawQuery("select * from "+TABLE_NAME_DEBTS, null);
    	cursor.moveToFirst();
    	int sum=0;
    	
    	while(!cursor.isAfterLast()){
    		int amt = Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_DEBTAMOUNT)));
    		//if(amt!=0)return amt;
    		if(amt>0){cursor.moveToNext();continue;} 
    		sum+=amt;
    		cursor.moveToNext();
    	}
    	db.close();
        return sum*-1;
    }
    
    public int getAmountOwed(){
    	
    	SQLiteDatabase db = _openHelper.getReadableDatabase();
    	//db.execSQL("delete from "+TABLE_NAME_DEBTS);//REMOVE THIS
    	Cursor cursor = db.rawQuery("Select * from "+TABLE_NAME_DEBTS, null);
    	if(cursor.getCount()==0)return 0;
        
    	cursor = db.rawQuery("select * from "+TABLE_NAME_DEBTS, null);
    	cursor.moveToFirst();
    	int sum=0;
    	while(!cursor.isAfterLast()){
    		int amt = Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_DEBTAMOUNT)));
    		if(amt<0){cursor.moveToNext();continue;}  //only if amount is debit
    		sum+=amt;
    		cursor.moveToNext();
    	}
    	db.close();
        return sum;
    }
    public void addDebt(Budget b) {
        SQLiteDatabase db = _openHelper.getWritableDatabase();

        DateToday datetime = new DateToday();
        ContentValues values = new ContentValues();
        values.put(KEY_DEBTAMOUNT, b.debt);
        values.put(KEY_DEBTPERSON, b.person);
        values.put(KEY_DEBTDATE, datetime.getTodayDateTime()+"");
        String d, p, t;
        t=datetime.getTodayDateTime();
        
        Cursor c = db.rawQuery("select * from "+TABLE_NAME_DEBTS+" where "+KEY_DEBTPERSON+"='"+b.person+"'", null);
        if(c.moveToFirst()){
        	int oldDebt = Integer.parseInt(c.getString(c.getColumnIndex(KEY_DEBTAMOUNT)));
        	int newDebt = (oldDebt+Integer.parseInt(b.debt));
        	if(newDebt==0)
        		db.execSQL("delete from "+TABLE_NAME_DEBTS+" where "+KEY_DEBTPERSON+"='"+b.person+"';");
        	else
        		db.execSQL("update "+TABLE_NAME_DEBTS+" set "+KEY_DEBTAMOUNT+"='"+newDebt+"', "+KEY_DEBTDATE+"='"+t+"' where "+KEY_DEBTPERSON+"='"+b.person+"';");
        }else{
	        d=b.debt;
	        p=b.person;
	        String sql = "INSERT INTO "+TABLE_NAME_DEBTS+" VALUES('"+d+"', '"+p+"', '"+t+"')";
			db.execSQL(sql);
        }
        db.close();
    }
    public String[] getAllDebts(){
    	SQLiteDatabase db = _openHelper.getReadableDatabase();
    	
    	Cursor cursor = db.rawQuery("Select * from "+TABLE_NAME_DEBTS, null);
    	if(cursor.getCount()==0)return new String[]{"You have no debts! Keep it up!"};
                
    	String debts[] = new String[cursor.getCount()];
    	int counter=0;

    	cursor.moveToFirst();

    	String temp="";
    	while(!cursor.isAfterLast()){
    		temp+="On "+cursor.getString(cursor.getColumnIndex(KEY_DEBTDATE))+", ";
    		temp+="you "+(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_DEBTAMOUNT)))>0?"lent to":"borrowed from");
    		temp+=" "+cursor.getString(cursor.getColumnIndex(KEY_DEBTPERSON));
    		temp+=" Rs. "+((cursor.getString(cursor.getColumnIndex(KEY_DEBTAMOUNT)).charAt(0)=='-')?cursor.getString(cursor.getColumnIndex(KEY_DEBTAMOUNT)).substring(1):cursor.getString(cursor.getColumnIndex(KEY_DEBTAMOUNT)));
    		debts[counter++]=temp;
    		cursor.moveToNext();
    		temp="";
    	}
    	db.close();
        return debts;
    	
    }
    public int getTotalSpent(){
    	SQLiteDatabase db = _openHelper.getReadableDatabase();

    	//db.execSQL("delete from "+TABLE_NAME_EXPENSES);db.execSQL("delete from "+TABLE_NAME_BUDGET);
    	
    	Cursor cursor = db.rawQuery("Select * from "+TABLE_NAME_EXPENSES, null);
    	if(cursor.getCount()==0)return 0;
                
    	cursor = db.rawQuery("select "+KEY_EXPENSE+" from "+TABLE_NAME_EXPENSES, null);
    	cursor.moveToFirst();
    	int sum=0;
    	while(!cursor.isAfterLast()){
    		sum+=Integer.parseInt(cursor.getString(0));
    		cursor.moveToNext();
    	}
    	db.close();
        return sum;
    }
    public String addExpense(Budget b) {
        SQLiteDatabase db = _openHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_EXPENSE, b.expense);
        values.put(KEY_CATEGORY, b.category);
        values.put(KEY_DATE, b.date);
        String startDate;
        DateToday d = new DateToday();
		int flagUpdate=0;
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_NAME_BUDGET,null);
		if(!cursor.moveToFirst()){
			b.budget = 0;
			startDate=d.getTodayDate();
			flagUpdate = 1;
		}
		else{
			b.budget = Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_BUDGET)));
			startDate = cursor.getString(cursor.getColumnIndex(KEY_STARTDATE));
		}
		cursor.moveToFirst();
		
		db.insert(TABLE_NAME_EXPENSES, null, values);
      
		
		if(d.isAfterStartDate(d.getTodayDate(), startDate)){
			if(flagUpdate==0)db.execSQL("update "+TABLE_NAME_BUDGET+" set "+KEY_BUDGET+"="+(b.budget-b.expense));  
			b.budget = b.budget - b.expense;
		}
        db.close();
        return "Expense added!";

    }



    public int getBudget() {
        SQLiteDatabase db = _openHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("select "+KEY_BUDGET+" from "+TABLE_NAME_BUDGET, null);

        if (cursor.getCount()!=0)
            cursor.moveToFirst();
        else return 0;
        int r = Integer.parseInt(cursor.getString(0));
        db.close();
        return r;
    }
    public String getStartDate() {
        SQLiteDatabase db = _openHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("select "+KEY_STARTDATE+" from "+TABLE_NAME_BUDGET, null);

        if (cursor.getCount()!=0)
            cursor.moveToFirst();
        else return "0";
        String r = cursor.getString(0);
        db.close();
        return r;
    }
    public void clearExpenditures(){
    	SQLiteDatabase db = _openHelper.getReadableDatabase();

    	db.execSQL("delete from "+TABLE_NAME_EXPENSES);
    	
    	db.close();

    }

    public void newBudget(Budget b){
    	SQLiteDatabase db = _openHelper.getWritableDatabase();

    	//db.execSQL("delete from "+TABLE_NAME_EXPENSES);//REMOVE THIS
    	db.execSQL("delete from "+TABLE_NAME_BUDGET);
    	
        ContentValues values = new ContentValues();
        values.put(KEY_BUDGET, b.budget);
        values.put(KEY_STARTDATE, b.startdate);        
        db.insert(TABLE_NAME_BUDGET, null, values);
        
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_NAME_EXPENSES,null);
		if(cursor.getCount()==0)
			return;
		cursor.moveToFirst();
		
		DateToday d = new DateToday();
		while(!cursor.isAfterLast()){
			String startDate = cursor.getString(cursor.getColumnIndex(KEY_DATE));
			b.expense = Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_EXPENSE)));
			if(d.isAfterStartDate( d.getTodayDate(),startDate)){
				b.budget -= b.expense;
				db.execSQL("update "+TABLE_NAME_BUDGET+" set "+KEY_BUDGET+"="+(b.budget));  
				
			}
			cursor.moveToNext();
		}
		
		db.close();
        return;

    }
    
    public String[] getExpenditureCategory(String category){
    	SQLiteDatabase db = _openHelper.getReadableDatabase();
    	String arr[];
    	Cursor cursor = db.rawQuery("select * from "+TABLE_NAME_EXPENSES+" where "+KEY_CATEGORY+" = '"+category+"'", null);
        int count=0;
        if (cursor.moveToFirst()){
            arr=new String[cursor.getCount()];
        }else{
        	return new String[]{"No expenditure in this category!"};
        }
        while(!cursor.isAfterLast()){
	        int expense = cursor.getInt(cursor.getColumnIndex(KEY_EXPENSE));
	        String date = cursor.getString(cursor.getColumnIndex(KEY_DATE));
	        arr[count++]="Rs. "+expense+" on "+date.substring(0, 10)+" at "+date.substring(11);
	        cursor.moveToNext();
        }
        db.close();
        return arr;

    }
    @SuppressLint("SimpleDateFormat")
    public String[] getAverage(){
    	int sum = getTotalSpent();
    	SQLiteDatabase db = _openHelper.getReadableDatabase();
    	String arr[]=new String[5];
    	
    	long days = 0;
    	
        Cursor cursor = db.rawQuery("select "+KEY_STARTDATE+" from "+TABLE_NAME_BUDGET, null);

        if (cursor.getCount()!=0){
            cursor.moveToFirst();
        }else{
        	//return "0";
        	return new String[]{"0","0","0"};
        }
        String r = cursor.getString(0);
        SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yy");
        //String inputString1 = "23 01 1997";
        try {
        	Calendar c = Calendar.getInstance();
        	System.out.println("Current time => " + c.getTime());

        	String formattedDate = myFormat.format(c.getTime());
        	
            Date date1 = myFormat.parse(r);
            Date date2 = myFormat.parse(formattedDate);
            
            long diff = date2.getTime() - date1.getTime();
            //System.out.println ("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
            days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(days==0)days=1;
        arr[0]=""+days;
        arr[1]=""+sum;
        double av = sum/days; 
        arr[2]=""+(av);
        db.close();
        return arr;
    }
    public String deleteDebtWithDets(String name){
    	SQLiteDatabase db = _openHelper.getWritableDatabase();
    	String sql = "delete from "+TABLE_NAME_DEBTS+" where "+KEY_DEBTPERSON+"='"+name+"';";
    	db.execSQL(sql);
    	db.close();
    	return "Debt deleted!";
    }
    
    
}