package nextstep.subway.domain;

import nextstep.subway.applicaion.dto.LineUpdateRequest;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
public class Line {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String color;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "up_station_id")
	private Station upStation;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "down_station_id")
	private Station downStation;

	protected Line() {
	}

	public Line(String name, String color, Station upStation, Station downStation) {
		this.name = name;
		this.color = color;
		this.upStation = upStation;
		this.downStation = downStation;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getColor() {
		return color;
	}

	public Station getUpStation() {
		return upStation;
	}

	public Station getDownStation() {
		return downStation;
	}

	public List<Station> getStations() {
		List<Station> stations = new ArrayList<>();
		stations.add(upStation);
		stations.add(downStation);
		return stations;
	}

	public void update(LineUpdateRequest lineUpdateRequest) {
		this.name = lineUpdateRequest.getName();
		this.color = lineUpdateRequest.getColor();
	}
}
