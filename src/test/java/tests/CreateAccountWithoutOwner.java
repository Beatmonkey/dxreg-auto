package tests;


import config.AccConfig;
import model.api.Error;
import model.api.client.Account;
import model.api.client.Category;
import model.db.DbAccount;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.*;
import services.AccountDbService;
import services.AccountService;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

public class CreateAccountWithoutOwner {

    AccountService accountService = new AccountService();
    AccountDbService accountDbService = new AccountDbService();


    @Test
    @DisplayName("[MERCURYQA-4902] dxRegAPI - Create account without specified owner and with credit line")
    public void testCreateAccountWithoutSpecifiedOwnerAndWithCredit() {

        Account newAccount = Account.builder()
                .clearingCode("default")
                .accountCode(AccConfig.LABEL + System.currentTimeMillis())
                .brokerCode("root_broker")
                .type("CLIENT")
                .accountType(AccConfig.ACCOUNT_LIVE_TYPE)
                .currency("USD")
                .credit(15000)
                .build();


        Account createdAccount = accountService.createNewAccountWithoutOwner(newAccount).getData();
        DbAccount createdDbAccount = accountDbService.getAccountByAccCodeClearingCode(createdAccount.getAccountCode(), createdAccount.getClearingCode());

        assertEquals(newAccount.getClearingCode(), createdAccount.getClearingCode());
        assertEquals(newAccount.getAccountCode(), createdAccount.getAccountCode());
        assertEquals(newAccount.getBrokerCode(), createdAccount.getBrokerCode());
        assertEquals(newAccount.getAccountType(), createdAccount.getAccountType());
        assertEquals(newAccount.getCurrency(), createdAccount.getCurrency());
        assertEquals(newAccount.getCredit(), createdAccount.getCredit());


        //Compare User data from rest to data from Db
        assertEquals(createdAccount.getClearingCode(), createdDbAccount.getClearingCode());
        assertEquals(createdAccount.getAccountCode(), createdDbAccount.getAccountCode());
        assertEquals(createdDbAccount.getOwnerId(), 0);

    }

    @Test
    @DisplayName("[MERCURYQA-4872] dxRegAPI - Create account with defined accountCashType")
    public void testCreateAccountWithDefinedAccountCashType() {
        List<String> options = new ArrayList<>();

        options.add(AccConfig.CASH_TYPE_CASH);
        options.add(AccConfig.CASH_TYPE_MARGIN);

        for (String option : options) {
            Account newAccount = Account.builder()
                    .clearingCode("default")
                    .accountCode(AccConfig.LABEL + System.currentTimeMillis())
                    .brokerCode("root_broker")
                    .type("CLIENT")
                    .accountCashType(option)
                    .accountType(AccConfig.ACCOUNT_LIVE_TYPE)
                    .currency("USD")
                    .credit(15000)
                    .build();

            Account createdAccount = accountService.createNewAccountWithoutOwner(newAccount).getData();
            DbAccount createdDbAccount = accountDbService.getAccountByAccCodeClearingCode(createdAccount.getAccountCode(), createdAccount.getClearingCode());

            assertEquals(newAccount.getClearingCode(), createdAccount.getClearingCode());
            assertEquals(newAccount.getAccountCode(), createdAccount.getAccountCode());
            assertEquals(newAccount.getBrokerCode(), createdAccount.getBrokerCode());
            assertEquals(newAccount.getAccountType(), createdAccount.getAccountType());
            assertEquals(newAccount.getCurrency(), createdAccount.getCurrency());
            assertEquals(newAccount.getCredit(), createdAccount.getCredit());


            //Compare User data from rest to data from Db
            assertEquals(createdAccount.getClearingCode(), createdDbAccount.getClearingCode());
            assertEquals(createdAccount.getAccountCode(), createdDbAccount.getAccountCode());
            assertEquals(createdAccount.getAccountCashType(), createdAccount.getAccountCashType());
            assertEquals(createdDbAccount.getOwnerId(), 0);
        }
    }

