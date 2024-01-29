package subway.fixture;

import subway.line.LineCreateRequest;

public class LineRequestFixture {
	public static LineCreateRequest.Builder create() {
		return LineCreateRequest.builder()
			.name("TEST NAME")
			.color("TEST COLOR")
			.upStationId(1L)
			.downStationId(2L)
			.distance(1);
	}
}
