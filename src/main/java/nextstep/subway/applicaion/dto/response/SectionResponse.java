package nextstep.subway.applicaion.dto.response;

public class SectionResponse {

	private Long id;
	private Long upStationId;
	private Long downStationId;
	private int distance;

	public SectionResponse(Long id, Long upStationId, Long downStationId, int distance) {
		this.id = id;
		this.upStationId = upStationId;
		this.downStationId = downStationId;
		this.distance = distance;
	}

	public Long getId() {
		return this.id;
	}

	public static SectionResponse of(Long id, Long upStationId, Long downStationId, int distance) {

		return new SectionResponse(id, upStationId, downStationId, distance);
	}
}
