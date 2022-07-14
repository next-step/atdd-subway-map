package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.DatabaseCleanup;
import nextstep.subway.acceptance.utils.StationUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import static nextstep.subway.acceptance.utils.DataUtils.*;
import static nextstep.subway.acceptance.utils.SectionUtils.지하철_구간을_등록한다;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철 구간 실패 테스트")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SectionFailureTest {

	@Autowired
	DatabaseCleanup databaseCleanup;

	@LocalServerPort
	int port;

	@BeforeEach
	void setUp() {
		RestAssured.port = port;
		databaseCleanup.execute();
		subwayLineInit();
	}

	/**
	 * WHEN 이미 노선에 등록된 역을 구간에 등록하면
	 * THEN 오류가 발생한다.
	 */
	@DisplayName("중복역을 구간에 등록하면 오류가 발생한다")
	@Test
	void duplicateFailure() {
		//when
		ExtractableResponse<Response> registerUpStation = 지하철_구간을_등록한다(subwayLineId, downStationId, upStationId, 10);
		ExtractableResponse<Response> registerDownStation = 지하철_구간을_등록한다(subwayLineId, downStationId, downStationId, 10);

		//then
		assertAll(
				() -> assertThat(registerUpStation.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
				() -> assertThat(registerDownStation.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
		);
	}

	/*
	 * WHEN 노선의 상행역을 구간의 상행역으로 등록하려면
	 * THEN 오류가 발생한다.
	 */
	@DisplayName("노선의 상행역을 구간의 상행역으로 등록하려면 오류가 발생한다.")
	@Test
	void registerFailure() {
		//given
		long otherStationId = StationUtils.지하철역을_등록한다("상현역").jsonPath().getLong("id");

		//when
		ExtractableResponse<Response> registerWithUpStation = 지하철_구간을_등록한다(subwayLineId, upStationId, otherStationId, 10);

		//then
		assertThat(registerWithUpStation.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}

	/**
	 * WHEN 하행역이 아닌 역을 제거하려면
	 * THEN 오류가 발생한다.
	 */
	@DisplayName("하행역이 아닌 역을 제거하려면 오류가 발생한다")
	@Test
	void deleteFailureOnUpStation() {
		//when


		//then
	}

	/**
	 * WHEN 구간이 1개인 경우에 역을 제거하려면
	 * THEN 오류가 발생한다.
	 */
	@DisplayName("구간이 1개인 경우에 역을 제거하려면 오류가 발생한다")
	@Test
	void deleteFailure() {
		//given

		//when

		//then
	}
}
