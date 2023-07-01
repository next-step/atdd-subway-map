package subway;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

public class AcceptanceUtils {
	private AcceptanceUtils() {
	}

	public static Long createStationLine(String name, String color, Long upStationId, Long downStationId, BigDecimal distance) {
		final Map<String, String> stationLineCreateRequest = new HashMap<>();

		stationLineCreateRequest.put("name", name);
		stationLineCreateRequest.put("color", color);
		stationLineCreateRequest.put("upStationId", String.valueOf(upStationId));
		stationLineCreateRequest.put("downStationId", String.valueOf(downStationId));
		stationLineCreateRequest.put("distance", distance.toString());

		return RestAssured.given().log().all()
			.body(stationLineCreateRequest)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().post("/lines")
			.then().log().all()
			.statusCode(HttpStatus.OK.value())
			.extract()
			.jsonPath().getLong("id");
	}

	public static JsonPath getStationLine(Long stationLineId) {
		return RestAssured.given().log().all()
			.when().get("/lines/" + stationLineId)
			.then().log().all()
			.statusCode(HttpStatus.OK.value())
			.extract()
			.jsonPath();
	}

	public static JsonPath getStationLines() {
		return RestAssured.given().log().all()
			.when().get("/lines")
			.then().log().all()
			.statusCode(HttpStatus.OK.value())
			.extract()
			.jsonPath();
	}

	public static List<Long> createStations(List<String> names) {
		return names.stream()
			.map(AcceptanceUtils::createStation)
			.collect(Collectors.toList());
	}

	public static Long createStation(String name) {
		final Map<String, String> stationCreateRequest = new HashMap<>();
		stationCreateRequest.put("name", name);

		return RestAssured.given().log().all()
			.body(stationCreateRequest)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.post("/stations")
			.then().log().all()
			.extract()
			.jsonPath().getLong("id");
	}
}
