import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class TaskAPITest extends BaseTest{

    private String taskId;
    private RequestSpecification requestSpecification;
    private ResponseSpecification responseSpecification;

    @BeforeSuite
    public void beforeSuite(){
        requestSpecification = given()
                                .contentType(ContentType.JSON)
                                .accept(ContentType.JSON)
                                .baseUri(baseUrl);
        responseSpecification = new ResponseSpecBuilder()
                                .expectStatusCode(200)
                                .expectContentType(ContentType.JSON)
                                .build(); // Another design pattern - Builder pattern very useful in wizard.
        ValidatableResponse response = given(requestSpecification)
                                        //.contentType(ContentType.JSON)
                                        //.accept(ContentType.JSON)
                                        .when()
                                        //.get(baseUrl.concat("/health"))
                                        .get("/health")
                                        .then()
                                        .spec(responseSpecification)
                                        .log()
                                        .all();
        //System.out.println(response.extract().body().asString());
        //Assert.assertEquals(response.extract().statusCode(),200);
    }

    @Test
    public void createTaskTest(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name","Task One");
        jsonObject.put("category","Category name");
        jsonObject.put("status","Completed");
        ValidatableResponse response = given(requestSpecification)
                                        //.contentType(ContentType.JSON)
                                        //.accept(ContentType.JSON) // Make sure response is JSON object
                                        .body(jsonObject.toJSONString())
                                        .when()
                                        //.post(baseUrl.concat("/tasks"))
                                        .post("/tasks")
                                        .then()
                                        .log()
                                        .all();
        taskId = response.extract().body().jsonPath().get("_id");
        // Here we use testNG assertions.
        Assert.assertEquals(response.extract().statusCode(),201);
        Assert.assertEquals(response.extract().body().jsonPath().get("name"),"Task One");
        Assert.assertEquals(response.extract().body().jsonPath().get("category"),"Category name");
        Assert.assertEquals(response.extract().body().jsonPath().get("status[0]"),"Completed");
    }

    @Test
    public void getTaskTest(){
        createTask();
        ValidatableResponse response = given(requestSpecification)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                //.get(baseUrl.concat("/tasks/"+getTaskId()))
                .get("/tasks/"+getTaskId())
                .then()
                .spec(responseSpecification)
                .log()
                .all();
        //System.out.println(response.extract().body().asString());
        //Assert.assertEquals(response.extract().statusCode(),200);
        Assert.assertEquals(response.extract().body().jsonPath().get("name"),"Task One");
        Assert.assertEquals(response.extract().body().jsonPath().get("category"),"Category name");
        Assert.assertEquals(response.extract().body().jsonPath().get("status[0]"),"Completed");
    }

    @Test
    public void editTaskTest(){
        createTask();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name","Task One EDITED");
        jsonObject.put("category","Category name");
        jsonObject.put("status","Completed");
        ValidatableResponse response = given(requestSpecification)
                                        .contentType(ContentType.JSON)
                                        .accept(ContentType.JSON)
                                        .body(jsonObject.toJSONString())
                                        .when()
                                        //.put(baseUrl.concat("/tasks/"+getTaskId()))
                                        .put("/tasks/"+getTaskId())
                                        .then()
                                        .spec(responseSpecification)
                                        .log()
                                        .all();
        //Assert.assertEquals(response.extract().statusCode(),200);
        Assert.assertEquals(response.extract().body().jsonPath().get("name"),"Task One EDITED");
    }

    @AfterMethod
    public void deleteTask(){
        String taskIdToBeDeleted;
        if(taskId == null){
            taskIdToBeDeleted = getTaskId();
        } else {
            taskIdToBeDeleted = taskId;
        }
        ValidatableResponse response = given(requestSpecification)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                //.delete(baseUrl.concat("/tasks/"+taskIdToBeDeleted))
                .delete("/tasks/"+taskIdToBeDeleted)
                .then()
                .log()
                .all();
        Assert.assertEquals(response.extract().statusCode(),204);
    }

}
