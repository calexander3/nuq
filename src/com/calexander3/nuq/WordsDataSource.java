package com.calexander3.nuq;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class WordsDataSource {
	// Database fields
	private SQLiteDatabase database;
	private DatabaseManager dbHelper;
	private String[] allColumns = { DatabaseManager.COLUMN_ID,
			DatabaseManager.COLUMN_EText, DatabaseManager.COLUMN_KText };

	public WordsDataSource(Context context) {
		dbHelper = new DatabaseManager(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public Word createWord(String EWord, String KWord) {
		ContentValues values = new ContentValues();
		values.put(DatabaseManager.COLUMN_EText, EWord);
		values.put(DatabaseManager.COLUMN_KText, KWord);
		long insertId = database.insert(DatabaseManager.TABLE_Words, null,
				values);
		Cursor cursor = database.query(DatabaseManager.TABLE_Words,
				allColumns, DatabaseManager.COLUMN_ID + " = " + insertId, null,
				null, null, null);
		cursor.moveToFirst();
		Word newWord = cursorToWord(cursor);
		cursor.close();
		return newWord;
	}

	public void deleteWord(Word word) {
		long id = word.getId();
		System.out.println("Word deleted with id: " + id);
		database.delete(DatabaseManager.TABLE_Words, DatabaseManager.COLUMN_ID
				+ " = " + id, null);
	}

	public List<Word> getAllWords() {
		List<Word> words = new ArrayList<Word>();
		Cursor cursor = database.query(DatabaseManager.TABLE_Words, allColumns,
				null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Word word = cursorToWord(cursor);
			words.add(word);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return words;
	}
	
	public Word getWordByEText(String EText)
	{
		Word returnWord = null;
		Cursor cursor = database.query(DatabaseManager.TABLE_Words, allColumns,
				"EText = ?",new String[] {EText}, null, null, null);
		cursor.moveToFirst();
		if(!cursor.isAfterLast())
		{
			returnWord = cursorToWord(cursor);
		}
		
		return returnWord;
	}

	private Word cursorToWord(Cursor cursor) {
		Word word = new Word();
		word.setId(cursor.getLong(0));
		word.setEText(cursor.getString(1));
		word.setKText(cursor.getString(2));
		return word;
	}
}
