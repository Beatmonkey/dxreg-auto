package steps;

import model.api.client.Client;
import org.junit.Assert;
import org.junit.Test;
import services.ClientService;

import java.util.List;

public class ClientDefinition {
    ClientService clientService = new ClientService();

    @Test
    public void testRequestAllClients() {

        List<Client> clientList = clientService.getClients();

        Assert.assertNotNull(clientList);
        Assert.assertFalse(clientList.isEmpty());
    }

    @Test
    public void testCreateClientWithAccounts() {
        String testLogin = "newAutoUser1";
        Client newClient = clientService.createNewClient("default", testLogin);

        Assert.assertNotNull(newClient);
        Assert.assertEquals(testLogin, newClient.getLogin());
    }
}
