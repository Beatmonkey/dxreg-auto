package tests;

import config.AccConfig;
import config.UserConfig;
import model.api.Error;
import model.api.client.Account;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import services.AccountService;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import static org.junit.jupiter.api.Assertions.*;

public class AccountManagement {

    AccountService accountService = new AccountService();

    @Test
    @DisplayName("[MERCURYQA-6244] dxRegAPI - Update account status")
    public void testUpdateAccountStatus() {

        LinkedList<String> statuses = new LinkedList<>();
        statuses.add(AccConfig.STATUS_FULL_TRADING);
        statuses.add(AccConfig.STATUS_CLOSE_ONLY);
        statuses.add(AccConfig.STATUS_NO_TRADING);
        statuses.add(AccConfig.STATUS_TERMINATED);


        for (String status : statuses) {
            List<Account> accounts = accountService.getAccountsInfo();
            Account randomAccount = accounts.stream()
                    .filter(account -> account.getAccountCode().contains(UserConfig.label))
                    .findAny()
                    .orElseThrow(() -> new NoSuchElementException("Nothing found!"));

            randomAccount.setStatus(status);

            Account updatedAccount = accountService.updateAccount(randomAccount.getClearingCode(), randomAccount.getAccountCode(), randomAccount).getData();

            assertEquals(randomAccount.getStatus(), updatedAccount.getStatus());
        }
    }

    @Test
    @DisplayName("[MERCURYQA-6245] dxRegAPI - Update account with not existent account")
    public void testUpdateAccountWithNotExistingAccount() {
        Account newAccount = Account.builder()
                .clearingCode("default")
                .accountCode(AccConfig.LABEL + System.currentTimeMillis())
                .type("CLIENT")
                .brokerCode("root_broker")
                .accountType(AccConfig.ACCOUNT_LIVE_TYPE)
                .currency("USD")
                .credit(15000)
                .build();

        Error error = accountService.updateAccount(RandomStringUtils.random(1), RandomStringUtils.random(1), newAccount).getError();

        assertEquals(30007, error.errorCode);
        assertEquals("Specified account is not found", error.errorMessage);

    }





}

