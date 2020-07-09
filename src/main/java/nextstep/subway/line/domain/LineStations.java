package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.exception.AlreadyRegisteredException;
import nextstep.subway.exception.NotRegisteredException;

@Embeddable
@Getter
@NoArgsConstructor
public class LineStations {

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "line_id", foreignKey = @ForeignKey(name = "fk_line_station_to_line"))
	private List<LineStation> lineStations = new ArrayList<>();

	public LineStation add(LineStation lineStation) {
		validateAlreadyExists(lineStation);
		lineStations.stream()
			.filter(station -> station.comparePreStationId(lineStation))
			.findFirst()
			.ifPresent(station -> station.updatePreStationTo(lineStation));
		lineStations.add(lineStation);
		return lineStation;
	}

	private void validateAlreadyExists(LineStation lineStation) {
		lineStations.stream()
			.filter(station -> station.compareStationIdentityWithStationName(lineStation))
			.findFirst()
			.ifPresent(station -> {
				throw new AlreadyRegisteredException("this station is already registered. you can't add this again.");
			});
	}

	public LineStation remove(LineStation lineStation) {
		lineStations.remove(lineStation);
		return lineStation;
	}

	public LineStation findLineStationByLineStationId(Long lineStationId) {
		return lineStations.stream()
			.filter(lineStation -> lineStation.compareLineStationIdentityWithLineStationId(lineStationId))
			.findFirst()
			.orElseThrow(() -> new NotRegisteredException("nothing found in line with matched station."));
	}

	public int findLineStationIndexByLineStation(LineStation lineStation) {
		return lineStations.indexOf(lineStation);
	}

	public void adjustPreStationIdOfPriorToUnregisteredLineStation(int lineStationIndex) {
		try {
			LineStation lineStation = lineStations.get(lineStationIndex);
			lineStation.movePreStationOneCountBack();
		} catch (Exception ignored) {
		}
	}
}
