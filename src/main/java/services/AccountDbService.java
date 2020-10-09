package services;

import model.db.Activity;
import model.db.ActivityLeg;
import model.db.DbAccount;
import model.db.DbClient;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AccountDbService extends AbstractDbService {


    public DbAccount getAccountByAccCodeClearingCode(String accountCode, String clearingCode) {
        try {
            Statement statement = this.getConnection().createStatement();
            String query = "select * from accounts where account_code='" + accountCode + "' and clearing_code='" + clearingCode + "';";
            ResultSet rs = statement.executeQuery(query);
            DbAccount dbAccount = new DbAccount();

            while (rs.next()) {
                dbAccount.setId(rs.getInt("id"));
                dbAccount.setVersion(rs.getInt("version"));
                dbAccount.setClearingCode(rs.getString("clearing_code"));
                dbAccount.setAccountCode(rs.getString("account_code"));
                dbAccount.setCurrencyId(rs.getInt("currency_id"));
                dbAccount.setStatus(rs.getString("status"));
                dbAccount.setAccountCreationTime(rs.getTime("account_creation_time"));
                dbAccount.setOwnerId(rs.getInt("owner_id"));
                dbAccount.setAccountCashType(rs.getString("account_cash_type"));
                dbAccount.setExpirationTime(rs.getTime("expiration_time"));
                dbAccount.setLastTransactionTime(rs.getTime("last_transaction_time"));
                dbAccount.setExtensions(rs.getString("extensions"));

            }
            return dbAccount;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.printf("Failed.");
            return new DbAccount();
        }
    }


    public Activity getActivityByAccIdActionCode(int accountId, String actionCode) {
        try {
            Statement statement = this.getConnection().createStatement();
            String query = "select * from activities where account_id =" + accountId + " and action_code ='" + actionCode + "';";
            ResultSet rs = statement.executeQuery(query);
            Activity activity = new Activity();
            while (rs.next()) {
                activity.setAccountId(rs.getInt("account_id"));
                activity.setSessionCode(rs.getString("session_code"));
                activity.setActionCode(rs.getString("action_code"));
                activity.setDescription(rs.getString("description"));
                activity.setUserId(rs.getInt("user_id"));
                activity.setCreatedTime(rs.getTime("created_time"));
                activity.setQuantity(rs.getInt("quantity"));
                activity.setActivityType(rs.getString("activity_type"));
                activity.setChainActionType(rs.getString("chain_action_type"));
            }
            return activity;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed.");
            return new Activity();
        }
    }


    public List<ActivityLeg> getActivityLeg(String query) {
        try {
            Statement statement = this.getConnection().createStatement();
            ResultSet rs = statement.executeQuery(query);

            List<ActivityLeg> result = new ArrayList<>();
            while (rs.next()) {
                ActivityLeg activityLeg = new ActivityLeg();
                activityLeg.setActivityId(rs.getInt("activity_id"));
                activityLeg.setInstrumentId(rs.getInt("instrument_id"));
                activityLeg.setQuantity(rs.getDouble("quantity"));
                activityLeg.setPrice(rs.getDouble("price"));
                activityLeg.setTriggerPrice(rs.getDouble("trigger_price"));
                activityLeg.setCost(rs.getDouble("cost"));
                activityLeg.setSpotPrice(rs.getDouble("spot_price"));
                activityLeg.setPositionCode(rs.getString("position_code"));
                activityLeg.setRateType(rs.getString("rate_type"));
                activityLeg.setLegType(rs.getString("leg_type"));
                activityLeg.setLegSubType(rs.getString("leg_sub_type"));
                activityLeg.setLeg_id(rs.getInt("leg_id"));

                result.add(activityLeg);
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed.");
            return new ArrayList<>();
        }
    }


}
