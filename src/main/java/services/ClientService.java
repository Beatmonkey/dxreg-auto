package services;

import com.google.gson.*;
import io.restassured.path.json.mapper.factory.JsonbObjectMapperFactory;
import io.restassured.response.Response;
import model.api.client.AccountsItem;
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
        List<AccountsItem> accounts = new ArrayList<>();

        AccountsItem newAccount = AccountsItem.builder()
                .clearingCode("default")
                .accountCode("newAutoAcc1")
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

}
