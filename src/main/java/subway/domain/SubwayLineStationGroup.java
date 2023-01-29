package subway.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class SubwayLineStationGroup {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "STATION_ID")
	private Station station;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SUBWAY_LINE_ID")
	private SubwayLine subwayLine;

	public SubwayLineStationGroup(Station station, SubwayLine subwayLine) {
		this.station = station;
		this.subwayLine = subwayLine;
	}
}
