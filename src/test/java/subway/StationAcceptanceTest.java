package subway;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static subway.fixture.StationFixture.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.common.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationAcceptanceTest {

	private static final String ROOT_PATH = "/stations";

	/**
	 * When 지하철역을 생성하면
	 * Then 지하철역이 생성된다
	 * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
	 */
	@DisplayName("지하철역을 생성한다.")
	@Test
	void createStation() {
		// when
		ExtractableResponse<Response> response = 지하철역_생성(STATION_NAME_1);

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

		// then
		List<String> stationNames =
			given().log().all()
				.when().get(ROOT_PATH)
				.then().log().all()
				.extract().jsonPath().getList("name", String.class);
		assertThat(stationNames).containsAnyOf("강남역");
	}

	/**
	 * Given 2개의 지하철역을 생성하고
	 * When 지하철역 목록을 조회하면
	 * Then 2개의 지하철역을 응답 받는다
	 */
	// TODO: 지하철역 목록 조회 인수 테스트 메서드 생성
	@Test
	void 지하철역_2개의_조회_응답에_성공한다() {
		// given
		지하철역_생성(STATION_NAME_1);
		지하철역_생성(STATION_NAME_2);

		// when
		ExtractableResponse<Response> response = 지하철역을_조회한다();
		List<StationResponse> stationResponses = response.as(new TypeRef<>() {});

		// then
		assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
			() -> assertThat(stationResponses).hasSize(2)
		);
	}


	// TODO: 지하철역 제거 인수 테스트 메서드 생성
	/**
	 * Given 지하철역을 생성하고
	 * When 그 지하철역을 삭제하면
	 * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
	 */



	private ExtractableResponse<Response> 지하철역_생성(String name) {
		StationRequest stationRequest = StationRequest.builder()
			.name(name)
			.build();

		//@formatter:off
        return given()
                .log().all()
            .body(stationRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
                .post("/stations")
            .then()
                .log().all()
            .extract();
    }

	private ExtractableResponse<Response> 지하철역을_조회한다() {
		//@formatter:off
		return given()
				.log().all()
			.when()
				.get(ROOT_PATH)
			.then()
				.log().all()
			.extract();
	}
}
