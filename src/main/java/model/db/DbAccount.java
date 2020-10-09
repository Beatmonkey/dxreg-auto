package model.db;

import java.sql.Time;

public class DbAccount {

    private int id;
    private int version;
    private String clearingCode;
    private String accountCode;
    private int currencyId;
    private String status;
    private Time accountCreationTime;
    private int ownerId;
    private String accountCashType;
    private Time expirationTime;
    private Time lastTransactionTime;
    private String extensions;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getClearingCode() {
        return clearingCode;
    }

    public void setClearingCode(String clearingCode) {
        this.clearingCode = clearingCode;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public int getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(int currencyId) {
        this.currencyId = currencyId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Time getAccountCreationTime() {
        return accountCreationTime;
    }

    public void setAccountCreationTime(Time accountCreationTime) {
        this.accountCreationTime = accountCreationTime;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public String getAccountCashType() {
        return accountCashType;
    }

    public void setAccountCashType(String accountCashType) {
        this.accountCashType = accountCashType;
    }

    public Time getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Time expirationTime) {
        this.expirationTime = expirationTime;
    }

    public Time getLastTransactionTime() {
        return lastTransactionTime;
    }

    public void setLastTransactionTime(Time lastTransactionTime) {
        this.lastTransactionTime = lastTransactionTime;
    }

    public String getExtensions() {
        return extensions;
    }

    public void setExtensions(String extensions) {
        this.extensions = extensions;
    }
}
