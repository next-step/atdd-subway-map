package subway;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.MediaType;

public class StationTestUtils {

  public static List<Long> 지하철역_생성(Collection<String> names) {
    return names.stream().map(StationTestUtils::지하철역_생성).collect(Collectors.toList());
  }

  public static Long 지하철역_생성(String name) {
    Map<String, String> params = new HashMap<>();
    params.put("name", name);

    return RestAssured
        .given()
          .contentType(MediaType.APPLICATION_JSON_VALUE).body(params).log().all()
        .when()
          .post("/stations")
        .then()
          .extract().jsonPath().getLong("id");
  }

  public static RequestSpecification prepareRestAssuredGiven(Map<String, String> body) {
    return prepareRestAssuredGiven().body(body);
  }

  public static RequestSpecification prepareRestAssuredGiven() {
    return RestAssured.given().log().all().contentType(MediaType.APPLICATION_JSON_VALUE);
  }

  public static List<String> 지하철역_조회() {
    return StationTestUtils.prepareRestAssuredGiven()
        .when().get("/stations")
        .then().log().all()
        .extract().jsonPath().getList("name", String.class);
  }

  public static void 지하철역_삭제(Long id) {
    RestAssured
        .given().log().all().contentType(MediaType.APPLICATION_JSON_VALUE)
        .when().delete("/stations/" + id);
  }
}
