package steps;

import model.api.Error;
import model.api.client.Account;
import model.api.client.Client;

import org.junit.jupiter.api.*;
import services.ClientService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ClientDefinition {
    ClientService clientService = new ClientService();

    @Test
    public void testRequestAllClients() {

        List<Client> clientList = clientService.getClients();

        assertNotNull(clientList);
        assertFalse(clientList.isEmpty());
    }

    @Test
    @DisplayName("[MERCURYQA-4898] dxRegAPI - Create user with account")
    public void testCreateClientWithAccount() {
        List<Account> accounts = new ArrayList<>();

        Client newClient = Client.builder()
                .domain("default")
                .login("newAutoUser17")
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
                .accountType("LIVE")
                .currency("USD")
                .balance(1000)
                .build();
        accounts.add(newAccount);


        Client createdClient = clientService.createNewClient(newClient, accounts).data;

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

        Error error = clientService.createNewClient(newClient, accounts).error;

        assertEquals(error.errorCode, 20006);
        assertEquals(error.errorMessage, "Account with given account code and clearing code already exists");
    }

    @Test
    @DisplayName("[MERCURYQA-4898] dxRegAPI - Create user with account")
    public void testCreateClientWithSeveralAccounts() {
        List<Account> accounts = new ArrayList<>();

        Client newClient = Client.builder()
                .domain("default")
                .login("newAutoUser17")
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
                .accountCode(newClient.getLogin())
                .brokerCode("root_broker")
                .type("CLIENT")
                .accountType("LIVE")
                .currency("USD")
                .balance(1000)
                .build();

        accounts.add(newAccount1);
        accounts.add(newAccount2);


        Client createdClient = clientService.createNewClient(newClient, accounts).data;

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
    }




    @Test
    public void testRetrieveExistingClient() {

        Client retrievedClient = clientService.getClientInfo("newAutoUser15", "default");

        assertNotNull(retrievedClient);
        assertEquals("newAutoUser15", retrievedClient.getLogin());
        assertEquals("default", retrievedClient.getDomain());

    }
}
