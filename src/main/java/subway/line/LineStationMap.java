package subway.line;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import subway.station.Station;

@Entity
public class LineStationMap {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Long upperId;

	@ManyToOne(targetEntity = Line.class)
	@JoinColumn(name = "line_id", nullable = false)
	private Line line;

	@ManyToOne(targetEntity = Station.class)
	@JoinColumn(name = "station_id", nullable = false)
	private Station station;

	protected LineStationMap() {
	}

	public LineStationMap(Line line, Station station, Long upperId) {
		this.line = line;
		this.station = station;
		this.upperId = upperId;
	}

	public Long getId() {
		return id;
	}

	public Line getLine() {
		return line;
	}

	public Station getStation() {
		return station;
	}

	public Long getUpperId() {
		return upperId;
	}
}
