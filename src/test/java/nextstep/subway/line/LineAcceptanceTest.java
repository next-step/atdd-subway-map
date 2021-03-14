package nextstep.subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static nextstep.subway.line.LineSteps.*;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {
	@DisplayName("지하철 노선을 생성한다.")
	@Test
	void createLine() {
		// when
		final ExtractableResponse<Response> response = 지하철_노선_생성_요청("신분당선", "bg-red-600");

		// then
		지하철_노선_생성됨(response);
	}

	@DisplayName("지하철 노선 목록을 조회한다.")
	@Test
	void getLines() {
		// given
		List<Long> lineResponseIds = new ArrayList();
		lineResponseIds.add(지하철_노선_등록되어_있음("신분당선", "bg-red-600"));
		lineResponseIds.add(지하철_노선_등록되어_있음("1호선", "bg-blue-600"));

		// when
		final ExtractableResponse<Response> response = 지하철_노선_목록_조회_요청();

		// then
		지하철_노선_목록_응답됨(response);

		List<Long> responseIds = 지하철_노선_목록_포함됨(response);
		assertThat(lineResponseIds).containsAll(responseIds);
	}

	@DisplayName("지하철 노선을 조회한다.")
	@Test
	void getLine() {
		// given
		final Long id = 지하철_노선_등록되어_있음("신분당선", "bg-red-600");

		// when
		ExtractableResponse<Response> response = 지하철_노선_조회_요청(id);

		// then
		지하철_노선_응답됨(response);
	}

	@DisplayName("지하철 노선을 수정한다.")
	@Test
	void updateLine() {
		// given
		LineRequest request = new LineRequest("신분당선", "bg-red-600");
		final Long id = 지하철_노선_등록되어_있음(request.getName(), request.getColor());

		// when
		LineRequest updateRequest = new LineRequest("신분당선", "bg-green-600");
		final ExtractableResponse<Response> updateResponse = 지하철_노선_수정_요청(updateRequest.getName(), updateRequest.getColor(), id);

		// then
		지하철_노선_응답됨(updateResponse);
		지하철_노선_수정됨(id, updateRequest);
	}

	@DisplayName("지하철 노선을 제거한다.")
	@Test
	void deleteLine() {
		// given
		final Long id = 지하철_노선_등록되어_있음("신분당선", "bg-red-600");

		// when
		ExtractableResponse<Response> response = 지하철_노선_제거_요청(id);

		// then
		지하철_노선_삭제됨(response);
	}

	@DisplayName("기존에 존재하는 지하철 노선 이름으로 지하철 노선을 생성한다.")
	@Test
	void createLineWithDuplicateName() {
		// given
		지하철_노선_등록되어_있음("신분당선", "bg-red-600");

		// when
		ExtractableResponse<Response> response = 지하철_노선_생성_요청("신분당선", "bg-red-600");

		// then
		지하철_노선_생성_실패됨(response);
	}
}
