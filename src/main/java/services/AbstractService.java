package services;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class AbstractService {

    protected RequestSpecification authRequest() {
        return given()
                .auth()
                .preemptive()
                .basic("master_dealer@default", "test")
                .header("Accept", ContentType.JSON.getAcceptHeader())
                .contentType(ContentType.JSON);
    }
}
