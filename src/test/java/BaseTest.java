import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.json.simple.JSONObject;
import org.testng.Assert;

import static io.restassured.RestAssured.given;

public class BaseTest {

    protected String baseUrl = "https://b20e750132f8.ngrok.io";
    private String taskId;

    protected void createTask(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name","Task One");
        jsonObject.put("category","Category name");
        jsonObject.put("status","Completed");
        ValidatableResponse response = given()
                .contentType(ContentType.JSON) // We send request payload in this format - JSON.
                .accept(ContentType.JSON) // Make sure response is JSON object
                .body(jsonObject.toJSONString())
                .when()
                .post(baseUrl.concat("/tasks"))
                .then()
                .log()
                .all();
        taskId = response.extract().body().jsonPath().get("_id");
        // Here we use testNG assertions.
        Assert.assertEquals(response.extract().statusCode(),201);
    }

    protected String getTaskId(){
        return taskId;
    }
}
