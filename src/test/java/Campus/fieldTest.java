package Campus;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class fieldTest {
    Cookies cookies;

    @BeforeClass
    public void loginCampus() {
        baseURI = "https://test.mersys.io/";

        Map<String, String> credential = new HashMap<>();
        credential.put("username", "turkeyts");
        credential.put("password", "TechnoStudy123");
        credential.put("rememberMe", "true");

        cookies=
                given()
                        .contentType(ContentType.JSON)
                        .body(credential)

                        .when()
                        .post("auth/login")

                        .then()
                        //.log().cookies()
                        .statusCode(200)
                        .extract().response().getDetailedCookies()
        ;

    }
    String fieldName;
    String fieldCode;
    String fieldID;



    @Test
    public void createFields() {
        fieldName=getRandomName();
        fieldCode=getRandomCode();

        Fields fields = new Fields();

        fields.setName(fieldName);
        fields.setCode(fieldCode);
        fields.setType("STRING");
        fields.setSchoolId("5fe07e4fb064ca29931236a5");
        fieldID =
                given()
                        .cookies(cookies)
                        .contentType(ContentType.JSON)
                        .body(fields)

                        .when()
                        .post("school-service/api/entity-field")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().jsonPath().getString("id");
        ;
    }

    public String getRandomName() {
        return RandomStringUtils.randomAlphabetic(8);
    }

    public String getRandomCode() {
        return RandomStringUtils.randomAlphabetic(3);
    }


    @Test(dependsOnMethods = "createFields")
    public void createFieldsNegative() {


        Fields fields = new Fields();
        fields.setName(fieldName);
        fields.setCode(fieldCode);
        fields.setType("STRING");
        fields.setSchoolId("5fe07e4fb064ca29931236a5");

                given()
                        .cookies(cookies)
                        .contentType(ContentType.JSON)
                        .body(fields)

                        .when()
                        .post("school-service/api/entity-field")

                        .then()
                        .log().body()
                        .statusCode(400)
                        .extract().jsonPath().getString("id");
        ;
    }

    @Test(dependsOnMethods = "createFields")
    public void updateFields() {
        fieldName = getRandomName();


        Fields fields = new Fields();
        fields.setId(fieldID);
        fields.setName(fieldName);
        fields.setCode(fieldCode);
        fields.setType("STRING");
       fields.setSchoolId("6343bf893ed01f0dc03a509a");

        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(fields)

                .when()
                .put("school-service/api/entity-field")

                .then()
                .log().body()
                .statusCode(200)
        .body("name",equalTo(fieldName));


    }

    @Test(dependsOnMethods = "updateFields")
    public void deleteFieldsById() {

        given()
                .cookies(cookies)
                .pathParam("fieldID", fieldID)

                .when()
                .delete("school-service/api/entity-field/{fieldID}")

                .then()
                .log().body()
                .statusCode(204);


    } @Test(dependsOnMethods = "deleteFieldsById")
    public void deleteFieldsNegative() {

        given()
                .cookies(cookies)
                .pathParam("fieldID", fieldID)

                .when()
                .delete("school-service/api/entity-field/{fieldID}")

                .then()
                .log().body()
                .statusCode(400);


    }
    @Test (dependsOnMethods = "deleteFieldsById")
    public void updateFieldsNegative(){

        fieldName = getRandomName();

        Fields fields = new Fields();
        fields.setId(fieldID);
        fields.setName(fieldName);
        fields.setCode(fieldCode);
        fields.setType("STRING");
        fields.setSchoolId("5fe07e4fb064ca29931236a5");

        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(fields)

                .when()
                .put("school-service/api/entity-field")

                .then()
                .log().body()
                .statusCode(400)
                .body("message", equalTo("EntityField not found"));



    }




}



