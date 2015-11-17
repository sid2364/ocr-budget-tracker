package edu.sfsu.cs.orange.ocr;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.widget.Toast;


@SuppressLint("SimpleDateFormat") public class DateToday {
	public String getTodayDate(){
		long date = System.currentTimeMillis(); 
		SimpleDateFormat sdf = new 	SimpleDateFormat("dd/MM/yyyy");
		String dateString = sdf.format(date); 
		return dateString;
	}
	public String getTodayDateTime(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		Date date = new Date();
		return dateFormat.format(date)+""; //2014/08/06 15:59:48
		
	}
		public boolean isAfterStartDate(String date, String startDate){
			Calendar myCalendar;
			 myCalendar = Calendar.getInstance();
	
			 String myFormatS = "dd/MM/yyyy"; //In which you need put here
		     SimpleDateFormat sdf = new SimpleDateFormat(myFormatS, Locale.US);
		    
			SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
		     Calendar c = Calendar.getInstance();
		     
		     //String selected = sdf.format(myCalendar.getTime());
		     String rightNow = myFormat.format(c.getTime());
		     	try{
	           Date date1 = myFormat.parse(startDate.substring(0,10));
	           Date date2 = myFormat.parse(date.substring(0,10));
	           long diff = date1.getTime() - date2.getTime();
	           long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
	           if(days<1){
	        	   return true;
	           }
	           }catch(Exception e){
		     		e.printStackTrace();
		     	}       		
		     	return false;
	           
	
		}
}
