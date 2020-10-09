package tests;

import config.UserConfig;
import model.api.transfer.Deposit;
import model.api.Error;
import model.api.transfer.Transfer;
import model.api.client.Account;
import model.api.client.Client;
import model.api.transfer.Withdrawal;
import model.db.Activity;
import model.db.ActivityLeg;
import model.db.DbAccount;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import services.*;

import java.util.List;


public class DepositAndWithdrawal {

    ClientService clientService = new ClientService();
    AccountService accountService = new AccountService();
    ClientDbService clientDbService = new ClientDbService();
    AccountDbService accountDbService = new AccountDbService();
    DepositService depositService = new DepositService();


    @Test
    @DisplayName("[MERCURYQA-4882] dxRegAPI - deposit service - transfer deposit")
    public void testCreateTransferDeposit() {

        List<Client> result = clientService.getClients();

        Client randomAutoClient = result.stream()
                .filter(client -> client.getLogin().contains(UserConfig.label))
                .findAny()
                .get();

        Account randomClientAccount = randomAutoClient.getAccounts().get(0);

        Transfer newDeposit = Deposit.builder()
                .amount(5555L)
                .currency(randomClientAccount.getCurrency())
                .description("autoDescDeposit")
                .build();

        String depositIdentifier = "newAutoDeposit" + System.currentTimeMillis();
        Transfer createdDeposit = depositService.makeDepositOrWithdrawal(randomAutoClient.getDomain(), randomClientAccount.getAccountCode(), depositIdentifier, newDeposit).getData();

        DbAccount dbAccount = accountDbService.getAccountByAccCodeClearingCode(randomClientAccount.getAccountCode(), randomAutoClient.getDomain());
        Activity depositActivity = accountDbService.getActivityByAccIdActionCode(dbAccount.getId(), depositIdentifier);

        String query = "select * from activity_legs where activity_id = (select id from activities where account_id =" + dbAccount.getId() + " and action_code ='" + depositIdentifier + "');";
        ActivityLeg activityLeg = accountDbService.getActivityLeg(query).get(0);


        assertEquals(depositActivity.getActionCode(), depositIdentifier);
        assertEquals(depositActivity.getDescription(), createdDeposit.getDescription());
        assertEquals(depositActivity.getActivityType(), "DEPOSIT");
        assertEquals(depositActivity.getChainActionType(), "INITIAL");

        assertEquals(createdDeposit.getAmount(), activityLeg.getQuantity());
        assertEquals(activityLeg.getLegType(), "CASH");


    }

    @Test
    @DisplayName("[MERCURYQA-4886] dxRegAPI - Withdraw for insufficient balance")
    public void testWithdrawForInsufficientBalance() {
        List<Client> result = clientService.getClients();

        Client randomAutoClient = result.stream()
                .filter(client -> client.getLogin().contains(UserConfig.label))
                .findAny()
                .get();

        Account randomClientAccount = randomAutoClient.getAccounts().get(0);

        Transfer newWithdrawal = Withdrawal.builder()
                .amount(99999999L)
                .currency(randomClientAccount.getCurrency())
                .description("autoDescWithdraw")
                .build();

        String withdrawalIdentifier = "newAutoDeposit" + System.currentTimeMillis();
        Error insufficientBalance = depositService.makeDepositOrWithdrawal(randomAutoClient.getDomain(), randomClientAccount.getAccountCode(), withdrawalIdentifier, newWithdrawal).getError();

        assertEquals(30005, insufficientBalance.errorCode);
        assertEquals("Insufficient account balance", insufficientBalance.errorMessage);
    }


    @Test
    @DisplayName("[MERCURYQA-4909] dxRegAPI - deposit service - error - required field is empty")
    public void testErrorRequiredFieldsEmpty() {
        List<Client> result = clientService.getClients();

        Client randomAutoClient = result.stream()
                .filter(client -> client.getLogin().contains(UserConfig.label))
                .findAny()
                .get();

        Account randomClientAccount = randomAutoClient.getAccounts().get(0);

        Transfer newWithdrawal = Deposit.builder()
                .amount(100L)
                .description("autoDescWithdraw")
                .build();

        String depositIdentifier = "newAutoDeposit" + System.currentTimeMillis();
        Error error = depositService.makeDepositOrWithdrawal(randomAutoClient.getDomain(), randomClientAccount.getAccountCode(), depositIdentifier, newWithdrawal).getError();

        assertEquals(20001, error.errorCode);
        assertEquals("Currency is not set", error.errorMessage);
    }

