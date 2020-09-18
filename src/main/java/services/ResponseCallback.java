package services;

import io.restassured.response.Response;
import model.api.Result;
import model.api.client.Client;

public interface ResponseCallback {

    Result<Client> call(Result<Client> result);
}
