package services;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import io.restassured.response.Response;
import model.api.Error;
import model.api.Result;
import model.api.UpdateCategory;
import model.api.broker.BrokerCategory;
import model.api.broker.Value;
import model.api.client.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryService extends AbstractService {

    private static Gson gson = new Gson();

    private final String CATEGORY_FOR_ACCOUNT = "/account/{clearingCode}/{accountCode}/category/{categoryCode}";
    private final String CATEGORY_PER_BROKER = "/broker/{brokerCode}/category/"; //Get all categories per broker, Create new, Rename or delete category
    private final String RETRIEVE_PER_BROKER = "/broker/{brokerCode}/category/{categoryCode}"; //Retrieve category by broker
    private final String CATEGORY_PER_SYSTEM = "/category";


    public List<BrokerCategory> getAllSettingsCategories() {
        List<BrokerCategory> result = new ArrayList<>();

        Response response = authRequest()
                .get(CATEGORY_PER_SYSTEM);

        if (response.getStatusCode() == 200) {

            String responseResult = response.getBody().asString();
            JsonArray jsonArrayCategories = JsonParser.parseString(responseResult).getAsJsonArray();

            for (JsonElement jsonCategory : jsonArrayCategories) {
                BrokerCategory category = gson.fromJson(jsonCategory, BrokerCategory.class);
                result.add(category);
            }

        }
        return result;
    }


    public Result<List<BrokerCategory>> getAllBrokerCategories(String brokerCode) {
        List<BrokerCategory> result = new ArrayList<>();

        Response response = authRequest()
                .pathParam("brokerCode", brokerCode)
                .get(CATEGORY_PER_BROKER, brokerCode);

        if (response.getStatusCode() == 200) {

            String responseResult = response.getBody().asString();
            JsonArray jsonArrayCategories = JsonParser.parseString(responseResult).getAsJsonArray();

            for (JsonElement jsonCategory : jsonArrayCategories) {
                BrokerCategory category = gson.fromJson(jsonCategory, BrokerCategory.class);
                result.add(category);
            }
            return Result.successful(result);

        } else {
            JsonElement joResponse = JsonParser.parseString(response.asString()).getAsJsonObject();
            Error error = gson.fromJson(joResponse, Error.class);
            return Result.failed(error);
        }
    }


    public Result<BrokerCategory> getBrokerCategory(String brokerCode, String categoryCode) {

        Response response = authRequest()
                .pathParam("brokerCode", brokerCode)
                .pathParam("categoryCode", categoryCode)
                .get(RETRIEVE_PER_BROKER, brokerCode, categoryCode);

        if (response.getStatusCode() == 200) {
            JsonElement jo = JsonParser.parseString(response.asString()).getAsJsonObject();
            BrokerCategory brokerCategory = gson.fromJson(jo, BrokerCategory.class);
            return Result.successful(brokerCategory);
        } else {
            JsonElement joResponse = JsonParser.parseString(response.asString()).getAsJsonObject();
            Error error = gson.fromJson(joResponse, Error.class);
            return Result.failed(error);
        }


    }

    public Result<Category> createNewAccountCategory(String brokerCode, Category category) {

        String result = gson.toJson(category);

        Response response = authRequest()
                .body(result)
                .pathParam("brokerCode", brokerCode)
                .post(CATEGORY_PER_BROKER, brokerCode);

//        String resp = response.prettyPrint();


        return handleResponse(response);
    }

    public Result<Category> renameAccountCategory(String brokerCode, UpdateCategory updateCategory) {

        String result = gson.toJson(updateCategory);

        Response response = authRequest()
                .body(result)
                .pathParam("brokerCode", brokerCode)
                .put(CATEGORY_PER_BROKER, brokerCode);


        return handleResponse(response);
    }

    public Result<Integer> deleteAccountCategory(String brokerCode, Category category) {

        String result = gson.toJson(category);

        Response response = authRequest()
                .body(result)
                .pathParam("brokerCode", brokerCode)
                .delete(CATEGORY_PER_BROKER, brokerCode);


//        response.prettyPrint();

        if (response.getStatusCode() == 204) {
            /*JsonElement jo = JsonParser.parseString(response.asString()).getAsJsonObject();
            Integer code = gson.fromJson(jo, Integer.class);*/

            Integer code = response.getStatusCode();
            return Result.successful(code);
        } else {
            JsonElement joResponse = JsonParser.parseString(response.asString()).getAsJsonObject();
            Error error = gson.fromJson(joResponse, Error.class);
            return Result.failed(error);
        }
    }


    public Result<Integer> deleteCategoryForAccount(String clearingCode, String accountCode, Category category) {

        String result = gson.toJson(category);

        Response response = authRequest()
                .body(result)
                .pathParam("clearingCode", clearingCode)
                .pathParam("accountCode", accountCode)
                .pathParam("categoryCode", category.getCategory())
                .delete(CATEGORY_FOR_ACCOUNT, clearingCode, accountCode, category.getCategory());

        if (response.getStatusCode() == 204) {
            Integer code = response.getStatusCode();
            return Result.successful(code);
        } else {
            JsonElement joResponse = JsonParser.parseString(response.asString()).getAsJsonObject();
            Error error = gson.fromJson(joResponse, Error.class);
            return Result.failed(error);
        }
    }


    public Result<Integer> setCategoryForAccount(String clearingCode, String accountCode, String categoryCode, Value value) {

        String result = gson.toJson(value);

        Response response = authRequest()
                .body(result)
                .pathParam("clearingCode", clearingCode)
                .pathParam("accountCode", accountCode)
                .pathParam("categoryCode", categoryCode)
                .put(CATEGORY_FOR_ACCOUNT, clearingCode, accountCode, categoryCode);


        if (response.getStatusCode() == 204) {
            Integer code = response.getStatusCode();
            return Result.successful(code);
        } else {
            JsonElement joResponse = JsonParser.parseString(response.asString()).getAsJsonObject();
            Error error = gson.fromJson(joResponse, Error.class);
            return Result.failed(error);
        }
    }

    public Result<Category> getCategoryForAccount(String clearingCode, String accountCode, Category category) {

        String result = gson.toJson(category);

        Response response = authRequest()
                .body(result)
                .pathParam("clearingCode", clearingCode)
                .pathParam("accountCode", accountCode)
                .pathParam("categoryCode", category.getCategory())
                .get(CATEGORY_FOR_ACCOUNT, clearingCode, accountCode, category.getCategory());


        return handleResponse(response);

    }

    private static Result<Category> handleResponse(Response response) {

        if (response.getStatusCode() == 200) {
            JsonElement jo = JsonParser.parseString(response.asString()).getAsJsonObject();
            Category setCategoryOld = gson.fromJson(jo, Category.class);
            return Result.successful(setCategoryOld);
        } else {
            JsonElement joResponse = JsonParser.parseString(response.asString()).getAsJsonObject();
            Error error = gson.fromJson(joResponse, Error.class);
            return Result.failed(error);
        }

    }
}
