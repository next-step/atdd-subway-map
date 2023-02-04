package subway.fixture;

import static subway.fixture.StationFixtures.*;

import subway.presentation.request.SectionRequest;

public class SectionFixtures {

	public static SectionRequest.Create 혜화_동대문() {
		return SectionRequest.Create.builder()
			.upStationId(헤화)
			.downStationId(동대문)
			.distance(10)
			.build();
	}

	public static SectionRequest.Create 동대문_동대문역사문화공원() {
		return SectionRequest.Create.builder()
			.upStationId(동대문)
			.downStationId(동대문역사문화공원)
			.distance(10)
			.build();
	}

	public static SectionRequest.Create 동대문역사문화공원_충무로() {
		return SectionRequest.Create.builder()
			.upStationId(동대문역사문화공원)
			.downStationId(충무로)
			.distance(10)
			.build();
	}
}
