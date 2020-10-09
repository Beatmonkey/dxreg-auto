package services;

import com.google.gson.Gson;
import config.RequestSpec;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public abstract class AbstractService {

    private static Gson gson = new Gson();



    protected RequestSpecification authRequest() {

        return given()
                .spec(RequestSpec.authDealerWithPermissions)
                .contentType(ContentType.JSON);
    }

    protected RequestSpecification authRequest(RequestSpecification requestSpecification) {

        return given()
                .spec(requestSpecification)
                .contentType(ContentType.JSON);
    }

}
