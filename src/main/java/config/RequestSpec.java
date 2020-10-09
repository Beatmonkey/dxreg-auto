package config;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.preemptive;

import io.restassured.specification.RequestSpecification;


public class RequestSpec {

    public static final RequestSpecification authDealerWithPermissions = new RequestSpecBuilder()
            .setBaseUri(EnvConfig.HOST)
            .setAuth(preemptive().basic(EnvConfig.DEALER_DEFAULT_LOGIN, EnvConfig.DEALER_DEFAULT_PASSWORD))
            .addHeader("Accept", ContentType.JSON.getAcceptHeader())
            .build();

    public static final RequestSpecification authDealerWithoutPermissions = new RequestSpecBuilder()
            .setBaseUri(EnvConfig.HOST)
            .setAuth(preemptive().basic(EnvConfig.DEALER_WO_PERMISSION_LOGIN, EnvConfig.DEALER_WO_PERMISSION_PASSWORD))
            .addHeader("Accept", ContentType.JSON.getAcceptHeader())
            .build();
}
