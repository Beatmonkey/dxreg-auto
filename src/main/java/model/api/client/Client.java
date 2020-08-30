package model.api.client;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Client{
	private String lastLogin;
	private String contractDate;
	private String comments;
	private int passwordExpiry;
	private String citizenship;
	private Document document;
	private String fullName;
	private String createdDateTime;
	private List<String> groups;
	private String login;
	private String type;
	private String birthDate;
	private String birthPlace;
	private String insuranceNumber;
	private String brokerCode;
	private String password;
	private String phone;
	private String domain;
	private boolean passwordReset;
	private String company;
	private Location location;
	private List<AccountsItem> accounts;
	private String email;
	private String status;

	public String getLastLogin(){
		return lastLogin;
	}

	public String getContractDate(){
		return contractDate;
	}

	public String getComments(){
		return comments;
	}

	public int getPasswordExpiry(){
		return passwordExpiry;
	}

	public String getCitizenship(){
		return citizenship;
	}

	public Document getDocument(){
		return document;
	}

	public String getFullName(){
		return fullName;
	}

	public String getCreatedDateTime(){
		return createdDateTime;
	}

	public List<String> getGroups(){
		return groups;
	}

	public String getLogin(){
		return login;
	}

	public String getType(){
		return type;
	}

	public String getBirthDate(){
		return birthDate;
	}

	public String getBirthPlace(){
		return birthPlace;
	}

	public String getInsuranceNumber(){
		return insuranceNumber;
	}

	public String getBrokerCode(){
		return brokerCode;
	}

	public String getPassword(){
		return password;
	}

	public String getPhone(){
		return phone;
	}

	public String getDomain(){
		return domain;
	}

	public boolean isPasswordReset(){
		return passwordReset;
	}

	public String getCompany(){
		return company;
	}

	public Location getLocation(){
		return location;
	}

	public List<AccountsItem> getAccounts(){
		return accounts;
	}

	public String getEmail(){
		return email;
	}

	public String getStatus(){
		return status;
	}
}