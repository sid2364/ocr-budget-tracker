package edu.sfsu.cs.orange.ocr;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.view.Menu;
import android.webkit.WebView;
import android.widget.TextView;

public class AboutActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		TextView tv = (TextView)findViewById(R.id.textViewAbout);
		String about = "\"The habit of saving is itself an education; it fosters every virtue, teaches self-denial, " +
				"cultivates the sense of order, trains to forethought, and so broadens the mind.\" " +
				"This simple application has been developed in hope to bring some order into an otherwise unorderly " +
				"business; money. Money is a terrible master but an excellent servant, tame it well!<br>Feel free to contact me about any bug that you notice, or anything you would like being " +
				"implemented; I'll be more than welcome to any constructive criticism. Thank you very much for using Budget Tracker! " +
				"Cheers.<br><br>Siddharth Sahay<br>sahaysid@gmail.com";
		//tv.setText(about);

		WebView view = (WebView) findViewById(R.id.textContent);
		String text;
		text = "<html><body><h4 align=\"justify\"><font color='white'>";
		text+= about;
		text+= "</font></h4></body></html>";
		view.setBackgroundColor(Color.TRANSPARENT);
		view.loadData(text, "text/html", "utf-8");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.about, menu);
		setTitle("About Budget Tracker");
		return true;
	}

}
