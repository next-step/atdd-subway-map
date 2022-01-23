package nextstep.subway.applicaion.section.dto;

import nextstep.subway.applicaion.section.domain.Section;

public class SectionResponse {

	private Long id;
	private Long downStationId;
	private Long upStationId;
	private int distance;

	private SectionResponse() {
	}

	private SectionResponse(Long id, Long downStationId, Long upStationId, int distance) {
		this.id = id;
		this.downStationId = downStationId;
		this.upStationId = upStationId;
		this.distance = distance;
	}

	public static SectionResponse from(Section section) {
		return new SectionResponse(
				section.getId(),
				section.getDownStationId(),
				section.getUpStationId(),
				section.getDistance()
		);
	}

	public Long getId() {
		return id;
	}

	public Long getDownStationId() {
		return downStationId;
	}

	public Long getUpStationId() {
		return upStationId;
	}

	public int getDistance() {
		return distance;
	}
}
