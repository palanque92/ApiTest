package Campus;

import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.*;


import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;

import static org.hamcrest.Matchers.*;


public class subjectCategoriesTest {
    Cookies cookies;
    String subjectName;
    String subjectCode;
    String subjectID;

    @BeforeClass
    public void loginCampus() {
        baseURI = "https://test.mersys.io/";

        Map<String, String> credential = new HashMap<>();
        credential.put("username", "turkeyts");
        credential.put("password", "TechnoStudy123");
        credential.put("rememberMe", "true");

        cookies =
                given()
                        .contentType(ContentType.JSON)
                        .body(credential)

                        .when()
                        .post("auth/login")

                        .then()
                        .log().body()
                        .statusCode(200)
                        .extract().response().getDetailedCookies()
        ;


    }  public String getRandomName() {
        return RandomStringUtils.randomAlphabetic(8);
    }

    public String getRandomCode() {
        return RandomStringUtils.randomAlphabetic(3);
    }

    @Test
    public void createSubjectCategories() {
        subjectName = getRandomName();
        subjectCode = getRandomCode();

        SubjectCategories sc=new SubjectCategories();
        sc.setName(subjectName);
        sc.setCode(subjectCode);

        subjectID =
                given()
                        .cookies(cookies)
                        .contentType(ContentType.JSON)
                        .body(sc)

                        .when()
                        .post("school-service/api/subject-categories")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().jsonPath().getString("id")
        ;
    }
    @Test(dependsOnMethods = "createSubjectCategories")
    public void createSubjectCategoriesyNegative() {
        SubjectCategories sc=new SubjectCategories();
        sc.setName(subjectName);
        sc.setCode(subjectCode);

        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(sc)

                .when()
                .post("school-service/api/subject-categories")

                .then()
                .log().body()
                .statusCode(400)
                .body("message", equalTo("The Subject Category with Name \"" +subjectName+ "\" already exists."))
                .body("message", containsString("already exists"))
        ;
    }

    @Test(dependsOnMethods = "createSubjectCategories")
    public void updateSubjectCategories() {
        subjectName = getRandomName();
        subjectCode = getRandomCode();

        SubjectCategories sc=new SubjectCategories();
        sc.setName(subjectName);
        sc.setCode(subjectCode);
        sc.setId(subjectID);

        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(sc)

                .when()
                .put("school-service/api/subject-categories")

                .then()
                .log().body()
                .statusCode(200)
                .body("name", equalTo(subjectName))
        ;
    }

    @Test(dependsOnMethods = "updateSubjectCategories")
    public void deleteSubjectCategoriesById() {
        given()
                .cookies(cookies)
                .pathParam("subjectID", subjectID)
                .log().uri()

                .when()
                .delete("school-service/api/subject-categories/{subjectID}")

                .then()
                .log().body()
                .statusCode(200)
        // .body("message", containsString("successfully"))
        ;
    }
    @Test(dependsOnMethods = "deleteSubjectCategoriesById")
    public void deleteSubjectCategoriesByIdNegative() {
        given()
                .cookies(cookies)
                .pathParam("subjectID", subjectID)
                .log().uri()

                .when()
                .delete("school-service/api/subject-categories/{subjectID}")

                .then()
                .log().body()
                .statusCode(400)
        //.body("message", equalTo("not found"))
        ;
    }

    @Test (dependsOnMethods = "deleteSubjectCategoriesById")
    public void updateSubjectCategoriesNegative(){

        subjectName = getRandomName();

        SubjectCategories subjectCategories = new SubjectCategories();
        subjectCategories.setId(subjectID);
        subjectCategories.setName(subjectName);
        subjectCategories.setCode(subjectCode
        );

        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(subjectCategories)

                .when()
                .put("school-service/api/subject-categories")

                .then()
                .log().body()
                .statusCode(400)
                .body("message", equalTo("Can't find Subject Category"));



    }

}
