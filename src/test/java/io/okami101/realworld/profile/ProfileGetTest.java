package io.okami101.realworld.profile;

import org.junit.jupiter.api.Test;

import io.okami101.realworld.RealworldApplicationTests;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

public class ProfileGetTest extends RealworldApplicationTests {

    @Test
    public void cannot_get_non_existent_profile() {
        given().contentType(ContentType.JSON).when().get(baseUrl + "/api/profiles/celeb").then().statusCode(404);
    }

    @Test
    public void can_get_profile() {
        createJohnUser();

        given().contentType(ContentType.JSON).when().get(baseUrl + "/api/profiles/celeb_John Doe").then()
                .statusCode(200).body("profile.username", equalTo("John Doe")).body("profile.bio", equalTo("John Bio"))
                .body("profile.image", equalTo("https://randomuser.me/api/portraits/men/1.jpg"))
                .body("profile.following", equalTo(false));
    }
}
