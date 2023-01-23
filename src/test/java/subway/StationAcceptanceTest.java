package subway;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationAcceptanceTest {
	/**
	 * When 지하철역을 생성하면
	 * Then 지하철역이 생성된다
	 * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
	 */
	@Test
	void 지하철역을_생성한다() {
		// when & then
		createStation("강남역");

		// then
		List<String> stationNames = getStationNames();
		assertThat(stationNames).containsAnyOf("강남역");
	}

	/**
	 * Given 2개의 지하철역을 생성하고
	 * When 지하철역 목록을 조회하면
	 * Then 2개의 지하철역을 응답 받는다
	 */
	// TODO: 지하철역 목록 조회 인수 테스트 메서드 생성
	@Test
	void 지하철_목록_조회() {
		//given 2개의 지하철역을 생성하고
		createStation("강남역");
		createStation("역삼역");

		//when 지하철역 목록을 조회하면
		List<String> stationNames = getStationNames();

		//then 2개의 지하철역을 응답 받는다
		assertThat(stationNames).contains("강남역", "역삼역"); // contains 를 이용해 두 역이 모두 포함되어 있는지 검증
	}

	/**
	 * Given 지하철역을 생성하고
	 * When 그 지하철역을 삭제하면
	 * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
	 */
	// TODO: 지하철역 제거 인수 테스트 메서드 생성
	@Test
	void 지하철역_제거() {
		//Given 지하철역을 생성하고
		createStation("강남역");
		String deleteTargetId = createStation("역삼역");

		//When 그 지하철역을 삭제하면
		given()
			.when()
			.delete("/stations/" + deleteTargetId)
			.then()
			.statusCode(HttpStatus.NO_CONTENT.value());

		// Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
		List<String> stationNameList = getStationNames();

		assertThat(stationNameList).containsOnly("강남역"); // containsOnly 를 이용해 강남역만 포함되어 있는지 검증
	}

	private String createStation(String stationName) {
		Map<String, String> params = new HashMap<>();
		params.put("name", stationName);

		return given()
			.body(params)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when()
			.post("/stations")
			.then()
			.statusCode(HttpStatus.CREATED.value())
			.extract()
			.jsonPath()
			.getString("id"); // 응답 객체에서 id값을 추출해 리턴
	}

	private List<String> getStationNames() {
		return when().get("/stations")
			.then()
			.extract()
			.jsonPath()
			.getList("name", String.class);
	}
}