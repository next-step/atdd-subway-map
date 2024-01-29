package subway.create;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.fixture.LineRequestFixture;
import subway.line.LineCreateRequest;
import subway.line.LineResponse;

public class LineCreator {
	private String upStationName;
	private String downStationName;

	private LineCreator() {
	}

	private LineCreator(String upStationName, String downStationName) {
		this.upStationName = upStationName;
		this.downStationName = downStationName;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static Builder init() {
		return LineCreator.builder()
			.upStationName("강남역")
			.downStationName("양재역");
	}

	private ExtractableResponse<Response> action(LineCreateRequest request) {
		return RestAssured
			.given().log().all()
			.body(request)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().post("/lines")
			.then().log().all()
			.extract();
	}

	public ExtractableResponse<Response> actionReturnExtractableResponse() {
		Long upStationId = StationCreator.action(upStationName);
		Long downStationId = StationCreator.action(downStationName);
		LineCreateRequest lineCreateRequest = LineRequestFixture.create()
			.upStationId(upStationId)
			.downStationId(downStationId)
			.build();

		return action(lineCreateRequest);
	}

	public LineResponse actionReturnLineResponse() {
		return actionReturnExtractableResponse().jsonPath()
			.getObject("", LineResponse.class);
	}

	public static class Builder {
		private String upStationName;
		private String downStationName;

		Builder() {
		}

		public Builder upStationName(String upStationName) {
			this.upStationName = upStationName;
			return this;
		}

		public Builder downStationName(String downStationName) {
			this.downStationName = downStationName;
			return this;
		}

		public LineCreator build() {
			return new LineCreator(upStationName, downStationName);
		}
	}
}
