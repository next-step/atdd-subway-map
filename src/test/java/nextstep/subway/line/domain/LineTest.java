package nextstep.subway.line.domain;

import static java.util.stream.Collectors.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.line.exception.AlreadyExistDownStationException;
import nextstep.subway.line.exception.NotSameUpStationException;
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

		line.addStations(new Station("판교역"), new Station("정자역"), 4);

		final List<String> addAfterStationNames = line.getStations().stream()
			.map(Station::getName)
			.collect(toList());

		assertThat(addAfterStationNames)
			.containsExactlyElementsOf(Arrays.asList("강남역", "양재역", "판교역", "정자역"));
	}

	@DisplayName("추가되는 상행역과 마지막 하행역이 같지 않으면 예외 발생")
	@Test
	void addStations_notSameUpStationException() {
		assertThatThrownBy(
			() -> line.addStations(new Station("판교역"), new Station("정자역"), 4))
			.isInstanceOf(NotSameUpStationException.class);
	}

	@DisplayName("추가되는 하행역이 이미 존재하면 예외 발생")
	@Test
	void addStations_alreadyExistDownStationException() {
		assertThatThrownBy(
			() -> line.addStations(new Station("양재역"), new Station("강남역"), 4))
			.isInstanceOf(AlreadyExistDownStationException.class);
	}
}