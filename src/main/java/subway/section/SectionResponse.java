package subway.section;

import subway.station.Station;

public class SectionResponse {
	private Long id;
	private Station upStation;
	private Station downStation;
	private int distance;

	public SectionResponse(Section section) {
		this.id = section.getId();
		this.upStation = section.getUpStation();
		this.downStation = section.getDownStation();
		this.distance = section.getDistance();
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
