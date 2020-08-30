package steps;

import config.EnvConfig;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import model.api.Broker;
import org.junit.Assert;
import org.junit.Test;
import services.BrokerService;

import java.util.List;

import static io.restassured.RestAssured.given;

public class BrokerDefinition {


    @Test
    public void getBrokerData() {
        BrokerService brokerService = new BrokerService();

        List<Broker> brokers = brokerService.getBrokerData();

        Assert.assertNotNull(brokers);
        Assert.assertFalse(brokers.isEmpty());
    }




}
