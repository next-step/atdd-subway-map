package nextstep.subway.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.request.LineRequest;
import nextstep.subway.ui.path.SubwayPath;
import org.springframework.http.MediaType;

public class LineStep {

	// 생성
	public static ExtractableResponse<Response> saveLine(
					final String color,
					final String name,
					final Long upStationId,
					final Long downStationId,
					final int distance
	) {
		LineRequest request = request(color, name, upStationId, downStationId, distance);

		return RestAssured
						.given().body(request).contentType(MediaType.APPLICATION_JSON_VALUE).log().all()
						.when().post(SubwayPath.LINES)
						.then().log().all()
						.extract();
	}

	// 노선 조회
	public static ExtractableResponse<Response> showLine(final Integer id) {

		return RestAssured
						.given().log().all()
						.when().get(SubwayPath.LINES + SubwayPath.ID, id)
						.then().log().all()
						.extract();

	}

	// 노선들 조회
	public static ExtractableResponse<Response> showLines() {

		return RestAssured
						.given().log().all()
						.when().get(SubwayPath.LINES)
						.then().log().all()
						.extract();
	}


	// 수정
	public static ExtractableResponse<Response> updateLine(
					final String color,
					final String name,
					final Integer id,
					final Long upStationId,
					final Long downStationId,
					final int distance
	) {
		LineRequest request = request(color, name, upStationId, downStationId, distance);

		return RestAssured
						.given().body(request).contentType(MediaType.APPLICATION_JSON_VALUE).log().all()
						.when().put(SubwayPath.LINES + SubwayPath.ID, id)
						.then().log().all()
						.extract();
	}

	// 삭제
	public static ExtractableResponse<Response> deleteLine(final Integer id) {

		return RestAssured
						.given().log().all()
						.when().delete(SubwayPath.LINES + SubwayPath.ID, 1)
						.then().log().all()
						.extract();
	}

	// 요청을 생성하는 메소드
	public static LineRequest request(
					final String color,
					final String name,
					final Long upStationId,
					final Long downStationId,
					final int distance
	) {

		return new LineRequest(color, name, upStationId, downStationId, distance);
	}
}
