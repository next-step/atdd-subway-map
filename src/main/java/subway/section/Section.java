package subway.section;

import java.util.NoSuchElementException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import subway.station.Station;

@Entity
public class Section {

	@Id
	@Column(name = "section_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private Station upStation;

	@ManyToOne
	private Station downStation;

	private int distance;

	public Section() {
	}

	public static Section validCreateSection(Sections sections, Station upStation, Station downStation, int distance) {
		if (sections.getLastSection() == null) {
			return new Section(upStation, downStation, distance);
		}
		if (sections.getLastSection().getDownStation() != upStation) {
			throw new NoSuchElementException("등록하려는 새로운 구간의 상행역이 노선의 하행 종점역과 일치하지 않습니다.");
		}
		if (sections.getAllStation().contains(downStation)) {
			throw new IllegalArgumentException("등록하려는 새로운 구간의 하행 종점역이 이미 노선에 등록되어 있습니다.");
		}
		return new Section(upStation, downStation, distance);
	}

	public Section(Station upStation, Station downStation, int distance) {
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

	public int getDistance() {
		return distance;
	}
}
