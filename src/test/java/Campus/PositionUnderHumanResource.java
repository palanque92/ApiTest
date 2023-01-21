package Campus;

import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.*;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class PositionUnderHumanResource {

    String id="63cbc671cf835c2388fe3964";
    String positionID;
    String positionTenantId="6390ef53f697997914ec20c2";
    String positionName;
    String positionShortName;
    Cookies cookies;

    @BeforeClass
    public void loginCampus()
    {
        baseURI="https://test.mersys.io/";

        Map<String,String> credential=new HashMap<>();
        credential.put("username","turkeyts");
        credential.put("password","TechnoStudy123");
        credential.put("rememberMe","true");

        cookies=
                given()
                        .contentType(ContentType.JSON)
                        .body(credential)

                        .when()
                        .post("auth/login")

                        .then()
                        .statusCode(200)
                        .extract().response().getDetailedCookies()
        ;
    }
    public String getRandomName() {  return RandomStringUtils.randomAlphabetic(8); }

    public String getRandomShortName() { return RandomStringUtils.randomAlphabetic(3); }



    @Test
    public void createPosition()
    {
        positionName=getRandomName();
        positionShortName=getRandomShortName();

        position ps = new position();
        ps.setName(positionName);
        ps.setShortName(positionShortName);
        ps.setTenantId(positionTenantId);



        positionID=
                given()
                        .cookies(cookies)
                        .contentType(ContentType.JSON)
                        .body(ps)

                        .when()
                        .post("school-service/api/employee-position")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().jsonPath().getString("id")
        ;
    }

    @Test(dependsOnMethods = "createPosition")
    public void createPositionNegative()
    {

        position ps = new position();
        ps.setName(positionName);
        ps.setShortName(positionShortName);
        ps.setTenantId(positionTenantId);


                given()
                        .cookies(cookies)
                        .contentType(ContentType.JSON)
                        .log().uri()
                        .body(ps)

                        .when()
                        .post("school-service/api/employee-position")

                        .then()
                        .log().body()
                        .statusCode(400)
                        .body("message",equalTo("The Position with Name \""+positionName+"\" already exists."))
        ;
    }

    @Test(dependsOnMethods = "createPosition")
    public void updatePosition()
    {
        positionName=getRandomName();
        positionShortName=getRandomShortName();

        position ps = new position();
        ps.setName(positionName);
        ps.setShortName(positionShortName);
        ps.setTenantId(positionTenantId);
        ps.setId(positionID);



        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(ps)

                .when()
                .put("school-service/api/employee-position")

                .then()
                .log().body()
                .statusCode(200)
        ;
    }
    @Test(dependsOnMethods = "updatePosition")
    public void deletePosition()
    {

        given()
                .cookies(cookies)
                .pathParam("positionID",positionID)
                .log().uri()

                .when()
                .delete("school-service/api/employee-position/{positionID}")

                .then()
                .log().body()
                .statusCode(204)
        ;
    }

    @Test(dependsOnMethods = "deletePosition")
    public void deletePositionNegative()
    {

        given()
                .cookies(cookies)
                .pathParam("positionID",positionID)
                .log().uri()

                .when()
                .delete("school-service/api/employee-position/{positionID}")

                .then()
                .log().body()
                .statusCode(204)
        ;
    }








    public class position{
        String name;
        String shortName;
        String id;
        String tenantId;

        public String getTenantId() {
            return tenantId;
        }

        public void setTenantId(String tenantId) {
            this.tenantId = tenantId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getShortName() {
            return shortName;
        }

        public void setShortName(String shortName) {
            this.shortName = shortName;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
