package nextstep.subway.line.domain;

import static java.util.stream.Collectors.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.line.exception.AlreadyExistDownStationException;
import nextstep.subway.line.exception.NotLastStationException;
import nextstep.subway.line.exception.NotSameUpStationException;
import nextstep.subway.line.exception.TooLowLengthSectionsException;
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
	void addSection() {
		line.addSection(new Station("양재역"), new Station("판교역"), 5);

		final List<String> stationNames = line.getStations().stream()
			.map(Station::getName)
			.collect(toList());

		assertThat(stationNames)
			.containsExactlyElementsOf(Arrays.asList("강남역", "양재역", "판교역"));

		line.addSection(new Station("판교역"), new Station("정자역"), 4);

		final List<String> addAfterStationNames = line.getStations().stream()
			.map(Station::getName)
			.collect(toList());

		assertThat(addAfterStationNames)
			.containsExactlyElementsOf(Arrays.asList("강남역", "양재역", "판교역", "정자역"));
	}

	@DisplayName("추가되는 상행역과 마지막 하행역이 같지 않으면 예외 발생")
	@Test
	void addSection_notSameUpStationException() {
		assertThatThrownBy(
			() -> line.addSection(new Station("판교역"), new Station("정자역"), 4))
			.isInstanceOf(NotSameUpStationException.class);
	}

	@DisplayName("추가되는 하행역이 이미 존재하면 예외 발생")
	@Test
	void addSection_alreadyExistDownStationException() {
		assertThatThrownBy(
			() -> line.addSection(new Station("양재역"), new Station("강남역"), 4))
			.isInstanceOf(AlreadyExistDownStationException.class);
	}

	@DisplayName("노선에서 구간을 삭제한다.")
	@Test
	void deleteSection() {
		line.addSection(new Station("양재역"), new Station("판교역"), 5);

		line.deleteSection(new Station("판교역"));

		final List<String> stationNames = line.getStations().stream()
			.map(Station::getName)
			.collect(toList());

		assertThat(stationNames)
			.containsExactlyElementsOf(Arrays.asList("강남역", "양재역"));
	}

	@DisplayName("노선에 구간이 1개이면 삭제할 수 없다.")
	@Test
	void deleteSection_tooLowLengthSectionsException() {
		assertThatThrownBy(() -> line.deleteSection(new Station("판교역")))
			.isInstanceOf(TooLowLengthSectionsException.class);
	}

	@DisplayName("삭제하려는 역이 마지막 구간이 아니면 예외 발생")
	@Test
	void deleteSection_notLastStationException() {
		line.addSection(new Station("양재역"), new Station("판교역"), 5);

		assertThatThrownBy(() -> line.deleteSection(new Station("강남역")))
			.isInstanceOf(NotLastStationException.class);
	}
}