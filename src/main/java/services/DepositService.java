package services;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.restassured.response.Response;
import model.api.transfer.Deposit;
import model.api.transfer.Transfer;
import model.api.Error;
import model.api.Result;

public class DepositService extends AbstractService {


    private final String ACCOUNT_DEPOSIT = "account/{clearingCode}/{accountCode}/deposit/{id}";
    private final String ACCOUNT_WITHDRAWAL = "account/{clearingCode}/{accountCode}/withdrawal/{id}";

    Gson gson = new Gson();


    public Result<Transfer> makeDepositOrWithdrawal(String clearingCode, String accountCode, String id, Transfer transfer) {
        String result = gson.toJson(transfer);

        String endPoint = "";
        if (transfer instanceof Deposit) {
            endPoint = ACCOUNT_DEPOSIT;
        } else {
            endPoint = ACCOUNT_WITHDRAWAL;
        }

        Response response = authRequest()
                .pathParam("clearingCode", clearingCode)
                .pathParam("accountCode", accountCode)
                .pathParam("id", id)
                .body(result)
                .put(endPoint);

        if (response.getStatusCode() == 201) {
            JsonObject joResponse = JsonParser.parseString(response.body().asString()).getAsJsonObject();
            Transfer createdTransfer = gson.fromJson(joResponse, Transfer.class);
            return Result.successful(createdTransfer);
        } else {
            JsonObject joResponse = JsonParser.parseString(response.body().asString()).getAsJsonObject();
            Error error = gson.fromJson(joResponse, Error.class);
            return Result.failed(error);
        }

    }
}