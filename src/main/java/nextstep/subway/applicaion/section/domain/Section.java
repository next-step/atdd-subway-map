package nextstep.subway.applicaion.section.domain;

import nextstep.subway.applicaion.line.domain.Line;
import nextstep.subway.applicaion.station.domain.Station;

import javax.persistence.*;

@Entity
public class Section {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "line_id")
	private Line line;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "up_station_id")
	private Station upStation;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "down_station_id")
	private Station downStation;

	@Column(nullable = true)
	private int distance;

	protected Section() {
	}

	private Section(Station upStation, Station downStation, int distance) {
		this.upStation = upStation;
		this.downStation = downStation;
		this.distance = distance;
	}

	public static Section of(Station upStation, Station downStation, int distance) {
		final Section section = new Section(upStation, downStation, distance);
		downStation.addDownStationSection(section);
		upStation.addUpStationSection(section);
		return section;
	}

	public Long getId() {
		return id;
	}

	public Line getLine() {
		return line;
	}

	public void setLine(Line line) {
		this.line = line;
		if (!line.getSections().contains(this)) {
			this.line.addSection(this);
		}
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

	public Long getDownStationId() {
		return downStation.getId();
	}

	public Long getUpStationId() {
		return upStation.getId();
	}

	public boolean isOnLine(Line otherLine) {
		return otherLine.equals(line);
	}

	public void setDownStation(Station station) {
		downStation = station;
	}

	public void setUpStation(Station station) {
		upStation = station;
	}

	public boolean isDownStation(Station station) {
		return downStation.equals(station);
	}

	public boolean isUpStation(Station station) {
		return upStation.equals(station);
	}

	public boolean hasStation(Station station) {
		return upStation.equals(station) || downStation.equals(station);
	}
}
