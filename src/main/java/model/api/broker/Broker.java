package model.api.broker;
import lombok.*;

@Data
public class Broker {
	private String brokerCode;
	private String parentBrokerCode;
	private String countryCode;
	private String dealerPrincipalGroup;
	private String name;
	private String namespace;
	private String description;
	private String clientPrincipalGroup;
	private String type;
}
