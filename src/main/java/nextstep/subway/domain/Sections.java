package nextstep.subway.domain;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;

public class Sections {
	private final List<Section> values;

	public Sections(List<Section> selectionList) {
		values = new ArrayList<>(selectionList);
	}

	public boolean isDeletable(long stationId) {
		if (CollectionUtils.isEmpty(values)) {
			throw new IllegalStateException();
		}

		Section section = values.stream()
			.reduce((first, second) -> second)
			.orElseThrow(IllegalStateException::new);

		if (section.getDownStationId() != stationId) {
			throw new IllegalStateException();
		}

		return true;
	}

}
