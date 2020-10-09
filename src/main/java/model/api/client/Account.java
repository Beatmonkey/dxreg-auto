package model.api.client;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Account {
	private String accountCashType;
	private String accountCode;
	private String brokerCode;
	private int balance;
	private String accountType;
	private String clearingCode;
	private String currency;
	private List<Category> categories;
	private String type;
	private int credit;
	private String status;
	private String expirationDate;

	public String getAccountCashType(){
		return accountCashType;
	}

	public String getAccountCode(){
		return accountCode;
	}

	public String getBrokerCode(){
		return brokerCode;
	}

	public int getBalance(){
		return balance;
	}

	public String getAccountType(){
		return accountType;
	}

	public String getClearingCode(){
		return clearingCode;
	}

	public String getCurrency(){
		return currency;
	}

	public List<Category> getCategories(){
		return categories;
	}

	public String getType(){
		return type;
	}

	public int getCredit(){
		return credit;
	}

	public String getStatus(){
		return status;
	}

	public String getExpirationDate(){
		return expirationDate;
	}
}