    @Test
    @DisplayName("[MERCURYQA-4883] dxRegAPI - Deposit for not existent account")
    public void testCreateDepositForNonExistentAccount() {

        Transfer newWithdrawal = Deposit.builder()
                .amount(100L)
                .description("autoDescWithdraw")
                .currency("USD")
                .build();

        String depositIdentifier = "newAutoDeposit" + System.currentTimeMillis();
        Error error = depositService.makeDepositOrWithdrawal(RandomStringUtils.random(1), RandomStringUtils.random(1), depositIdentifier, newWithdrawal).getError();

        assertEquals(30007, error.errorCode);
        assertEquals("Specified account is not found", error.errorMessage);
    }

    @Test
    @DisplayName("[MERCURYQA-4890] dxRegAPI - deposit service - error - invalid transfer amount")
    public void testCreateDepositTransferWithInvalidAmount() {

        List<Client> result = clientService.getClients();

        Client randomAutoClient = result.stream()
                .filter(client -> client.getLogin().contains(UserConfig.label))
                .findAny()
                .get();

        Account randomClientAccount = randomAutoClient.getAccounts().get(0);

        Transfer newWithdrawal = Deposit.builder()
                .amount(-100L)
                .description("autoDescWithdraw")
                .currency("USD")
                .build();

        String depositIdentifier = "newAutoDeposit" + System.currentTimeMillis();
        Error error = depositService.makeDepositOrWithdrawal(randomAutoClient.getDomain(), randomClientAccount.getAccountCode(), depositIdentifier, newWithdrawal).getError();

        assertEquals(20004, error.errorCode);
        assertEquals("Invalid transfer amount", error.errorMessage);

    }

    @Test
    @DisplayName("[MERCURYQA-4865] dxRegAPI - deposit service - error - not unique transfer id")
    public void testCreateDepositWithExistingTransferId() {
        List<Client> result = clientService.getClients();

        Client randomAutoClient = result.stream()
                .filter(client -> client.getLogin().contains(UserConfig.label))
                .findAny()
                .get();

        Account randomClientAccount = randomAutoClient.getAccounts().get(0);

        Transfer newDeposit = Deposit.builder()
                .amount(5555L)
                .currency(randomClientAccount.getCurrency())
                .description("autoDescDeposit")
                .build();

        String depositIdentifier = "newAutoDeposit" + System.currentTimeMillis();
        Transfer createdDeposit = depositService.makeDepositOrWithdrawal(randomAutoClient.getDomain(), randomClientAccount.getAccountCode(), depositIdentifier, newDeposit).getData();

        Error error = depositService.makeDepositOrWithdrawal(randomAutoClient.getDomain(), randomClientAccount.getAccountCode(), depositIdentifier, newDeposit).getError();

        assertEquals(30006, error.errorCode);
        assertEquals("Another transfer with specified id already exists", error.errorMessage);
    }

    @Test
    @DisplayName("[MERCURYQA-4858] dxRegAPI - deposit service - error - wrong deposit currency")
    public void testCreateDepositWithWrongCurrency() {

        List<Client> result = clientService.getClients();

        Client randomAutoClient = result.stream()
                .filter(client -> client.getLogin().contains(UserConfig.label))
                .findAny()
                .get();

        Account randomClientAccount = randomAutoClient.getAccounts().get(0);

        Transfer newWithdrawal = Deposit.builder()
                .amount(100L)
                .description("autoDescWithdraw")
                .currency("US_TEST")
                .build();

        String depositIdentifier = "newAutoDeposit" + System.currentTimeMillis();
        Error error = depositService.makeDepositOrWithdrawal(randomAutoClient.getDomain(), randomClientAccount.getAccountCode(), depositIdentifier, newWithdrawal).getError();

        assertEquals(20005, error.errorCode);
        assertEquals("Currency of transfer must be '" + randomClientAccount.getCurrency() + "'", error.errorMessage);

    }
}
