package tests;


import model.api.Broker;
import org.junit.jupiter.api.*;
import services.BrokerService;

import java.util.List;


public class BrokerManagement {


    @Test
    public void getBrokerData() {
        BrokerService brokerService = new BrokerService();

        List<Broker> brokers = brokerService.getBrokerData();

        Assertions.assertNotNull(brokers);
        Assertions.assertFalse(brokers.isEmpty());
    }




}
