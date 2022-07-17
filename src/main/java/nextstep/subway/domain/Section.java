package nextstep.subway.domain;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
public class Section {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(fetch = LAZY)
	@JoinColumn(name = "up_station_id")
	private Station upStation;

	@OneToOne(fetch = LAZY)
	@JoinColumn(name = "_station_id")
	private Station downStation;

	private Integer distance;

	public Section() {
	}

	public Section(Station upStation, Station downStation, Integer distance) {
		this.upStation = upStation;
		this.downStation = downStation;
		this.distance = distance;
	}

	public Long getId() {
		return id;
	}

	public Station getUpStation() {
		return upStation;
	}

	public Station getDownStation() {
		return downStation;
	}

	public Integer getDistance() {
		return distance;
	}
}
