package tests;

import model.api.Error;
import model.api.Result;
import model.api.UpdateCategory;
import model.api.broker.BrokerCategory;
import model.api.broker.Value;
import model.api.client.Account;
import model.api.client.Category;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.*;
import services.AccountService;
import services.CategoryService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class CategoriesManagement {

    static Account account;
    static List<BrokerCategory> brokerCategories;
    static AccountService accountService = new AccountService();
    static CategoryService categoryService = new CategoryService();


    @BeforeAll
    static void createAccountToTestCategories() {
        //get some account
        account = accountService.getAccountsInfo()
                .stream()
                .filter(account -> account.getAccountCode().contains("newAccAuto"))
                .findAny()
                .orElseThrow(NoSuchElementException::new);


        brokerCategories = categoryService.getAllBrokerCategories(account.getBrokerCode()).getData();

    }

    @BeforeEach
    void deleteAccountCategories() {
        List<Category> accountCategories = accountService.getAccountInfo(account.getClearingCode(), account.getAccountCode()).getData().getCategories();

        if (!accountCategories.isEmpty()) {
            for (Category category : accountCategories) {
                categoryService.deleteCategoryForAccount(account.getClearingCode(), account.getAccountCode(), category);
            }
        }
    }

    @Test
    @DisplayName("[MERCURYQA-4893] dxRegAPI - Set account category")
    public void testSetAccountCategory() {


        BrokerCategory brokerCategory = brokerCategories.stream()
                .findAny().orElseThrow(() -> {
                    System.out.println("Broker categories list is empty");
                    return new NoSuchElementException();
                });


        Value value = brokerCategory.getValues().stream()
                .findAny().orElseThrow(() -> {
                    System.out.println("Values for the brokerCategory are absent");
                    return new NoSuchElementException();
                });


        categoryService.setCategoryForAccount(account.getClearingCode(), account.getAccountCode(), brokerCategory.getCategory(), value);


        Account updatedAccount = accountService.getAccountInfo(account.getClearingCode(), account.getAccountCode()).getData();
        Category createdCategory = updatedAccount.getCategories().get(0);


        assertEquals(brokerCategory.getCategory(), createdCategory.getCategory());
        assertEquals(value.getValue(), createdCategory.getValue());

    }

    @Test
    @DisplayName("[MERCURYQA-4878] dxRegAPI - Change account category")
    public void testChangeCategoryForAccount() {

        BrokerCategory brokerCategory = brokerCategories.stream()
                .filter(category -> category.getValues().size() >= 2)
                .findAny().orElseThrow(() -> {
                    System.out.println("Broker categories list is empty");
                    return new NoSuchElementException();
                });


        for (Value value : brokerCategory.getValues()) {
            categoryService.setCategoryForAccount(account.getClearingCode(), account.getAccountCode(), brokerCategory.getCategory(), value);

            Account updatedAccount = accountService.getAccountInfo(account.getClearingCode(), account.getAccountCode()).getData();
            Category createdCategory = updatedAccount.getCategories().get(0);


            assertEquals(brokerCategory.getCategory(), createdCategory.getCategory());
            assertEquals(value.getValue(), createdCategory.getValue());

        }
    }

    @Test
    @DisplayName("[MERCURYQA-4970] dxRegAPI - Rename category")
    public void testRenameAccountCategory() {

        Category categoryToCreate = Category.builder()
                .category("Spreads")
                .value("TestCategoryAutoCreate")
                .build();

        if (categoryService.getBrokerCategory("root_broker", categoryToCreate.getCategory()).getData() != null) {
            categoryService.deleteAccountCategory(account.getBrokerCode(), categoryToCreate);
        }

        Category createdCategory = categoryService.createNewAccountCategory("root_broker", categoryToCreate).getData();


        UpdateCategory categoryToUpdate = UpdateCategory.builder()
                .category(createdCategory.getCategory())
                .newValue("TestCategoryAutoCreateUPDATED")
                .oldValue(createdCategory.getValue())
                .build();

        Category updatedCategory = categoryService.renameAccountCategory("root_broker", categoryToUpdate).getData();

        assertEquals(categoryToUpdate.getNewValue(), updatedCategory.getValue());

        int statusCode = categoryService.deleteAccountCategory("root_broker", updatedCategory).getData();
        assertEquals(204, statusCode);

    }

    @Test
    @DisplayName("[MERCURYQA-4955] dxRegAPI - Create category value")
    public void testCreateCategoryValue() {

        Category categoryToCreate = Category.builder()
                .category("Spreads")
                .value("TestCategoryAutoCreate")
                .build();


        if (categoryService.getBrokerCategory("root_broker", categoryToCreate.getCategory()).getData() != null) {
            categoryService.deleteAccountCategory(account.getBrokerCode(), categoryToCreate);
        }

        Category createdCategory = categoryService.createNewAccountCategory("root_broker", categoryToCreate).getData();

        assertEquals(categoryToCreate.getCategory(), createdCategory.getCategory());
        assertEquals(categoryToCreate.getValue(), createdCategory.getValue());

        int statusCode = categoryService.deleteAccountCategory("root_broker", createdCategory).getData();
        assertEquals(204, statusCode);
    }


    @Test
    @DisplayName("[MERCURYQA-4964] dxRegAPI - Get all category types available in the system")
    public void testGetAllCategoryTypesAvailableInTheSystem() {

        List<BrokerCategory> categoriesPerBroker = categoryService.getAllBrokerCategories("root_broker").getData();
        List<BrokerCategory> categoriesPerSystem = categoryService.getAllSettingsCategories();


        for (int i = 0; i < categoriesPerBroker.size(); i++) {
            assertEquals(categoriesPerBroker.get(i).getCategory(), categoriesPerSystem.get(i).getCategory());
        }
    }

    @Test
    @DisplayName("[MERCURYQA-5383] dxRegAPI - Rename category - Restricted Group Categories")
    public void testRenameCategoryRestrictedGroupCategories() {

        UpdateCategory categoryToUpdate = UpdateCategory.builder()
                .category("Quoting")
                .newValue("testAuto2")
                .oldValue("testAuto1")
                .build();

        Error error = categoryService.renameAccountCategory("root_broker", categoryToUpdate).getError();

        assertEquals("Can't create/delete setting account group. Setting domain 'Quoting' is forbidden. Please, (re)configure domains hidden categories via 'create/delete hidden_account_categories' commands", error.errorMessage);
        assertEquals(10001, error.errorCode);

    }

    @Test
    @DisplayName("[MERCURYQA-5382] dxRegAPI - Delete category value - Restricted Group Categories")
    public void testDeleteCategoryRestrictedGroupCategories() {

        Category categoryToDelete = Category.builder()
                .category("Quoting")
                .value("testAuto2")
                .build();

        Error error = categoryService.deleteAccountCategory("root_broker", categoryToDelete).getError();

        assertEquals("Can't create/delete setting account group. Setting domain 'Quoting' is forbidden. Please, (re)configure domains hidden categories via 'create/delete hidden_account_categories' commands", error.errorMessage);
        assertEquals(10001, error.errorCode);

    }


    @Test
    @DisplayName("[MERCURYQA-5381] dxRegAPI - Create category value - Restricted Group Categories")
    public void testCreateCategoryRestrictedGroupCategories() {

        Category categoryToCreate = Category.builder()
                .category("Quoting")
                .value("testAuto2")
                .build();

        Error error = categoryService.createNewAccountCategory("root_broker", categoryToCreate).getError();

        assertEquals("Can't create/delete setting account group. Setting domain 'Quoting' is forbidden. Please, (re)configure domains hidden categories via 'create/delete hidden_account_categories' commands", error.errorMessage);
        assertEquals(10001, error.errorCode);

    }

    @Test
    @DisplayName("[MERCURYQA-4969] dxRegAPI - Delete category without necessary attributes")
    public void testDeleteCategoryWithoutNecessaryAttributes() {

        Category categoryToDelete = Category.builder()
                .category("Spreads")
                .build();

        Error error = categoryService.deleteAccountCategory("root_broker", categoryToDelete).getError();

        assertEquals("Some of the required field is empty", error.errorMessage);
        assertEquals(20001, error.errorCode);
    }

    @Test
    @DisplayName("[MERCURYQA-4967] dxRegAPI - Delete category - Add account than remove it")
    public void testAddCategoryToAccountThenRemoveCategory() {

        Category categoryToDelete = Category.builder()
                .category("Spreads")
                .value("TestCategoryAutoCreate")
                .build();

        Value value = new Value(categoryToDelete.getValue());

        Category createdCategory = categoryService.createNewAccountCategory("root_broker", categoryToDelete).getData();

        categoryService.setCategoryForAccount(account.getClearingCode(), account.getAccountCode(), categoryToDelete.getCategory(), value);

        Account accountWithCategory = accountService.getAccountInfo(account.getClearingCode(), account.getAccountCode()).getData();

        assertEquals(createdCategory.getCategory(), accountWithCategory.getCategories().get(0).getCategory());
        assertEquals(value.getValue(), accountWithCategory.getCategories().get(0).getValue());


        categoryService.deleteCategoryForAccount(account.getClearingCode(), account.getAccountCode(), createdCategory);

        Integer result = categoryService.deleteAccountCategory("root_broker", createdCategory).getData();

        assertEquals(204, result);

    }

    @Test
    @DisplayName("[MERCURYQA-4869] dxRegAPI - Set category with not-existent value")
    public void testSetCategoryWithNonExistentValue() {

        Category categoryToCreate = Category.builder()
                .category("Spreads")
                .value(RandomStringUtils.random(1))
                .build();

        Value value = new Value(categoryToCreate.getValue());
        Error error = categoryService.setCategoryForAccount(account.getClearingCode(), account.getAccountCode(), categoryToCreate.getCategory(), value).getError();

        assertEquals(30016, error.errorCode);
        assertEquals("Specified category is not found in account group categories", error.errorMessage);
    }

    @Test
    @DisplayName("[MERCURYQA-4968] dxRegAPI - Create category without necessary attributes")
    public void testCreateClientWithNotExistentCategory() {

        Category categoryToCreate = Category.builder()
                .category("Spreads")
                .build();

        Error error = categoryService.createNewAccountCategory("root_broker", categoryToCreate).getError();

        assertEquals(20001, error.errorCode);
        assertEquals("Some of the required field is empty", error.errorMessage);
    }

    @Test
    @DisplayName("[MERCURYQA-4966] dxRegAPI - Delete category with account")
    public void testDeleteCategoryWithAccount() {

        Category categoryToDelete = Category.builder()
                .category("Spreads")
                .value("Fixed Spreads")
                .build();

        Error error = categoryService.deleteAccountCategory("root_broker", categoryToDelete).getError();

        assertEquals(10001, error.errorCode);
        assertEquals("Cannot remove group (700000003) because it contains 3 participants", error.errorMessage);

    }

    @Test
    @DisplayName("[MERCURYQA-4963] dxRegAPI - Delete - Category with not-existent value")
    public void testDeleteCategoryWithNotExistentValue() {

        Category categoryToDelete = Category.builder()
                .category("Spreads")
                .value("FixedTEST")
                .build();

        Error error = categoryService.deleteAccountCategory("root_broker", categoryToDelete).getError();

        assertEquals(10001, error.errorCode);
        assertEquals("Can't delete account group. Can't find category: " + categoryToDelete.getCategory() + "/" + categoryToDelete.getValue(), error.errorMessage);
    }


    @Test
    @DisplayName("[MERCURYQA-4962] dxRegAPI - Delete - Specified category is not found")
    public void testDeleteSpecifiedCategoryNotFound() {

        Category categoryToDelete = Category.builder()
                .category("Spreadsssss")
                .value("FixedTEST")
                .build();

        Error error = categoryService.deleteAccountCategory("root_broker", categoryToDelete).getError();

        assertEquals(10001, error.errorCode);
        assertEquals("Can't delete account group. Can't find category: " + categoryToDelete.getCategory() + "/" + categoryToDelete.getValue(), error.errorMessage);
    }


    @Test
    @DisplayName("[MERCURYQA-4961] dxRegAPI - Delete category with empty required field")
    public void testDeleteCategoryWithEmptyRequiredField() {

        Category categoryToDelete = Category.builder()
                .category("Spreads")
                .build();

        Error error = categoryService.deleteAccountCategory("root_broker", categoryToDelete).getError();

        assertEquals(20001, error.errorCode);
        assertEquals("Some of the required field is empty", error.errorMessage);
    }

    @Test
    @DisplayName("[MERCURYQA-4959] dxRegAPI - Create - Category with existing value")
    public void testCreateCategoryWithExistingValue() {
        Category categoryToCreate = Category.builder()
                .category("Spreads")
                .value("Fixed Spreads")
                .build();

        Error error = categoryService.createNewAccountCategory("root_broker", categoryToCreate).getError();

        assertEquals(110003, error.errorCode);
        assertEquals("Category with passed name already exists", error.errorMessage);
    }

    @Test
    @DisplayName("[MERCURYQA-4958] dxRegAPI - Create - Specified category is not found")
    public void testCreateSpecifiedCategoryNotFound() {

        Category categoryToCreate = Category.builder()
                .category("Spreadsssss")
                .value("FixedTEST")
                .build();

        Error error = categoryService.createNewAccountCategory("root_broker", categoryToCreate).getError();

        assertEquals(30010, error.errorCode);
        assertEquals("Specified category is not found", error.errorMessage);
    }

    @Test
    @DisplayName("[MERCURYQA-4956] dxRegAPI - Create category with empty required field")
    public void testCreateCategoryWithEmptyRequiredField() {

        Category categoryToCreate = Category.builder()
                .category("")
                .value("Fixed Spreads")
                .build();

        Error error = categoryService.createNewAccountCategory("root_broker", categoryToCreate).getError();

        assertEquals(30010, error.errorCode);
        assertEquals("Specified category is not found", error.errorMessage);
    }

    @Test
    @DisplayName("[MERCURYQA-4889] dxRegAPI - Set account category for not existent account")
    public void testSetAccountCategoryForNotExistentAccount() {
        Category categoryToSet = Category.builder()
                .category("Spreads")
                .value("Fixed Spreads")
                .build();

        Value value = new Value("test");

        Error error = categoryService.setCategoryForAccount(RandomStringUtils.random(1), RandomStringUtils.random(1), categoryToSet.getCategory(), value).getError();

        assertEquals(30007, error.errorCode);
        assertEquals("Specified account is not found", error.errorMessage);
    }

    @Test
    @DisplayName("[MERCURYQA-4888] dxRegAPI - Set account category with empty requred field")
    public void testSetAccountCategoryWithEmptyRequiredField() {
        Category categoryToSet = Category.builder()
                .category("Spreads")
                .build();

        Value value = new Value("");

        Error error = categoryService.setCategoryForAccount(account.getClearingCode(), account.getAccountCode(), categoryToSet.getCategory(), value).getError();

        assertEquals(20001, error.errorCode);
        assertEquals("Some of the required field is empty", error.errorMessage);
    }

    @Test
    @DisplayName("[MERCURYQA-4887] dxRegAPI - Set account category when specified category is not found")
    public void testSetAccountCategoryWhenSpecifiedCategoryNotFound() {

        Category categoryToSet = Category.builder()
                .category("Spreads")
                .build();

        Value value = new Value("test");

        Error error = categoryService.setCategoryForAccount(account.getClearingCode(), account.getAccountCode(), categoryToSet.getCategory(), value).getError();

        assertEquals(30016, error.errorCode);
        assertEquals("Specified category is not found in account group categories", error.errorMessage);
    }

    @Test
    @DisplayName("[MERCURYQA-5096] dxRegAPI - Broker categories management - Retrieve category - Error 400 - Specified broker is not found")
    public void testRetrieveBrokerCategoriesSpecifiedBrokerNotFound() {
        Error error = categoryService.getBrokerCategory("TEST", "Spreads").getError();

        assertEquals(30008, error.errorCode);
        assertEquals("Broker with specified broker code not found", error.errorMessage);
    }

    @Test
    @DisplayName("[MERCURYQA-5094] dxRegAPI - Broker categories management - Retrieve category - Error 400 - Specified category is not found")
    public void testRetrieveCategorySpecifiedCategoryNotFound() {
        Error error = categoryService.getBrokerCategory("root_broker", "TEST").getError();

        assertEquals(30010, error.errorCode);
        assertEquals("Specified category is not found", error.errorMessage);
    }

    @Test
    @DisplayName("[MERCURYQA-5090] dxRegAPI - Broker categories management - Retrieve categories - Success")
    public void testRetrieveBrokerCategorySuccess() {
        BrokerCategory result = categoryService.getBrokerCategory("root_broker", "Spreads").getData();

        assertNotNull(result);
        assertFalse(result.getValues().isEmpty());
    }

    @Test
    @DisplayName("[MERCURYQA-5091] dxRegAPI - Broker categories management - Retrieve categories - Error 400 - Specified broker is not found")
    public void testRetrieveCategoriesSpecifiedBrokerNotFound() {
        Error error = categoryService.getAllBrokerCategories("TEST").getError();

        assertEquals(30008, error.errorCode);
        assertEquals("Broker with specified broker code not found", error.errorMessage);
    }


    @Test
    @DisplayName("[MERCURYQA-5090] dxRegAPI - Broker categories management - Retrieve categories - Success")
    public void testRetrieveBrokerCategoriesSuccess() {
        List<BrokerCategory> result = categoryService.getAllBrokerCategories("root_broker").getData();

        assertNotNull(result);
    }


    @Test
    @DisplayName("[MERCURYQA-5005] dxRegAPI - Rename category - Not unique name")
    public void testRenameCategoryWithExistingValue() {
        UpdateCategory updateCategory = UpdateCategory.builder()
                .category("Spreads")
                .oldValue("Fixed Spreads")
                .newValue("Fixed Spreads")
                .build();


        Error error = categoryService.renameAccountCategory("root_broker", updateCategory).getError();

        assertEquals(110003, error.errorCode);
        assertEquals("Category with passed name already exists", error.errorMessage);
    }

    @Test
    @DisplayName("[MERCURYQA-4974] dxRegAPI - Rename category without necessary attributes")
    public void testRenameCategoryWithoutNecessaryAttributes() {
        UpdateCategory updateCategory = UpdateCategory.builder()
                .category("Spreads")
                .oldValue("")
                .newValue("Fixed Spreads")
                .build();


        Error error = categoryService.renameAccountCategory("root_broker", updateCategory).getError();

        assertEquals(20001, error.errorCode);
        assertEquals("Some of the required field is empty", error.errorMessage);
    }


    @Test
    @DisplayName("[MERCURYQA-4973] dxRegAPI - Rename - Category with not-existent value")
    public void testRenameCategoryWithNonExistentValue() {
        UpdateCategory updateCategory = UpdateCategory.builder()
                .category("Spreads")
                .oldValue(RandomStringUtils.random(1))
                .newValue("TEST")
                .build();


        Error error = categoryService.renameAccountCategory("root_broker", updateCategory).getError();

        assertEquals(30016, error.errorCode);
        assertEquals("Specified category is not found in account group categories", error.errorMessage);
    }

    @Test
    @DisplayName("[MERCURYQA-4972] dxRegAPI - Rename - Specified category is not found")
    public void testRenameSpecifiedCategoryNotFound() {
        UpdateCategory updateCategory = UpdateCategory.builder()
                .category("Rebatesss")
                .oldValue("test_ns_new")
                .newValue("test_ns_new1")
                .build();


        Error error = categoryService.renameAccountCategory("root_broker", updateCategory).getError();

        assertEquals(10001, error.errorCode);
        assertEquals("Unknown category key", error.errorMessage);
    }

    @Test
    @DisplayName("[MERCURYQA-4971] dxRegAPI - Rename category with empty required field")
    public void testRenameCategoryWithEmptyRequiredField() {
        UpdateCategory updateCategory = UpdateCategory.builder()
                .category("")
                .oldValue("")
                .newValue("Fixed Spreads")
                .build();


        Error error = categoryService.renameAccountCategory("root_broker", updateCategory).getError();

        assertEquals(20001, error.errorCode);
        assertEquals("Some of the required field is empty", error.errorMessage);
    }








}
