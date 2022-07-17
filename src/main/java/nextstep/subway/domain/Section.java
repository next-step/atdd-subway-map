package nextstep.subway.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Section {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	private SubwayLine subwayLine;

	private Long upStationId;
	private Long downStationId;
	private Integer distance;

	public Section(Long upStationId, Long downStationId, Integer distance) {
		this.upStationId = upStationId;
		this.downStationId = downStationId;
		this.distance = distance;
	}

	public void addSubwayLine(SubwayLine subwayLine) {
		this.subwayLine = subwayLine;
	}
}
