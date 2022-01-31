package nextstep.subway.acceptance;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.applicaion.dto.StationResponse;

@DisplayName("지하철 구간 관리 기능")
public class SectionAcceptanceTest extends AcceptanceTest {
	private StationResponse 강남역;
	private StationResponse 역삼역;
	private StationResponse 교대역;
	private LineResponse 이호선;
	
	@BeforeEach
	void 초기생성() {
		// given
		LineRequest initLine = LineSteps.노선_데이터(LineSteps.Line.이호선.getName());

		교대역 = StationSteps.지하철역_생성_요청(StationSteps.Station.교대역.getName()).as(StationResponse.class);
		역삼역 = StationSteps.지하철역_생성_요청(StationSteps.Station.역삼역.getName()).as(StationResponse.class);
		강남역 = StationSteps.지하철역_생성_요청(StationSteps.Station.강남역.getName()).as(StationResponse.class);

		이호선 = LineSteps.지하철_노선_생성_요청(LineRequest.of(initLine.getName(), initLine.getColor(), 교대역.getId(), 역삼역.getId(), 10))
			.as(LineResponse.class);
	}

	/**
	 * Feature: 구간 등록 기능
	 * Scenario: 지하철 구간 등록
	 *
	 * Given 지하철 노선이 등록되어 있다.
	 * When 지하철 구간 등록을 요청 하면
	 * Then 지하철 구간 등록이 성공한다.
	*/
	@DisplayName("지하철 구간 등록")
	@Test
	void 지하철_구간_등록() {
		// when
		ExtractableResponse<Response> response = SectionSteps.지하철_구간_등록_요청(이호선.getId(), SectionRequest.of(역삼역.getId(), 강남역.getId(), 6));

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}

	/**
	 * Feature: 구간 등록 조건 에러 처리_1
	 * Scenario: 지하철 구간 등록시 조건에 부합하지 않는 경우 에러 처리
	 *
	 * Given 지하철 노선이 등록되어 있다.
	 * AND 지하철 구간 등록을 요청하고
	 * When 새로운 구간의 상행역이 해당 노선에 등록되어 있는 하행 종점역이 아니면
	 * Then 지하철 구간 등록이 실패한다.
	 */
	@DisplayName("지하철 구간 등록 조건 에러 처리_1")
	@Test
	void 새로운_구간_상행역이_해당_노선_하행_종점역_인지_체크() {
		// when
		ExtractableResponse<Response> response = SectionSteps.지하철_구간_등록_요청(이호선.getId(), SectionRequest.of(강남역.getId(), 역삼역.getId(), 6));

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.EXPECTATION_FAILED.value());
	}

	/**
	 * Feature: 구간 등록 조건 에러 처리_2
	 * Scenario: 지하철 구간 등록시 조건에 부합하지 않는 경우 에러 처리
	 *
	 * Given 지하철 노선이 등록되어 있다.
	 * AND 지하철 구간 등록을 요청하고
	 * When 새로운 구간의 하행역은 해당 노선에 등록되어 있는 역이라면
	 * Then 지하철 구간 등록이 실패한다.
	 */
	@DisplayName("지하철 구간 등록 조건 에러 처리_2")
	@Test
	void 새로운_구간_하행역이_해당_노선에_등록되어_있는지_체크() {
		// when
		ExtractableResponse<Response> response = SectionSteps.지하철_구간_등록_요청(이호선.getId(), SectionRequest.of(역삼역.getId(), 교대역.getId(), 6));

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.EXPECTATION_FAILED.value());
	}

	/**
	 * Feature: 구간 제거
	 * Scenario: 지하철 노선에 구간을 제거하는 기능
	 *
	 * Given 지하철 노선이 등록되어 있다.
	 * AND 지하철 구간 등록이 되어 있다.
	 * When 지하철 구간 삭제를 요청 하면
	 * Then 지하철 구간 삭제가 성공한다.
	 */
	@DisplayName("지하철 구간 삭제")
	@Test
	void 지하철_구간_삭제() {
		// given
		SectionSteps.지하철_구간_등록_요청(이호선.getId(), SectionRequest.of(역삼역.getId(), 강남역.getId(), 6));

		// when
		ExtractableResponse<Response> response = SectionSteps.지하철_구간_삭제_요청(이호선.getId(), 강남역.getId());

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}
}
