package services;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import model.api.Error;
import model.api.Result;
import model.api.client.Account;

import java.util.ArrayList;
import java.util.List;

public class AccountService extends AbstractService {


    private static final String GET_ALL_ACCOUNTS = "account/";
    private static final String CREATE_ACCOUNT = "account/"; //create new account
    private static final String CREATE_ACCOUNT_WITH_OWNER = "/client/{domain}/{login}/account/"; //create new account with specified owner
    private static final String ACCOUNT = "/account/{clearingCode}/{accountCode}";


    private static Gson gson = new Gson();


    public List<Account> getAccountsInfo() {

        Response response = authRequest()
                .get(GET_ALL_ACCOUNTS)
                .then().extract().response();

        String responseResult = response.getBody().asString();

        List<Account> result = new ArrayList<>();

        JsonArray jsonArray = JsonParser.parseString(responseResult).getAsJsonArray();

        for (JsonElement elem : jsonArray) {
            Account account = gson.fromJson(elem, Account.class);
            result.add(account);
        }

        return result;
    }


    public Result<Account> getAccountInfo(String clearingCode, String accountCode) {

        Response response = authRequest()
                .pathParam("clearingCode", clearingCode)
                .pathParam("accountCode", accountCode)
                .get(ACCOUNT, clearingCode, accountCode);


        return handleResponse(response);

    }


    public Result<Account> createNewAccountWithoutOwner(Account account) {
        String result = gson.toJson(account);

        Response response = authRequest()
                .body(result)
                .post(CREATE_ACCOUNT);

        return handleResponse(response);

    }

    public Result<Account> createNewAccountWithoutOwner(RequestSpecification requestSpecification, Account account) {
        String result = gson.toJson(account);

        Response response = authRequest(requestSpecification)
                .body(result)
                .post(CREATE_ACCOUNT);

        return handleResponse(response);

    }

    public Result<Account> createNewAccountWithOwner(String login, String domain, Account account) {
        String result = gson.toJson(account);

        Response response = authRequest()
                .body(result)
                .pathParam("login", login)
                .pathParam("domain", domain)
                .post(CREATE_ACCOUNT_WITH_OWNER, login, domain);

        return handleResponse(response);

    }


    public Result<Account> updateAccount(String clearingCode, String accountCode, Account account) {
        String result = gson.toJson(account);

        Response response = authRequest()
                .body(result)
                .pathParam("clearingCode", clearingCode)
                .pathParam("accountCode", accountCode)
                .put(ACCOUNT, clearingCode, accountCode);


        return handleResponse(response);

    }

    private static Result<Account> handleResponse(Response response) {
        if (response.statusCode() == 200) {
            JsonElement joResponse = JsonParser.parseString(response.asString()).getAsJsonObject();
            Account createdAccount = gson.fromJson(joResponse, Account.class);
            return Result.successful(createdAccount);
        } else {
            JsonElement joResponse = JsonParser.parseString(response.asString()).getAsJsonObject();
            Error error = gson.fromJson(joResponse, Error.class);
            return Result.failed(error);
        }
    }


}
