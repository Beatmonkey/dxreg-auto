package services;

import config.EnvConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.request;

public abstract class AbstractService {

    protected RequestSpecification authRequest() {
        return given()
                .auth()
                .preemptive()
                .basic(EnvConfig.USERNAME, EnvConfig.PASSWORD)
                .header("Accept", ContentType.JSON.getAcceptHeader())
                .contentType(ContentType.JSON);
    }
}
