package com.calexander3.nuq;

import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
	private WordsDataSource datasource = null;
	private Button btnLookup = null;
	private TextView txtIntro = null;
	private EditText inputWord = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		btnLookup = (Button) findViewById(R.id.btnLookup);
		txtIntro = (TextView) findViewById(R.id.txtIntro);
		inputWord = (EditText) findViewById(R.id.editText1);
		inputWord.setImeActionLabel("Lookup", KeyEvent.KEYCODE_ENTER);

		OpenDatabaseTask task = new OpenDatabaseTask();
		task.execute(new Activity[] { this });

	}

	public void lookupWord(View button) {
		final TextView resultText = (TextView) findViewById(R.id.txtResult);
		if (inputWord.getText().toString().length() > 0) {
			Word result = datasource.getWordByEText(inputWord.getText()
					.toString());
			if (result != null) {
				resultText.setText(result.getKText());
			} else {
				resultText.setText("That word is not in the database.");
			}
		} else {
			resultText.setText("");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	protected void onResume() {
		txtIntro.setText("Preparing database...");
		OpenDatabaseTask task = new OpenDatabaseTask();
		task.execute(new Activity[] { this });
		super.onResume();
	}

	@Override
	protected void onPause() {
		datasource.close();
		super.onPause();
	}

	private class OpenDatabaseTask extends AsyncTask<Activity, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Activity... context) {
			Boolean response = true;

			try {
				if (datasource == null) {
					datasource = new WordsDataSource(context[0]);
				}
				datasource.open();

			} catch (Exception e) {
				response = false;
			}
			return response;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			txtIntro.setText("Enter the english word you wish translated.");
			btnLookup.setEnabled(true);

			inputWord.setOnKeyListener(new View.OnKeyListener() {
				public boolean onKey(View v, int keyCode, KeyEvent event) {
					// TODO Auto-generated method stub
					if (keyCode == KeyEvent.KEYCODE_ENTER) {
						lookupWord(btnLookup);
						return true;
					} else {
						return false;
					}

				}
			});
		}
	}
}
