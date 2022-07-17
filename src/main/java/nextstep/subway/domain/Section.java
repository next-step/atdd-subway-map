package nextstep.subway.domain;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
public class Section {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(fetch = LAZY)
	@JoinColumn(name = "next_station_id")
	private Station nextStation;
	private Integer distance;

	public Section() {
	}

	public Section(Station nextStation, Integer distance) {
		this.nextStation = nextStation;
		this.distance = distance;
	}

	public Station getNextStation() {
		return nextStation;
	}

	public Integer getDistance() {
		return distance;
	}
}
