package model.db;

import java.sql.Time;

public class Activity {

    private int accountId;
    private int version;
    private int id;
    private String sessionCode;
    private String actionCode;
    private int userId;
    private int userSessionId;
    private String activityType;
    private String chainActionType;
    private int orderId;
    private int orderChainId;
    private Time createdTime;
    private String description;
    private String localizableDescription;
    private int refActivityId;
    private int activityChainId;
    private int quantity;
    private String orderStatus;
    private String orderStatusDetails;
    private String routeCode;
    private String externalParameters;
    private int trailPrice;
    private int limitPrice;
    private int stopPrice;
    private String linkedPositionCode;
    private int linkedInstrumentId;
    private String parameters;

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSessionCode() {
        return sessionCode;
    }

    public void setSessionCode(String sessionCode) {
        this.sessionCode = sessionCode;
    }

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserSessionId() {
        return userSessionId;
    }

    public void setUserSessionId(int userSessionId) {
        this.userSessionId = userSessionId;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getChainActionType() {
        return chainActionType;
    }

    public void setChainActionType(String chainActionType) {
        this.chainActionType = chainActionType;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getOrderChainId() {
        return orderChainId;
    }

    public void setOrderChainId(int orderChainId) {
        this.orderChainId = orderChainId;
    }

    public Time getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Time createdTime) {
        this.createdTime = createdTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocalizableDescription() {
        return localizableDescription;
    }

    public void setLocalizableDescription(String localizableDescription) {
        this.localizableDescription = localizableDescription;
    }

    public int getRefActivityId() {
        return refActivityId;
    }

    public void setRefActivityId(int refActivityId) {
        this.refActivityId = refActivityId;
    }

    public int getActivityChainId() {
        return activityChainId;
    }

    public void setActivityChainId(int activityChainId) {
        this.activityChainId = activityChainId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatusDetails() {
        return orderStatusDetails;
    }

    public void setOrderStatusDetails(String orderStatusDetails) {
        this.orderStatusDetails = orderStatusDetails;
    }

    public String getRouteCode() {
        return routeCode;
    }

    public void setRouteCode(String routeCode) {
        this.routeCode = routeCode;
    }

    public String getExternalParameters() {
        return externalParameters;
    }

    public void setExternalParameters(String externalParameters) {
        this.externalParameters = externalParameters;
    }

    public int getTrailPrice() {
        return trailPrice;
    }

    public void setTrailPrice(int trailPrice) {
        this.trailPrice = trailPrice;
    }

    public int getLimitPrice() {
        return limitPrice;
    }

    public void setLimitPrice(int limitPrice) {
        this.limitPrice = limitPrice;
    }

    public int getStopPrice() {
        return stopPrice;
    }

    public void setStopPrice(int stopPrice) {
        this.stopPrice = stopPrice;
    }

    public String getLinkedPositionCode() {
        return linkedPositionCode;
    }

    public void setLinkedPositionCode(String linkedPositionCode) {
        this.linkedPositionCode = linkedPositionCode;
    }

    public int getLinkedInstrumentId() {
        return linkedInstrumentId;
    }

    public void setLinkedInstrumentId(int linkedInstrumentId) {
        this.linkedInstrumentId = linkedInstrumentId;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public Time getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(Time transactionTime) {
        this.transactionTime = transactionTime;
    }

    public String getExecutionContext() {
        return executionContext;
    }

    public void setExecutionContext(String executionContext) {
        this.executionContext = executionContext;
    }

    public String getExecutionDestination() {
        return executionDestination;
    }

    public void setExecutionDestination(String executionDestination) {
        this.executionDestination = executionDestination;
    }

    public String getExtensions() {
        return extensions;
    }

    public void setExtensions(String extensions) {
        this.extensions = extensions;
    }

    private Time transactionTime;
    private String executionContext;
    private String executionDestination;
    private String extensions;


}
