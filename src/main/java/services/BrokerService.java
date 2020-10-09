package services;


import com.google.gson.*;
import config.EnvConfig;
import io.restassured.response.Response;
import model.api.broker.Broker;
import model.api.Error;
import model.api.Result;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class BrokerService extends AbstractService {

    public static String BROKER = "broker/";

    Gson gson = new Gson();

    public List<Broker> getBrokerData() {

        Response response = authRequest()
                .get(EnvConfig.HOST + BROKER)
                .then().extract().response();

        String responseResult = response.getBody().asString();

        List<Broker> result = new ArrayList<>();

        JsonArray jsonArray = JsonParser.parseString(responseResult).getAsJsonArray();

        for (JsonElement elem : jsonArray) {
            Broker broker = gson.fromJson(elem, Broker.class);
            result.add(broker);
        }

        return result;
    }


    public Result<Broker> getBrokerByBrokerCode(String brokerCode) {
        Response response = authRequest()
                .get(EnvConfig.HOST + BROKER + brokerCode)
                .then().extract().response();

        if (response.getStatusCode() == 200) {
            JsonObject jo = JsonParser.parseString(response.body().asString()).getAsJsonObject();
            Broker retrievedBroker = gson.fromJson(jo, Broker.class);
            return Result.successful(retrievedBroker);
        } else {
            JsonElement joResponse = JsonParser.parseString(response.body().asString()).getAsJsonObject();
            Error error = gson.fromJson(joResponse, Error.class);
            return Result.failed(error);
        }
    }




}
