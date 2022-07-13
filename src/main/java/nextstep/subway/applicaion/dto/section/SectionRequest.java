package nextstep.subway.applicaion.dto.section;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.domain.Section;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SectionRequest {
	private Long upStationId;
	private Long downStationId;
	private Integer distance;

	public Section toSection() {
		return new Section(upStationId, downStationId, distance);
	}
}
