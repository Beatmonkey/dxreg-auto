package services;

import model.api.client.Account;
import model.api.client.Client;
import model.db.DbClient;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ClientGenerator {

    public DbClientService dbClientService;
    public static final String label = "newAutoUser";
    public static int counter;

    public ClientGenerator(DbClientService dbClientService) {
        this.dbClientService = dbClientService;
    }




    public Client generateClientWithAccounts(int numberOfAccounts) {
        List<Account> accounts = new ArrayList<>();

//        int clientCode = getLastCreatedClientId() + 1;
        long clientCode = System.currentTimeMillis();
        Client newClient = Client.builder()
                .domain("default")
                .login(label + clientCode)
                .accounts(accounts)
                .brokerCode("root_broker")
                .type("CLIENT")
                .fullName("UserAuto")
                .email("test@testauto.com")
                .password("1234567test")
                .passwordExpiry(0)
                .passwordReset(false)
                .build();



        long accountCode = clientCode + 1;
        for (int i = 1; i <= numberOfAccounts; i++) {
            Account newAccount = Account.builder()
                    .accountCashType("MARGIN")
                    .categories(new ArrayList<>())
                    .status("FULL_TRADING")
                    .clearingCode("default")
                    .accountCode(label + accountCode)
                    .brokerCode("root_broker")
                    .type("CLIENT")
                    .accountType("LIVE")
                    .currency("USD")
                    .balance(2000)
                    .build();

            accounts.add(newAccount);
            accountCode++;

        }

        counter++;
        return newClient;
    }

    public Client generateClientWithAccounts(int numberOfAccounts, String password, String email, String accountType) {

        List<Account> accounts = new ArrayList<>();
//        int clientCode = getLastCreatedClientId() + 1;
        Client newClient = Client.builder()
                .domain("default")
                .login(label + System.currentTimeMillis())
                .accounts(accounts)
                .brokerCode("root_broker")
                .type("CLIENT")
                .fullName("UserAuto")
                .email(email)
                .password(password)
                .passwordExpiry(0)
                .passwordReset(false)
                .build();

//        int accountCode = clientCode;
        for (int i = 1; i <= numberOfAccounts; i++) {
            Account newAccount = Account.builder()
                    .accountCashType("MARGIN")
                    .categories(new ArrayList<>())
                    .status("FULL_TRADING")
                    .clearingCode("default")
                    .accountCode(label + System.currentTimeMillis())
                    .brokerCode("root_broker")
                    .type("CLIENT")
                    .accountType(accountType)
                    .currency("USD")
                    .balance(1000)
                    .credit(1000)
                    .build();
            accounts.add(newAccount);
//            accountCode++;
        }

//        counter++;
        return newClient;

    }

    public Client createNewClientWithoutAccountGroup() {
        List<Account> accounts = new ArrayList<>();

//        int clientCode = getLastCreatedClientId() + 1;
        Client client =  Client.builder()
                .domain("default")
                .login(label + System.currentTimeMillis())
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
                .accountCode(label + System.currentTimeMillis())
                .brokerCode("root_broker")
                .type("CLIENT")
                .currency("USD")
                .balance(1000)
                .build();
        accounts.add(newAccount);
        counter++;
        return client;
    }


    public int getLastCreatedClientId() {

        if (counter == 0) {
            List<DbClient> result = dbClientService.getClientsByLogin(label + "%");
            DbClient lastClient = result.stream()
                    .max(Comparator.comparing(DbClient::getId))
                    .get();
            return counter = Integer.parseInt(lastClient.getName().replace(label, ""));

        } else {
            return counter;
        }
    }

}
