package services;

import com.google.gson.*;
import io.restassured.response.Response;
import model.api.client.Account;
import model.api.client.Client;

import java.util.ArrayList;
import java.util.List;

public class ClientService extends AbstractService {

    Gson gson = new Gson();

    public List<Client> getClients() {

        Response response = authRequest()
                .get("https://dxdemoqa.prosp.devexperts.com/dxweb/rest/api/register/client")
                .then().extract().response();

        String responseResult = response.getBody().asString();

        List<Client> result = new ArrayList<>();

        JsonArray jsonArray = JsonParser.parseString(responseResult).getAsJsonArray();

        for (JsonElement elem : jsonArray) {
            Client client = gson.fromJson(elem, Client.class);
            result.add(client);
        }

        return result;
    }

    public Client createNewClient(String domain, String login) {
        List<Account> accounts = new ArrayList<>();

        Account newAccount = Account.builder()
                .clearingCode("default")
                .accountCode("newAutoAcc14")
                .brokerCode("root_broker")
                .type("CLIENT")
                .accountType("LIVE")
                .currency("USD")
                .balance(1000)
                .build();

        accounts.add(newAccount);


        Client newClient = Client.builder()
                .domain(domain)
                .login(login)
                .accounts(accounts)
                .brokerCode("root_broker")
                .type("CLIENT")
                .password("1234567qwe")
                .build();

        String result = gson.toJson(newClient);

        Response response = authRequest()
                .body(result)
                .post("https://dxdemoqa.prosp.devexperts.com/dxweb/rest/api/register/client")
                .then().extract().response();

        JsonElement joResponse = JsonParser.parseString(response.body().asString()).getAsJsonObject();

        Client createdClient = gson.fromJson(joResponse, Client.class);

        return createdClient;
    }

    public Client getClientInfo(String login, String domain) {
        Response response = authRequest()
                .get("https://dxdemoqa.prosp.devexperts.com/dxweb/rest/api/register/client/" + domain + "/" + login)
                .then().extract().response();
        JsonElement jsonElement = JsonParser.parseString(response.body().asString()).getAsJsonObject();


        Client retrievedClient = gson.fromJson(jsonElement, Client.class);


        return retrievedClient;
    }


}
