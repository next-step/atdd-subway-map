package subway.fixture;

import subway.dto.StationRequest;

public class StationFixture {
	public static StationRequest 강남역_생성_요청() {
		return new StationRequest("강남역");
	}

	public static StationRequest 압구정역_생성_요청() {
		return new StationRequest("압구역");
	}

	public static StationRequest 합정역_생성_요청() {
		return new StationRequest("합정역");
	}
}
