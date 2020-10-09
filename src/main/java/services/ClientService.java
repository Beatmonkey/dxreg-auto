package services;

import com.google.gson.*;
import config.EnvConfig;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import model.api.Error;
import model.api.client.Client;
import model.api.Result;

import java.util.ArrayList;
import java.util.List;

public class ClientService extends AbstractService {

    private static Gson gson = new Gson();

    public static String CLIENTS = "client/";



    public List<Client> getClients() {

        Response response = authRequest()
                .get(CLIENTS)
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

    public Result<Client> createNewClient(Client client) {
        String result = gson.toJson(client);

        Response response = authRequest()
                .body(result)
                .post(EnvConfig.HOST + CLIENTS);

        return handleResponse(response);
    }

    public Result<Client> createNewClient(RequestSpecification requestSpecification, Client client) {
        String result = gson.toJson(client);

        Response response = authRequest(requestSpecification)
                .body(result)
                .post(EnvConfig.HOST + CLIENTS);

        return handleResponse(response);
    }


    public Client getClientInfo(String login, String domain) {
        Response response = authRequest()
                .get(EnvConfig.HOST + CLIENTS + domain + "/" + login)
                .then()
                .extract()
                .response();


        JsonElement jsonElement = JsonParser.parseString(response.body().asString()).getAsJsonObject();
        Client retrievedClient = gson.fromJson(jsonElement, Client.class);


        return retrievedClient;
    }

    private static Result<Client> handleResponse(Response response) {
        if (response.statusCode() == 200) {
            JsonElement joResponse = JsonParser.parseString(response.body().asString()).getAsJsonObject();
            Client createdClient = gson.fromJson(joResponse, Client.class);
            return Result.successful(createdClient);

        } else {
            JsonElement joResponse = JsonParser.parseString(response.body().asString()).getAsJsonObject();
            Error error = gson.fromJson(joResponse, Error.class);
            return Result.failed(error);
        }
    }
}