    @Test
    @DisplayName("[MERCURYQA-4859] dxRegAPI - Create account with not defined type")
    public void testCreateAccountWithNotDefinedType() {

        Account newAccount = Account.builder()
                .clearingCode("default")
                .accountCode(AccConfig.LABEL + System.currentTimeMillis())
                .brokerCode("root_broker")
                .currency("USD")
                .build();

        Account createdAccount = accountService.createNewAccountWithoutOwner(newAccount).getData();
        DbAccount createdDbAccount = accountDbService.getAccountByAccCodeClearingCode(createdAccount.getAccountCode(), createdAccount.getClearingCode());

        assertEquals(newAccount.getClearingCode(), createdAccount.getClearingCode());
        assertEquals(newAccount.getAccountCode(), createdAccount.getAccountCode());
        assertEquals(newAccount.getBrokerCode(), createdAccount.getBrokerCode());
        assertEquals(AccConfig.ACCOUNT_DEMO_TYPE, createdAccount.getAccountType());
        assertEquals(newAccount.getCurrency(), createdAccount.getCurrency());
        assertEquals(AccConfig.TYPE_CLIENT, createdAccount.getType());


        //Compare User data from rest to data from Db
        assertEquals(createdAccount.getClearingCode(), createdDbAccount.getClearingCode());
        assertEquals(createdAccount.getAccountCode(), createdDbAccount.getAccountCode());
        assertEquals(createdAccount.getAccountCashType(), createdAccount.getAccountCashType());
        assertEquals(createdDbAccount.getOwnerId(), 0);
    }


    @Test
    @DisplayName("[MERCURYQA-4894] dxRegAPI - Create account services - check account not created when wrong accountCashType was set")
    public void testCreateAccountWhenWrongAccountCashTypeSet() {
        Account newAccount = Account.builder()
                .clearingCode("default")
                .accountCode(AccConfig.LABEL + System.currentTimeMillis())
                .brokerCode("root_broker")
                .accountCashType("TEST")
                .currency("USD")
                .build();


        Error error = accountService.createNewAccountWithoutOwner(newAccount).getError();
        assertEquals(20022, error.errorCode);
        assertEquals("Incorrect value for AccountCashType", error.errorMessage);

    }

    @Test
    @DisplayName("[MERCURYQA-4873] dxRegAPI - Create account services - Success - ExpirationDate")
    public void testCreateAccountWithExpirationDateManuallySet() {
        Account newAccount = Account.builder()
                .clearingCode("default")
                .accountCode(AccConfig.LABEL + System.currentTimeMillis())
                .brokerCode("root_broker")
                .accountCashType("MARGIN")
                .expirationDate("2022-05-02")
                .currency("USD")
                .build();


        Account createdAccount = accountService.createNewAccountWithoutOwner(newAccount).getData();
        assertEquals(newAccount.getExpirationDate(), createdAccount.getExpirationDate());

    }

    @Test
    @DisplayName("[MERCURYQA-4867] dxRegAPI - Create account when Specified broker is not found in dxCore")
    public void testCreateAccountWhenSpecifiedBrokerNotFoundInDxCore() {
        Account newAccount = Account.builder()
                .clearingCode("default")
                .accountCode(AccConfig.LABEL + System.currentTimeMillis())
                .brokerCode(RandomStringUtils.random(1))
                .accountCashType(AccConfig.CASH_TYPE_MARGIN)
                .currency("USD")
                .build();


        Error error = accountService.createNewAccountWithoutOwner(newAccount).getError();
        assertEquals(30008, error.errorCode);
        assertEquals("Broker with specified broker code not found", error.errorMessage);

    }

    @Test
    @DisplayName("[MERCURYQA-4903] dxRegAPI - Create account services - Error 400 - Currency with specified code is not found")
    public void testErrorCurrencyWithSpecifiedCodeIsNotFound() {
        Account newAccount = Account.builder()
                .clearingCode("default")
                .accountCode(AccConfig.LABEL + System.currentTimeMillis())
                .brokerCode("root_broker")
                .accountCashType(AccConfig.CASH_TYPE_MARGIN)
                .currency("INCORRECT")
                .build();


        Error error = accountService.createNewAccountWithoutOwner(newAccount).getError();
        assertEquals(20003, error.errorCode);
        assertEquals("Currency INCORRECT not found", error.errorMessage);

    }

    @Test
    @DisplayName("[MERCURYQA-4891] dxRegAPI - Create account services - Error 400 - Account already exists")
    public void testErrorAccountAlreadyExists() {
        List<Account> result = accountService.getAccountsInfo();

        Account randomAccount = result
                .stream().findAny().get();

        Account newAccount = Account.builder()
                .clearingCode(randomAccount.getClearingCode())
                .accountCode(randomAccount.getAccountCode())
                .brokerCode("root_broker")
                .accountCashType(AccConfig.CASH_TYPE_MARGIN)
                .currency("USD")
                .build();


        Error error = accountService.createNewAccountWithoutOwner(newAccount).getError();
        assertEquals(20006, error.errorCode);
        assertEquals("Account with given account code and clearing code already exists", error.errorMessage);

    }

