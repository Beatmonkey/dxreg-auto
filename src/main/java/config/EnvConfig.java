package config;

public class EnvConfig {
    //REST API credentials
    public static final String HOST = "https://dxdemoqa.prosp.devexperts.com/dxweb/rest/api/register/";

    // 1. Dealer user having permissions to create accounts
    public static final String DEALER_DEFAULT_LOGIN = "master_dealer@default";
    public static final String DEALER_DEFAULT_PASSWORD = "test";

    // 2. Dealer user without permissions
    public static final String DEALER_WO_PERMISSION_LOGIN = "dealer_auto@default";
    public static final String DEALER_WO_PERMISSION_PASSWORD = "test";


    //  Database credentials
    public static final String DB_URL = "jdbc:postgresql://dxdemoqa.prosp.devexperts.com:5432/dxdemoqa?ssl=false";
    public static final String DB_USER = "dxdemoqa_core";
    public static final String DB_PASS = "dxdemoqa_core";

}
