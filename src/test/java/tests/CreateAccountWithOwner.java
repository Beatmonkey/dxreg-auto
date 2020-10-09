package tests;

import config.AccConfig;
import config.UserConfig;
import model.api.Error;
import model.api.Result;
import model.api.client.Account;
import model.api.client.Category;
import model.api.client.Client;
import model.db.DbAccount;
import model.db.DbClient;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import services.AccountDbService;
import services.AccountService;
import services.ClientService;
import services.ClientDbService;

import java.util.ArrayList;
import java.util.List;

public class CreateAccountWithOwner {

    ClientService clientService = new ClientService();
    AccountService accountService = new AccountService();

    ClientDbService clientDbService = new ClientDbService();
    AccountDbService accountDbService = new AccountDbService();


    @Test
    @DisplayName("[MERCURYQA-4910] dxRegAPI - Create account for specified owner with credit line" +
            "[MERCURYQA-4861] dxRegAPI - Create new account of Live type for existent client user")
    public void testCreateAccountForSpecifiedOwnerWithCreditLine() {

        List<Client> result = clientService.getClients();

        Client randomAutoClient = result.stream()
                .filter(client -> client.getLogin().contains(UserConfig.label))
                .findAny()
                .get();


        Account newAccount = Account.builder()
                .clearingCode("default")
                .accountCode(randomAutoClient.getLogin() + System.currentTimeMillis())
                .brokerCode("root_broker")
                .type("CLIENT")
                .accountCashType("CASH")
                .accountType(AccConfig.ACCOUNT_LIVE_TYPE)
                .currency("USD")
                .credit(15000)
                .build();

        Account createdAccount = accountService.createNewAccountWithOwner(randomAutoClient.getLogin(), randomAutoClient.getDomain(), newAccount).getData();
        DbClient dbClient = clientDbService.getClientsByLogin(randomAutoClient.getLogin()).get(0);
        DbAccount dbAccount = accountDbService.getAccountByAccCodeClearingCode(createdAccount.getAccountCode(), createdAccount.getClearingCode());


        assertEquals(dbAccount.getOwnerId(), dbClient.getId());

    }

    @Test
    @DisplayName("[MERCURYQA-4906] dxRegAPI - Create new account for existent user with not defined accountType")
    public void testCreateAccountForSpecifiedOwnerWithoutDefinedAccountType() {

        List<Client> result = clientService.getClients();

        Client randomAutoClient = result.stream()
                .filter(client -> client.getLogin().contains(UserConfig.label))
                .findAny()
                .get();


        Account newAccount = Account.builder()
                .clearingCode("default")
                .accountCode(randomAutoClient.getLogin() + System.currentTimeMillis())
                .brokerCode("root_broker")
                .type("CLIENT")
                .accountCashType("CASH")
                .currency("USD")
                .credit(15000)
                .build();

        Account createdAccount = accountService.createNewAccountWithOwner(randomAutoClient.getLogin(), randomAutoClient.getDomain(), newAccount).getData();
        DbClient dbClient = clientDbService.getClientsByLogin(randomAutoClient.getLogin()).get(0);
        DbAccount dbAccount = accountDbService.getAccountByAccCodeClearingCode(createdAccount.getAccountCode(), createdAccount.getClearingCode());


        assertEquals(dbAccount.getOwnerId(), dbClient.getId());

    }

    @Test
    @DisplayName("[MERCURYQA-4879] dxRegAPI - Create account when specified owner is not found or not a client")
    public void testCreateAccountWhenSpecifiedOwnerIsNotFoundOrNotClient() {

        List<String> options = new ArrayList<>();
        options.add(RandomStringUtils.random(1));
        options.add("master_dealer");

        for (String option : options) {
            Account newAccount = Account.builder()
                    .clearingCode("default")
                    .accountCode(AccConfig.LABEL + System.currentTimeMillis())
                    .brokerCode("root_broker")
                    .type("CLIENT")
                    .accountCashType("CASH")
                    .currency("USD")
                    .credit(15000)
                    .build();

            Error error = accountService.createNewAccountWithOwner(option, "default", newAccount).getError();
            assertEquals(20014, error.errorCode);
            assertEquals("Specified owner is not found or not a client", error.errorMessage);

        }

    }

    @Test
    @DisplayName("[MERCURYQA-4871] dxRegAPI - Create account with specified owner and not existent category")
    public void testCreateAccountWithoutSpecifiedOwnerAndNotExistentCategory() {

        List<Client> result = clientService.getClients();

        Client randomAutoClient = result.stream()
                .filter(client -> client.getLogin().contains(UserConfig.label))
                .findAny()
                .get();

        Category categoryToCreate = Category.builder()
                .category("TEST")
                .value(RandomStringUtils.random(1))
                .build();

        List<Category> resultCategories = new ArrayList<>();
        resultCategories.add(categoryToCreate);

        Account newAccount = Account.builder()
                .accountCashType("MARGIN")
                .status("FULL_TRADING")
                .clearingCode(randomAutoClient.getDomain())
                .accountCode(AccConfig.LABEL + System.currentTimeMillis())
                .brokerCode("root_broker")
                .type("CLIENT")
                .categories(resultCategories)
                .currency("USD")
                .balance(1000)
                .build();

        Error error = accountService.createNewAccountWithOwner(randomAutoClient.getLogin(), randomAutoClient.getDomain(), newAccount).getError();
        assertEquals(30011, error.errorCode);
        assertEquals("Value is not supported for category", error.errorMessage);
    }



}
