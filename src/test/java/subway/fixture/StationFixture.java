package subway.fixture;

import static subway.location.enums.Location.*;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.rest.Rest;
import subway.station.StationRequest;

public class StationFixture {
	private final String stationName;

	private StationFixture(String stationName) {
		this.stationName = stationName;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static StationFixture.Builder init() {
		return StationFixture.builder()
			.stationName("강남역");
	}

	public String getStationName() {
		return stationName;
	}

	public ExtractableResponse<Response> actionReturnExtractableResponse() {
		StationRequest request = new StationRequest(stationName);

		return Rest.builder()
			.uri(STATIONS.path())
			.body(request)
			.post();
	}

	public static class Builder {
		private String stationName;

		Builder() {
		}

		public Builder stationName(String stationName) {
			this.stationName = stationName;
			return this;
		}

		public StationFixture build() {
			return new StationFixture(stationName);
		}
	}
}
