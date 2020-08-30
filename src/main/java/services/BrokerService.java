package services;


import com.google.gson.*;
import io.restassured.response.Response;
import model.api.Broker;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class BrokerService extends AbstractService {

    Gson gson = new Gson();

    public List<Broker> getBrokerData() {

        Response response = authRequest()
                .get("https://dxdemoqa.prosp.devexperts.com/dxweb/rest/api/register/broker")
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




}
