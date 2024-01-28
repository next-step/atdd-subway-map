package subway.api.domain.model.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import subway.api.domain.model.entity.Link;

/**
 * @author : Rene Choi
 * @since : 2024/01/27
 */
@Getter
@Setter
@Builder
public class StationInfo {
	private Long id;
	private String name;

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
