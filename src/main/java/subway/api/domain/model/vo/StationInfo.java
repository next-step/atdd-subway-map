package subway.api.domain.model.vo;

import lombok.Builder;
import lombok.Value;
import subway.api.domain.model.entity.Link;

/**
 * @author : Rene Choi
 * @since : 2024/01/27
 */
@Value
@Builder
public class StationInfo {
	Long id;
	String name;

	public static StationInfo fromUpStation(Link link) {
		return StationInfo.builder()
			.id(link.fetchUpStationId())
			.name(link.fetchUpStationName())
			.build();
	}

	public static StationInfo fromDownStation(Link link) {
		return StationInfo.builder()
			.id(link.fetchDownStationId())
			.name(link.fetchDownStationName())
			.build();
	}
}
