package subway.api.domain.dto.outport;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import subway.api.domain.model.entity.Section;

/**
 * @author : Rene Choi
 * @since : 2024/01/27
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StationInfo {
	Long id;
	String name;

	public static StationInfo fromUpStation(Section section) {
		return StationInfo.builder()
			.id(section.fetchUpStationId())
			.name(section.fetchUpStationName())
			.build();
	}

	public static StationInfo fromDownStation(Section section) {
		return StationInfo.builder()
			.id(section.fetchDownStationId())
			.name(section.fetchDownStationName())
			.build();
	}
}
