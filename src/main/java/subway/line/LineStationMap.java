package subway.line;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
	@Enumerated(EnumType.STRING)
	private Lane lane;

	@ManyToOne
	@JoinColumn(name = "line_id")
	private Line line;

	@Column(nullable = false)
	private Integer sort;

	@ManyToOne(targetEntity = Station.class)
	@JoinColumn(name = "station_id")
	private Station station;

	public LineStationMap() {
	}

	public LineStationMap(Lane lane, Line line, Integer sort, Station station) {
		this.lane = lane;
		this.line = line;
		this.sort = sort;
		this.station = station;
	}

	public Lane getLane() {
		return lane;
	}

	public Line getLine() {
		return line;
	}

	public Integer getSort() {
		return sort;
	}

	public Station getStation() {
		return station;
	}
}
