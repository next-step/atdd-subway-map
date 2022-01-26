package nextstep.subway.applicaion.station.domain;

import nextstep.subway.applicaion.line.domain.Line;
import nextstep.subway.applicaion.section.domain.Section;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class Stations {

	public static List<Station> ofLine(Line line) {
		Deque<Station> stations = new ArrayDeque<>();
		Deque<Section> sections = new ArrayDeque<>(line.getSections());

		appendFirstStation(stations, sections);

		while (!sections.isEmpty()) {
			addStation(stations, sections);
		}

		return new ArrayList<>(stations);
	}

	private static void addStation(Deque<Station> stations, Deque<Section> sections) {
		final Station upStation = stations.getFirst();
		final Station downStation = stations.getLast();
		final Section section = sections.removeFirst();
		if (downStation.equals(section.getUpStation())) {
			stations.addLast(section.getDownStation());
			return;
		}
		if (upStation.equals(section.getDownStation())) {
			stations.addFirst(section.getUpStation());
			return;
		}
		sections.addLast(section);
	}

	private static void appendFirstStation(Deque<Station> stations, Deque<Section> sections) {
		final Section first = sections.removeLast();
		stations.add(first.getUpStation());
		stations.add(first.getDownStation());
	}

}
