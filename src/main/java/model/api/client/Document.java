package model.api.client;

public class Document{
	private String number;
	private String issuedDate;
	private String series;
	private String issuedBy;
	private String type;

	public String getNumber(){
		return number;
	}

	public String getIssuedDate(){
		return issuedDate;
	}

	public String getSeries(){
		return series;
	}

	public String getIssuedBy(){
		return issuedBy;
	}

	public String getType(){
		return type;
	}
}
