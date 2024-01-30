package subway.fixture;

import static subway.location.enums.Location.*;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.line.LineCreateRequest;
import subway.line.LineResponse;
import subway.rest.Rest;

public class LineFixture {
	private final String upStationName;
	private final String downStationName;

	private LineFixture(String upStationName, String downStationName) {
		this.upStationName = upStationName;
		this.downStationName = downStationName;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static Builder init() {
		return LineFixture.builder()
			.upStationName("강남역")
			.downStationName("양재역");
	}

	private ExtractableResponse<Response> action(LineCreateRequest request) {
		return Rest.builder()
			.uri(LINES.path())
			.body(request)
			.post();
	}

	public ExtractableResponse<Response> actionReturnExtractableResponse() {
		Long upStationId = generateStationId(upStationName);
		Long downStationId = generateStationId(downStationName);
		LineCreateRequest lineCreateRequest = LineRequestFixture.create()
			.upStationId(upStationId)
			.downStationId(downStationId)
			.build();

		return action(lineCreateRequest);
	}

	private Long generateStationId(String stationName) {
		return StationFixture.builder()
			.stationName(stationName)
			.build()
			.actionReturnExtractableResponse()
			.jsonPath()
			.getLong("id");
	}

	public LineResponse actionReturnLineResponse() {
		return actionReturnExtractableResponse().as(LineResponse.class);
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

		public LineFixture build() {
			return new LineFixture(upStationName, downStationName);
		}
	}
}
