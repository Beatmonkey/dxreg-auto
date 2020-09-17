package steps;


import model.api.Broker;
import org.junit.jupiter.api.*;
import services.BrokerService;

import java.util.List;


public class BrokerDefinition {


    @Test
    public void getBrokerData() {
        BrokerService brokerService = new BrokerService();

        List<Broker> brokers = brokerService.getBrokerData();

        Assertions.assertNotNull(brokers);
        Assertions.assertFalse(brokers.isEmpty());
    }




}
