package nextstep.subway.line.domain;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.station.domain.Station;

@Entity
@Table
@Getter
@NoArgsConstructor
public class LineStation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "station_id")
	private Station station;
	private Long preStationId;
	private int distance;
	private int duration;

	@Builder
	public LineStation(Station station, Long preStationId, int distance, int duration) {
		this.station = station;
		this.preStationId = preStationId;
		this.distance = distance;
		this.duration = duration;
	}

	public Long getStationId() {
		return station.getId();
	}

	public boolean comparePreStationId(LineStation lineStation) {
		if (Objects.isNull(preStationId)) {
			return false;
		}
		return preStationId.equals(lineStation.getPreStationId());
	}

	public Long updatePreStationTo(LineStation lineStation) {
		preStationId = lineStation.getStationId();
		return preStationId;
	}

	public String lineStationName() {
		return station.getName();
	}

	public boolean compareStationIdentityWithStationName(LineStation lineStation) {
		return station.compareStationIdentityWithStationName(lineStation);
	}

	public boolean compareLineStationIdentityWithLineStationId(Long lineStationId) {
		return this.id.equals(lineStationId);
	}

	public void movePreStationOneCountBack() {
		this.preStationId = this.preStationId - 1;
	}
}
