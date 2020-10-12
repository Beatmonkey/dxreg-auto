package tests;

import config.AccConfig;
import config.RequestSpec;
import config.UserConfig;
import model.api.Error;
import model.api.client.Account;
import model.api.client.Category;
import model.api.client.Client;

import model.db.DbClient;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.*;
import services.AccountService;
import services.ClientService;
import services.ClientDbService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserManagement {

    ClientDbService clientDbService = new ClientDbService();
    ClientService clientService = new ClientService();
    AccountService accountService = new AccountService();

    @Test
    @DisplayName("[MERCURYQA-4898] dxRegAPI - Create user with account")
    public void testCreateClientWithAccount() {
        List<Account> accounts = new ArrayList<>();
        Client newClient = Client.builder()
                .domain("default")
                .login(UserConfig.label + System.currentTimeMillis())
                .accounts(accounts)
                .brokerCode("root_broker")
                .type("CLIENT")
                .fullName("UserAuto")
                .email("test@testauto.com")
                .password("1234567test")
                .passwordExpiry(0)
                .passwordReset(false)
                .build();

        Account newAccount = Account.builder()
                .accountCashType("MARGIN")
                .categories(new ArrayList<>())
                .status("FULL_TRADING")
                .clearingCode("default")
                .accountCode(newClient.getLogin())
                .brokerCode("root_broker")
                .type("CLIENT")
                .accountType(AccConfig.ACCOUNT_LIVE_TYPE)
                .currency("USD")
                .balance(1000)
                .build();
        accounts.add(newAccount);


        Client createdClient = clientService.createNewClient(newClient).getData();
        DbClient dbClient = clientDbService.getClientsByLogin(createdClient.getLogin()).get(0);


        // Client assertion
        assertNotNull(newClient);
        assertEquals(createdClient.getDomain(), newClient.getDomain());
        assertEquals(createdClient.getLogin(), newClient.getLogin());
        assertEquals(createdClient.getBrokerCode(), newClient.getBrokerCode());
        assertEquals(createdClient.getType(), newClient.getType());
        assertEquals(createdClient.getFullName(), newClient.getFullName());
        assertEquals(createdClient.getEmail(), newClient.getEmail());
        assertEquals(createdClient.getPasswordExpiry(), newClient.getPasswordExpiry());
        assertEquals(createdClient.isPasswordReset(), newClient.isPasswordReset());

        // Account assertion
        assertEquals(createdClient.getAccounts(), newClient.getAccounts());

        //Compare data for User from REST model with USER from db response
        assertEquals(createdClient.getDomain(), dbClient.getDomain());
        assertEquals(createdClient.getLogin(), dbClient.getName());
        assertEquals(createdClient.getFullName(), dbClient.getFullName());
        assertEquals(createdClient.getEmail(), dbClient.getEmail());

    }

    @Test
    @DisplayName("[MERCURYQA-4899] dxRegAPI - Create user - Existing account")
    public void testCreateUserWithExistingAccount() {
        List<Account> accounts = new ArrayList<>();
        Client newClient = Client.builder()
                .domain("default")
                .login("newAutoUser19")
                .accounts(accounts)
                .brokerCode("root_broker")
                .type("CLIENT")
                .fullName("UserAuto")
                .email("test@testauto.com")
                .password("1234567test")
                .passwordExpiry(0)
                .passwordReset(false)
                .build();

        Account newAccount = Account.builder()
                .accountCashType("MARGIN")
                .categories(new ArrayList<>())
                .status("FULL_TRADING")
                .clearingCode("default")
                .accountCode("newAutoUser18")
                .brokerCode("root_broker")
                .type("CLIENT")
                .accountType("LIVE")
                .currency("USD")
                .balance(1000)
                .build();
        accounts.add(newAccount);

        Error error = clientService.createNewClient(newClient).getError();

        assertEquals(20006, error.errorCode);
        assertEquals("Account with given account code and clearing code already exists", error.errorMessage);
    }

    @Test
    @DisplayName("[MERCURYQA-4857] dxRegAPI - Create user with several accounts")
    public void testCreateClientWithSeveralAccounts() {
        List<Account> accounts = new ArrayList<>();
        Client newClient = Client.builder()
                .domain("default")
                .login(UserConfig.label + System.currentTimeMillis())
                .accounts(accounts)
                .brokerCode("root_broker")
                .type("CLIENT")
                .fullName("UserAuto")
                .email("test@testauto.com")
                .password("1234567test")
                .passwordExpiry(0)
                .passwordReset(false)
                .build();

        Account newAccount1 = Account.builder()
                .accountCashType("MARGIN")
                .categories(new ArrayList<>())
                .status("FULL_TRADING")
                .clearingCode("default")
                .accountCode(newClient.getLogin())
                .brokerCode("root_broker")
                .type("CLIENT")
                .accountType("LIVE")
                .currency("USD")
                .balance(1000)
                .build();

        Account newAccount2 = Account.builder()
                .accountCashType("MARGIN")
                .categories(new ArrayList<>())
                .status("FULL_TRADING")
                .clearingCode("default")
                .accountCode(newClient.getLogin() + 1)
                .brokerCode("root_broker")
                .type("CLIENT")
                .accountType("LIVE")
                .currency("USD")
                .balance(1000)
                .build();

        accounts.add(newAccount1);
        accounts.add(newAccount2);

        Client createdClient = clientService.createNewClient(newClient).getData();

        DbClient dbClient = clientDbService.getClientsByLogin(createdClient.getLogin()).get(0);


        // Client assertion
        assertNotNull(createdClient);
        assertEquals(createdClient.getDomain(), newClient.getDomain());
        assertEquals(createdClient.getLogin(), newClient.getLogin());
        assertEquals(createdClient.getBrokerCode(), newClient.getBrokerCode());
        assertEquals(createdClient.getType(), newClient.getType());
        assertEquals(createdClient.getFullName(), newClient.getFullName());
        assertEquals(createdClient.getEmail(), newClient.getEmail());
        assertEquals(createdClient.getPasswordExpiry(), newClient.getPasswordExpiry());
        assertEquals(createdClient.isPasswordReset(), newClient.isPasswordReset());

        // Account assertion
        assertEquals(createdClient.getAccounts().get(0), newClient.getAccounts().get(0));
        assertEquals(createdClient.getAccounts().get(1), newClient.getAccounts().get(1));

        //Compare data for User from REST model with USER from db response
        assertEquals(createdClient.getDomain(), dbClient.getDomain());
        assertEquals(createdClient.getLogin(), dbClient.getName());
        assertEquals(createdClient.getFullName(), dbClient.getFullName());
        assertEquals(createdClient.getEmail(), dbClient.getEmail());


    }


    @Test
    @DisplayName("[MERCURYQA-4863] dxRegAPI - Create user without password and email, with credit")
    public void testCreateUserWithoutPasswordAndEmail() {
        List<Account> accounts = new ArrayList<>();
        Client newClient = Client.builder()
                .domain("default")
                .login(UserConfig.label + System.currentTimeMillis())
                .accounts(accounts)
                .brokerCode("root_broker")
                .type("CLIENT")
                .fullName("UserAuto")
                .passwordExpiry(0)
                .passwordReset(false)
                .build();

        Account newAccount1 = Account.builder()
                .accountCashType("MARGIN")
                .categories(new ArrayList<>())
                .status("FULL_TRADING")
                .clearingCode("default")
                .accountCode(newClient.getLogin())
                .brokerCode("root_broker")
                .type("CLIENT")
                .accountType("LIVE")
                .currency("USD")
                .credit(1000)
                .balance(1000)
                .build();

        accounts.add(newAccount1);

        Client createdClient = clientService.createNewClient(newClient).getData();
        DbClient dbClient = clientDbService.getClientsByLogin(createdClient.getLogin()).get(0);

        // Client assertion
        assertNotNull(createdClient);
        assertEquals(createdClient.getDomain(), newClient.getDomain());
        assertEquals(createdClient.getLogin(), newClient.getLogin());
        assertEquals(createdClient.getBrokerCode(), newClient.getBrokerCode());
        assertEquals(createdClient.getType(), newClient.getType());
        assertEquals(createdClient.getFullName(), newClient.getFullName());
        assertEquals(createdClient.getEmail(), newClient.getEmail());
        assertEquals(createdClient.getPasswordExpiry(), newClient.getPasswordExpiry());
        assertEquals(createdClient.isPasswordReset(), newClient.isPasswordReset());

        // Account assertion
        assertEquals(createdClient.getAccounts(), newClient.getAccounts());

        //Compare data for User from REST model with USER from db response
        assertEquals(createdClient.getDomain(), dbClient.getDomain());
        assertEquals(createdClient.getLogin(), dbClient.getName());
        assertEquals(createdClient.getFullName(), dbClient.getFullName());
        assertEquals(createdClient.getEmail(), dbClient.getEmail());
    }

    @Test
    @DisplayName("[MERCURYQA-4870] dxRegAPI - Create user with account of Live type")
    public void testCreateUserWithAccountTypeLive() {
        List<Account> accounts = new ArrayList<>();
        Client newClient = Client.builder()
                .domain("default")
                .login(UserConfig.label + System.currentTimeMillis())
                .accounts(accounts)
                .brokerCode("root_broker")
                .type("CLIENT")
                .fullName("UserAuto")
                .email("test@testauto.com")
                .password("1234567test")
                .passwordExpiry(0)
                .passwordReset(false)
                .build();

        Account newAccount = Account.builder()
                .accountCashType("MARGIN")
                .categories(new ArrayList<>())
                .status("FULL_TRADING")
                .clearingCode("default")
                .accountCode(newClient.getLogin())
                .brokerCode("root_broker")
                .type("CLIENT")
                .accountType(AccConfig.ACCOUNT_LIVE_TYPE)
                .currency("USD")
                .balance(1000)
                .build();
        accounts.add(newAccount);

        Client createdClient = clientService.createNewClient(newClient).getData();
        DbClient dbClient = clientDbService.getClientsByLogin(createdClient.getLogin()).get(0);

        // Client assertion
        assertNotNull(createdClient);
        assertEquals(createdClient.getDomain(), newClient.getDomain());
        assertEquals(createdClient.getLogin(), newClient.getLogin());
        assertEquals(createdClient.getBrokerCode(), newClient.getBrokerCode());
        assertEquals(createdClient.getType(), newClient.getType());
        assertEquals(createdClient.getFullName(), newClient.getFullName());
        assertEquals(createdClient.getEmail(), newClient.getEmail());
        assertEquals(createdClient.getPasswordExpiry(), newClient.getPasswordExpiry());
        assertEquals(createdClient.isPasswordReset(), newClient.isPasswordReset());

        // Account assertion
        assertEquals(createdClient.getAccounts(), newClient.getAccounts());

        //Compare data for User from REST model with USER from db response
        assertEquals(createdClient.getDomain(), dbClient.getDomain());
        assertEquals(createdClient.getLogin(), dbClient.getName());
        assertEquals(createdClient.getFullName(), dbClient.getFullName());
        assertEquals(createdClient.getEmail(), dbClient.getEmail());
    }

    @Test
    @DisplayName("[MERCURYQA-4862] dxRegAPI - Create user with account with not defined accountType when paperMoney group exists")
    public void testCreateUserWithAccountWithNotDefinedAccountTypePaperMoneyGroupExists() {

        List<Account> accounts = new ArrayList<>();
        Client newClient = Client.builder()
                .domain("default")
                .login(UserConfig.label + System.currentTimeMillis())
                .accounts(accounts)
                .brokerCode("root_broker")
                .type("CLIENT")
                .fullName("UserAuto")
                .email("test@testauto.com")
                .password("1234567test")
                .passwordExpiry(0)
                .passwordReset(false)
                .build();

        Account newAccount = Account.builder()
                .accountCashType("MARGIN")
                .categories(new ArrayList<>())
                .status("FULL_TRADING")
                .clearingCode("default")
                .accountCode(newClient.getLogin())
                .brokerCode("root_broker")
                .type("CLIENT")
                .currency("USD")
                .balance(1000)
                .build();
        accounts.add(newAccount);


        Client createdClient = clientService.createNewClient(newClient).getData();

        DbClient dbClient = clientDbService.getClientsByLogin(createdClient.getLogin()).get(0);


        assertEquals(createdClient.getAccounts().get(0).getAccountType(), AccConfig.ACCOUNT_DEMO_TYPE);
        assertTrue(createdClient.getAccounts().get(0).getCategories().isEmpty());
        //Compare data for User from REST model with USER from db response
        assertEquals(createdClient.getDomain(), dbClient.getDomain());
        assertEquals(createdClient.getLogin(), dbClient.getName());
        assertEquals(createdClient.getFullName(), dbClient.getFullName());
        assertEquals(createdClient.getEmail(), dbClient.getEmail());
    }


    @Test
    @DisplayName("[MERCURYQA-4900] dxRegAPI - Create user with account without brokerCode")
    public void testCreateUserWithAccountWithDifferentBrokerCode() {
        List<Account> accounts = new ArrayList<>();
        Client newClient = Client.builder()
                .domain("default")
                .login(UserConfig.label + System.currentTimeMillis())
                .accounts(accounts)
                .brokerCode("root_broker")
                .type("CLIENT")
                .fullName("UserAuto")
                .email("test@testauto.com")
                .password("1234567test")
                .passwordExpiry(0)
                .passwordReset(false)
                .build();

        Account newAccount = Account.builder()
                .accountCashType("MARGIN")
                .categories(new ArrayList<>())
                .status("FULL_TRADING")
                .clearingCode("default")
                .accountCode(UserConfig.label + System.currentTimeMillis())
                .brokerCode("test123456")
                .type("CLIENT")
                .accountType("LIVE")
                .currency("USD")
                .balance(1000)
                .build();
        accounts.add(newAccount);

        Error error = clientService.createNewClient(newClient).getError();

        assertEquals(20019, error.errorCode);
        assertEquals("Account broker code should be the same as client broker code", error.errorMessage);

    }

    @Test
    @DisplayName("[MERCURYQA-4900] dxRegAPI - Create user with account without brokerCode")
    public void testCreateNewUserWithMissedRequiredFields() {
        List<Account> accounts = new ArrayList<>();
        Client newClient = Client.builder()
                .domain("default")
                .login("")
                .accounts(accounts)
                .brokerCode("root_broker")
                .type("CLIENT")
                .fullName("UserAuto")
                .email("test@testauto.com")
                .password("1234567test")
                .passwordExpiry(0)
                .passwordReset(false)
                .build();


        Error error = clientService.createNewClient(newClient).getError();

        assertEquals(20001, error.errorCode);
        assertEquals("Login is not set", error.errorMessage);

    }

    @Test
    @DisplayName("[MERCURYQA-4868] dxRegAPI - Create user - Duplicate values")
    public void testCreateUserWithDuplicatedValues() {

        List<Client> result = clientService.getClients();

        Client randomAutoClient = result.stream()
                .filter(client -> client.getLogin().contains(UserConfig.label))
                .findAny()
                .get();

        Client newClient = Client.builder()
                .domain("default")
                .login(randomAutoClient.getLogin())
                .brokerCode("root_broker")
                .type("CLIENT")
                .fullName("UserAuto")
                .email("test@testauto.com")
                .password("1234567test")
                .passwordExpiry(0)
                .passwordReset(false)
                .build();


        Error error = clientService.createNewClient(newClient).getError();

        assertEquals(error.errorCode, 20007);
        assertEquals("Client with given login already exists", error.errorMessage);
    }

    @Test
    @DisplayName("[MERCURYQA-4907] dxRegAPI - Create user by dealer without permissions to create users")
    public void testCreateUserByDealerWithoutPermissionsToCreateUsers() {


        Client newClient = Client.builder()
                .domain("default")
                .login(UserConfig.label + System.currentTimeMillis())
                .brokerCode("root_broker")
                .type("CLIENT")
                .fullName("UserAuto")
                .email("test@testauto.com")
                .password("1234567test")
                .passwordExpiry(0)
                .passwordReset(false)
                .build();

        Error error = clientService.createNewClient(RequestSpec.authDealerWithoutPermissions, newClient).getError();
        assertEquals(20020, error.errorCode);
        assertEquals("No permission to create principals at domains: default", error.errorMessage);
    }

    @Test
    @DisplayName("[MERCURYQA-4884] dxRegAPI - Create user with account by dealer without permissions to create accounts")
    public void testCreateUserWithAccountByDealerWithoutPermissionsToCreateAccounts() {

        List<Client> result = clientService.getClients();

        Client randomAutoClient = result.stream()
                .filter(client -> client.getLogin().contains(UserConfig.label))
                .findAny()
                .get();

        Account newAccount = Account.builder()
                .accountCashType("MARGIN")
                .categories(new ArrayList<>())
                .status("FULL_TRADING")
                .clearingCode("default")
                .accountCode(randomAutoClient.getLogin())
                .brokerCode("root_broker")
                .type("CLIENT")
                .accountType("LIVE")
                .currency("USD")
                .balance(1000)
                .build();

        Error error = accountService.createNewAccountWithoutOwner(RequestSpec.authDealerWithoutPermissions, newAccount).getError();
        assertEquals(20020, error.errorCode);
        assertEquals("No permission to create accounts: " + randomAutoClient.getLogin(), error.errorMessage);

    }

    @Test
    @DisplayName("[MERCURYQA-4874] dxRegAPI - Create user with not valid brokerCode value")
    public void testCreateUserWithNotValidBrokerCodeValue() {

        List<Account> accounts = new ArrayList<>();
        Client newClient = Client.builder()
                .domain("default")
                .login(UserConfig.label + System.currentTimeMillis())
                .accounts(accounts)
                .brokerCode(RandomStringUtils.random(1))
                .type("CLIENT")
                .fullName("UserAuto")
                .email("test@testauto.com")
                .password("1234567test")
                .passwordExpiry(0)
                .passwordReset(false)
                .build();

        Account newAccount = Account.builder()
                .accountCashType("MARGIN")
                .categories(new ArrayList<>())
                .status("FULL_TRADING")
                .clearingCode("default")
                .accountCode(newClient.getLogin())
                .brokerCode(newClient.getBrokerCode())
                .type("CLIENT")
                .currency("USD")
                .balance(1000)
                .build();
        accounts.add(newAccount);

        Error error = clientService.createNewClient(newClient).getError();
        assertEquals(30008, error.errorCode);
        assertEquals("Broker with specified broker code not found", error.errorMessage);

    }

    @Test
    @DisplayName("[MERCURYQA-4855] dxRegAPI - Create user with Non-existing Currency")
    public void testCreateUserWithAccountWithNonExistingCurrency() {

        List<Account> accounts = new ArrayList<>();
        Client newClient = Client.builder()
                .domain("default")
                .login(UserConfig.label + System.currentTimeMillis())
                .accounts(accounts)
                .brokerCode("root_broker")
                .type("CLIENT")
                .fullName("UserAuto")
                .email("test@testauto.com")
                .password("1234567test")
                .passwordExpiry(0)
                .passwordReset(false)
                .build();

        Account newAccount = Account.builder()
                .accountCashType("MARGIN")
                .categories(new ArrayList<>())
                .status("FULL_TRADING")
                .clearingCode("default")
                .accountCode(newClient.getLogin())
                .brokerCode(newClient.getBrokerCode())
                .type("CLIENT")
                .currency("US")
                .balance(1000)
                .build();
        accounts.add(newAccount);

        Error error = clientService.createNewClient(newClient).getError();
        assertEquals(20003, error.errorCode);
        assertEquals("Currency US not found", error.errorMessage);
    }


    @Test
    @DisplayName("[MERCURYQA-4856] dxRegAPI - Create client with not existent category")
    public void testCreateClientWithNotExistentCategory() {

        List<Account> accounts = new ArrayList<>();
        Client newClient = Client.builder()
                .domain("default")
                .login(UserConfig.label + System.currentTimeMillis())
                .accounts(accounts)
                .brokerCode("root_broker")
                .type("CLIENT")
                .fullName("UserAuto")
                .email("test@testauto.com")
                .password("1234567test")
                .passwordExpiry(0)
                .passwordReset(false)
                .build();

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
                .accountCode(newClient.getLogin())
                .brokerCode(newClient.getBrokerCode())
                .type("CLIENT")
                .categories(result)
                .currency("USD")
                .balance(1000)
                .build();
        accounts.add(newAccount);

        Error error = clientService.createNewClient(newClient).getError();
        assertEquals(30011, error.errorCode);
        assertEquals("Value is not supported for category", error.errorMessage);

    }
}