    @Test
    @DisplayName("[MERCURYQA-4876] dxRegAPI - Create account services - Error 400 - Some of the required field is empty")
    public void testErrorCreateAccountSomeOfRequiredFieldsNotSet() {

        Account newAccount = Account.builder()
                .clearingCode("default")
                .accountCode(AccConfig.LABEL + System.currentTimeMillis())
                .brokerCode("root_broker")
                .accountCashType(AccConfig.CASH_TYPE_MARGIN)
                .build();


        Error error = accountService.createNewAccountWithoutOwner(newAccount).getError();
        assertEquals(20001, error.errorCode);
        assertEquals("Currency is not set", error.errorMessage);

    }

    @Test
    @DisplayName("[MERCURYQA-4866] dxRegAPI - Create account services - Error 400 - Non-empty Expiration Date for live account")
    public void testErrorNonEmptyExpirationDateForLiveAccount() {

        Account newAccount = Account.builder()
                .clearingCode("default")
                .accountCode(AccConfig.LABEL + System.currentTimeMillis())
                .brokerCode("root_broker")
                .accountCashType("MARGIN")
                .expirationDate("2022-05-02")
                .accountType(AccConfig.ACCOUNT_LIVE_TYPE)
                .currency("USD")
                .build();


        Error error = accountService.createNewAccountWithoutOwner(newAccount).getError();
        assertEquals(20021, error.errorCode);
        assertEquals("Non-empty Expiration Date for live account", error.errorMessage);

    }

    @Test
    @DisplayName("[MERCURYQA-4877] dxRegAPI - Create account with not defined brokerCode")
    public void testCreateAccountWithNotDefinedBrokerCode() {


            Account newAccount = Account.builder()
                    .clearingCode("default")
                    .accountCode(AccConfig.LABEL + System.currentTimeMillis())
                    .type("CLIENT")
                    .accountCashType(AccConfig.CASH_TYPE_MARGIN)
                    .accountType(AccConfig.ACCOUNT_LIVE_TYPE)
                    .currency("USD")
                    .credit(15000)
                    .build();

            Account createdAccount = accountService.createNewAccountWithoutOwner(newAccount).getData();
            DbAccount createdDbAccount = accountDbService.getAccountByAccCodeClearingCode(createdAccount.getAccountCode(), createdAccount.getClearingCode());

            assertEquals(newAccount.getClearingCode(), createdAccount.getClearingCode());
            assertEquals(newAccount.getAccountCode(), createdAccount.getAccountCode());
            assertEquals("root_broker", createdAccount.getBrokerCode());
            assertEquals(newAccount.getAccountType(), createdAccount.getAccountType());
            assertEquals(newAccount.getCurrency(), createdAccount.getCurrency());
            assertEquals(newAccount.getCredit(), createdAccount.getCredit());


            //Compare User data from rest to data from Db
            assertEquals(createdAccount.getClearingCode(), createdDbAccount.getClearingCode());
            assertEquals(createdAccount.getAccountCode(), createdDbAccount.getAccountCode());
            assertEquals(createdAccount.getAccountCashType(), createdAccount.getAccountCashType());
            assertEquals(createdDbAccount.getOwnerId(), 0);

    }

    @Test
    @DisplayName("[MERCURYQA-4875] dxRegAPI - Create account with not defined accountCashType")
    public void testCreateAccountWithNotDefinedAccountCashType() {

        Account newAccount = Account.builder()
                .clearingCode("default")
                .accountCode(AccConfig.LABEL + System.currentTimeMillis())
                .type("CLIENT")
                .brokerCode("root_broker")
                .accountType(AccConfig.ACCOUNT_LIVE_TYPE)
                .currency("USD")
                .credit(15000)
                .build();

        Account createdAccount = accountService.createNewAccountWithoutOwner(newAccount).getData();
        DbAccount createdDbAccount = accountDbService.getAccountByAccCodeClearingCode(createdAccount.getAccountCode(), createdAccount.getClearingCode());

        assertEquals(newAccount.getClearingCode(), createdAccount.getClearingCode());
        assertEquals(newAccount.getAccountCode(), createdAccount.getAccountCode());
        assertEquals(newAccount.getBrokerCode(), createdAccount.getBrokerCode());
        assertEquals(newAccount.getAccountType(), createdAccount.getAccountType());
        assertEquals(newAccount.getCurrency(), createdAccount.getCurrency());
        assertEquals(newAccount.getCredit(), createdAccount.getCredit());
        assertEquals("MARGIN", createdAccount.getAccountCashType());


        //Compare User data from rest to data from Db
        assertEquals(createdAccount.getClearingCode(), createdDbAccount.getClearingCode());
        assertEquals(createdAccount.getAccountCode(), createdDbAccount.getAccountCode());
        assertEquals(createdAccount.getAccountCashType(), createdAccount.getAccountCashType());

    }

