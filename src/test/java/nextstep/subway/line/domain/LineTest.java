package nextstep.subway.line.domain;

import static java.util.stream.Collectors.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.station.domain.Station;

class LineTest {

	private Line line;

	@BeforeEach
	void setUp() {
		line = Line.of("신분당선", "bg-red-600",
			new Station("강남역"), new Station("양재역"), 3);
	}

	@DisplayName("노선에 역을 추가한다.")
	@Test
	void addStations() {
		line.addStations(new Station("양재역"), new Station("판교역"), 5);

		final List<String> stationNames = line.getStations().stream()
			.map(Station::getName)
			.collect(toList());

		assertThat(stationNames)
			.containsExactlyElementsOf(Arrays.asList("강남역", "양재역", "판교역"));
	}
}