package subway.fixture;

import subway.api.interfaces.dto.request.SectionCreateRequest;

/**
 * @author : Rene Choi
 * @since : 2024/01/31
 */
public class SectionFixtureCreator {

	/**
	 * currently not using
	 */
	// todo -> to be managed
	public static SectionCreateRequest createSectionCreateRequest(Long downStationId, Long upStationId, Long distance) {
		return SectionCreateRequest.builder()
			.downStationId(downStationId)
			.upStationId(upStationId)
			.distance(distance)
			.build();
	}
}