    @Test
    @DisplayName("[MERCURYQA-4881] dxRegAPI - Create account services - Success - ClearingCode - Default value")
    public void testCreateAccountWithoutClearingCodeForNonRootBroker() {

        Account newAccount = Account.builder()
                .accountCode(AccConfig.LABEL + System.currentTimeMillis())
                .type("CLIENT")
                .brokerCode("BD3501:1")
                .accountType(AccConfig.ACCOUNT_LIVE_TYPE)
                .currency("USD")
                .credit(15000)
                .build();

        Account createdAccount = accountService.createNewAccountWithoutOwner(newAccount).getData();
        DbAccount createdDbAccount = accountDbService.getAccountByAccCodeClearingCode(createdAccount.getAccountCode(), createdAccount.getClearingCode());

        assertEquals("TF", createdAccount.getClearingCode());
        assertEquals(newAccount.getAccountCode(), createdAccount.getAccountCode());
        assertEquals(newAccount.getBrokerCode(), createdAccount.getBrokerCode());
        assertEquals(newAccount.getAccountType(), createdAccount.getAccountType());
        assertEquals(newAccount.getCurrency(), createdAccount.getCurrency());
        assertEquals(newAccount.getCredit(), createdAccount.getCredit());
        assertEquals("MARGIN", createdAccount.getAccountCashType());


        //Compare User data from rest to data from Db
        assertEquals(createdAccount.getClearingCode(), createdDbAccount.getClearingCode());
        assertEquals(createdAccount.getAccountCode(), createdDbAccount.getAccountCode());
        assertEquals(createdAccount.getAccountCashType(), createdAccount.getAccountCashType());

    }

    @Test
    @DisplayName("[MERCURYQA-4901] dxRegAPI - Create account services - Success - Type - CLIENT")
    public void testCreateAccountWithTypeClientForNonRootBroker() {

        Account newAccount = Account.builder()
                .accountCode(AccConfig.LABEL + System.currentTimeMillis())
                .type("CLIENT")
                .brokerCode("BD3501:1")
                .accountType(AccConfig.ACCOUNT_LIVE_TYPE)
                .clearingCode("default")
                .currency("USD")
                .credit(15000)
                .build();



        Account createdAccount = accountService.createNewAccountWithoutOwner(newAccount).getData();
        DbAccount createdDbAccount = accountDbService.getAccountByAccCodeClearingCode(createdAccount.getAccountCode(), createdAccount.getClearingCode());

        assertEquals(newAccount.getClearingCode(), createdAccount.getClearingCode());
        assertEquals(newAccount.getAccountCode(), createdAccount.getAccountCode());
        assertEquals(newAccount.getBrokerCode(), createdAccount.getBrokerCode());
        assertEquals(newAccount.getAccountType(), createdAccount.getAccountType());
        assertEquals(newAccount.getCurrency(), createdAccount.getCurrency());
        assertEquals(newAccount.getCredit(), createdAccount.getCredit());
        assertEquals("MARGIN", createdAccount.getAccountCashType());


        //Compare User data from rest to data from Db
        assertEquals(createdAccount.getClearingCode(), createdDbAccount.getClearingCode());
        assertEquals(createdAccount.getAccountCode(), createdDbAccount.getAccountCode());
        assertEquals(createdAccount.getAccountCashType(), createdAccount.getAccountCashType());
    }

    @Test
    @DisplayName("[MERCURYQA-4885] dxRegAPI - Create account without specified owner and not existent category")
    public void testCreateAccountWithoutSpecifiedOwnerAndNotExistentCategory() {
        Category categoryToCreate = Category.builder()
                .category("TEST")
                .value(RandomStringUtils.random(1))
                .build();

        List<Category> result = new ArrayList<>();
        result.add(categoryToCreate);

        Account newAccount = Account.builder()
                .accountCashType("MARGIN")
                .status("FULL_TRADING")
                .clearingCode("default")
                .accountCode(AccConfig.LABEL)
                .brokerCode("root_broker")
                .type("CLIENT")
                .categories(result)
                .currency("USD")
                .balance(1000)
                .build();

        Error error = accountService.createNewAccountWithoutOwner(newAccount).getError();
        assertEquals(30011, error.errorCode);
        assertEquals("Value is not supported for category", error.errorMessage);
    }


}
