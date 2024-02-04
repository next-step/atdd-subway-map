package subway;

import static io.restassured.RestAssured.given;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

public class BaseAcceptanceTest {
    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @BeforeEach
    void setUp() {
        databaseCleanUp.execute();
    }

    void createStation(Map<String, String> param1) {
        given().body(param1)
               .contentType(MediaType.APPLICATION_JSON_VALUE).log().all()
               .when().post("/stations")
               .then().log().all();
    }
}
