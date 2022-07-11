package nextstep.subway.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Selection {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private long lineId;
	private Long upStationId;
	private Long downStationId;
	private Long distance;

	protected Selection() {
	}

	public Selection(long lineId, Long upStationId, Long downStationId, Long distance) {
		this.lineId = lineId;
		this.upStationId = upStationId;
		this.downStationId = downStationId;
		this.distance = distance;
	}

	public Long getId() {
		return id;
	}
}
