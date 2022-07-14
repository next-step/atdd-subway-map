package nextstep.subway.applicaion.dto.subwayline;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.SubwayLine;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubwayLineRequest {
	private Long id;
	private String name;
	private String color;
	private Long upStationId;
	private Long downStationId;
	private Integer distance;

	public SubwayLine toEntity(Section section) {
		return new SubwayLine(id, section, name, color, upStationId, downStationId, distance);
	}
}
