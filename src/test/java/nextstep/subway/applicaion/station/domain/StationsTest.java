package nextstep.subway.applicaion.station.domain;

import nextstep.subway.applicaion.line.domain.Line;
import nextstep.subway.applicaion.section.domain.Section;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class StationsTest {

	static Stream<Arguments> provideInvalidUpStationSectionParameter() {
		final Line 신분당선 = Line.of("신분당선", "red");
		final Station 강남역 = Station.of("강남역");
		final Station 양재역 = Station.of("양재역");
		final Station 판교역 = Station.of("판교역");
		final Station 정자역 = Station.of("정자역");

		final Section 강남_양재_구간 = Section.of(강남역, 양재역, 10);
		final Section 양재_정자_구간 = Section.of(양재역, 정자역, 10);
		final Section 정자_판교_구간 = Section.of(정자역, 판교역, 10);

		신분당선.addSection(양재_정자_구간);
		신분당선.addSection(정자_판교_구간);
		신분당선.addSection(강남_양재_구간);

		return Stream.of(Arguments.of(신분당선, asList(강남역, 양재역, 정자역, 판교역)));
	}

	@DisplayName("stations 목록 정렬 테스트")
	@ParameterizedTest
	@MethodSource("provideInvalidUpStationSectionParameter")
	void getSortedStations(Line 신분당선, List<Station> expectedStations) {
		// when
		final List<Station> sortedStations = Stations.ofLine(신분당선);
		// then
		assertThat(sortedStations).containsExactlyElementsOf(expectedStations);

	}

}