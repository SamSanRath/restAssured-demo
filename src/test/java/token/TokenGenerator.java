package token;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpStatusCodes;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import util.TextWriter;

import static io.restassured.RestAssured.given;

public class TokenGenerator { // Class name should be noun

    private String token;

    public void generate(){ // Method name should be a verb
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userName","sfc_system");
        jsonObject.put("password","J6rfS39Js2xv49zZ");
        ValidatableResponse response = given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON) // Make sure response is JSON object
                .body(jsonObject.toJSONString())
                .when()
                //.post(baseUrl.concat("/tasks"))
                .post("https://int-piapi-internal.stg-openclass.com/tokens/")
                .then()
                .log()
                .all();
        token = response.extract().body().jsonPath().get("data");
        // Here we use testNG assertions.
        Assert.assertEquals(response.extract().statusCode(), HttpStatusCodes.STATUS_CODE_CREATED); // Dependency google-api-client
        Assert.assertEquals(response.extract().body().jsonPath().get("status"),"success");
        Assert.assertTrue(response.extract().body().jsonPath().get("data").toString().startsWith("ey"));
    }

    @Test
    public void testt(){
        generate();
        System.out.println("********************");
        System.out.println(token);
        TextWriter.write(token, "./token.txt");

    }

}
