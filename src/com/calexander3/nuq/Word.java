package com.calexander3.nuq;

public class Word {
	private long id;
	private String EText;
	private String KText;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getEText() {
		return EText;
	}

	public void setEText(String EText) {
		this.EText = EText;
	}

	public String getKText() {
		return KText;
	}

	public void setKText(String KText) {
		this.KText = KText;
	}

	// Will be used by the ArrayAdapter in the ListView
	@Override
	public String toString() {
		return EText + "-" + KText;
	}
}
