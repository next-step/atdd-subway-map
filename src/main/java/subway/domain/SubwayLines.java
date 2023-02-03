package subway.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SubwayLines {

	private final List<SubwayLine> subwayLines;

	public SubwayLines(List<SubwayLine> subwayLines) {
		this.subwayLines = new ArrayList<>(subwayLines);
	}

	public List<Long> getStationIds() {
		// Set<Long> upStationIds = extractUpStationIds();
		//
		// Set<Long> downStationIds = extractDownStationIds();

		// return makeAllStationIds(upStationIds, downStationIds);

		return null;
	}

	// private Set<Long> extractDownStationIds() {
	// 	return this.subwayLines.stream()
	// 		.map(SubwayLine::getDownStationId)
	// 		.collect(Collectors.toUnmodifiableSet());
	// }
	//
	// private Set<Long> extractUpStationIds() {
	// 	return this.subwayLines.stream()
	// 		.map(SubwayLine::getUpStationId)
	// 		.collect(Collectors.toUnmodifiableSet());
	// }

	private List<Long> makeAllStationIds(Set<Long> upStationIds, Set<Long> downStationIds) {
		List<Long> stationIds = new ArrayList<>(upStationIds.size() + downStationIds.size());
		stationIds.addAll(upStationIds);
		stationIds.addAll(downStationIds);
		return stationIds;
	}

	public void toLineInfos(List<Station> stations) {

	}
}
