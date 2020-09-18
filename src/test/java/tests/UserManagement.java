package tests;

import config.AccConfig;
import model.api.Error;
import model.api.Result;
import model.api.client.Account;
import model.api.client.Client;

import model.db.DbClient;
import org.junit.jupiter.api.*;
import services.ClientGenerator;
import services.ClientService;
import services.DbClientService;
import services.ResponseCallback;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserManagement {

    DbClientService dxdemoqa = new DbClientService();
    ClientService clientService = new ClientService();
    ClientGenerator clientGenerator = new ClientGenerator(dxdemoqa);


    @Test
    public void testRequestAllClients() {

        List<Client> clientList = clientService.getClients();

        assertNotNull(clientList);
        assertFalse(clientList.isEmpty());
    }

    @Test
    @DisplayName("[MERCURYQA-4898] dxRegAPI - Create user with account")
    public void testCreateClientWithAccount() {
        Client newClient = clientGenerator.generateClientWithAccounts(1);

        Client createdClient = clientService.createNewClient(newClient).getData();


        DbClient dbClient = dxdemoqa.getClientsByLogin(createdClient.getLogin()).get(0);


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

        assertEquals(error.errorCode, 20006);
        assertEquals(error.errorMessage, "Account with given account code and clearing code already exists");
    }

    @Test
    @DisplayName("[MERCURYQA-4857] dxRegAPI - Create user with several accounts")
    public void testCreateClientWithSeveralAccounts() {
        Client newClient = clientGenerator.generateClientWithAccounts(2);


        Client createdClient = clientService.createNewClient(newClient).getData();

        DbClient dbClient = dxdemoqa.getClientsByLogin(createdClient.getLogin()).get(0);


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
        Client newClient = clientGenerator.generateClientWithAccounts(1, null, null, AccConfig.DEMO_TYPE);
        Client createdClient = clientService.createNewClient(newClient).getData();
        DbClient dbClient = dxdemoqa.getClientsByLogin(createdClient.getLogin()).get(0);

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
    @DisplayName("[MERCURYQA-4870] dxRegAPI - Create user with account of Live type")
    public void testCreateUserWithAccountTypeLive() {
        Client newClient = clientGenerator.generateClientWithAccounts(1, "1234567test", "test@test.com", AccConfig.LIVE_TYPE);
        Client createdClient = clientService.createNewClient(newClient).getData();
        DbClient dbClient = dxdemoqa.getClientsByLogin(createdClient.getLogin()).get(0);

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
    @DisplayName("[MERCURYQA-4862] dxRegAPI - Create user with account with not defined accountType when paperMoney group exists")
    public void testCreateUserWithAccountWithNotDefinedAccountTypePaperMoneyGroupExists() {
        Client newClient = clientGenerator.createNewClientWithoutAccountGroup();


        Client createdClient = clientService.createNewClient(newClient).getData();

        DbClient dbClient = dxdemoqa.getClientsByLogin(createdClient.getLogin()).get(0);


        assertEquals(createdClient.getAccounts().get(0).getAccountType(), "DEMO");
        assertTrue(createdClient.getAccounts().get(0).getCategories().isEmpty());

        //Compare data for User from REST model with USER from db response
        assertEquals(createdClient.getDomain(), dbClient.getDomain());
        assertEquals(createdClient.getLogin(), dbClient.getName());
        assertEquals(createdClient.getFullName(), dbClient.getFullName());
        assertEquals(createdClient.getEmail(), dbClient.getEmail());
    }


    @Test
    public void testRetrieveExistingClient() {

        Client retrievedClient = clientService.getClientInfo("newAutoUser15", "default");

        assertNotNull(retrievedClient);
        assertEquals("newAutoUser15", retrievedClient.getLogin());
        assertEquals("default", retrievedClient.getDomain());

    }
}
