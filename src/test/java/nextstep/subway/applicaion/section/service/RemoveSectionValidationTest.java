package nextstep.subway.applicaion.section.service;

import nextstep.subway.applicaion.line.domain.Line;
import nextstep.subway.applicaion.section.domain.Section;
import nextstep.subway.applicaion.section.exception.InvalidSectionRemovalException;
import nextstep.subway.applicaion.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RemoveSectionValidationTest {

	static Stream<Arguments> provideLineAndNotLastDownStation() {
        final Line 신분당선 = Line.of("신분당선", "red");
        final Station 강남역 = Station.of("강남역");
        final Station 양재역 = Station.of("양재역");
        final Station 판교역 = Station.of("판교역");

        final Section 강남_양재_구간 = Section.of(강남역, 양재역, 10);
        final Section 양재_판교_구간 = Section.of(양재역, 판교역, 10);

        신분당선.addSection(강남_양재_구간);
        신분당선.addSection(양재_판교_구간);

        return Stream.of(
                Arguments.of(
                        신분당선,
                        양재역
                )
        );
	}

	static Stream<Arguments> provideOnlyOneSectionLine() {
		final Line 신분당선 = Line.of("신분당선", "red");
		final Station 강남역 = Station.of("강남역");
        final Station 양재역 = Station.of("양재역");
        final Section 강남_양재_구간 = Section.of(강남역, 양재역, 10);

		신분당선.addSection(강남_양재_구간);

		return Stream.of(
				Arguments.of(
						신분당선,
						양재역
				)
		);
	}

	static Stream<Arguments> provideValidLineForRemoval() {
        final Line 신분당선 = Line.of("신분당선", "red");
        final Station 강남역 = Station.of("강남역");
        final Station 양재역 = Station.of("양재역");
        final Station 판교역 = Station.of("판교역");

        final Section 강남_양재_구간 = Section.of(강남역, 양재역, 10);
        final Section 양재_판교_구간 = Section.of(양재역, 판교역, 10);

        신분당선.addSection(강남_양재_구간);
        신분당선.addSection(양재_판교_구간);

        return Stream.of(
                Arguments.of(
                        신분당선,
                        판교역
                )
        );
	}

	/**
	 * Given 지하철 노선에 구간이 1개가 아닌 경우
	 * Given 지하철 노선에 등록된 마지막 역(하행 종점역) 일 경우
	 * Then 구간 삭제 예외가 발생하지 않는다.
	 */
	@DisplayName("구간 삭제 유효")
	@ParameterizedTest
	@MethodSource("provideValidLineForRemoval")
	void validRemoveSection(Line 신분당선, Station 신분당선_종점역) {
		assertThatNoException()
				.isThrownBy(() -> 신분당선.removeSection(신분당선_종점역));

	}

	/**
	 * Given 지하철 노선에 상행 종점역과 하행 종점역만 있는 경우(구간이 1개인 경우)
	 * Then InvalidSectionRemovalException 예외를 던진다.
	 */
	@DisplayName("구간 삭제시 구간 개수 오류")
	@ParameterizedTest
	@MethodSource("provideOnlyOneSectionLine")
	void invalidLineHasLessThanOneSectionTest(Line 신분당선, Station 종점역) {
		assertThatThrownBy(() -> 신분당선.removeSection(종점역))
				.isInstanceOf(InvalidSectionRemovalException.class);
	}

	/**
	 * Given 지하철 노선에 등록된 마지막 역(하행 종점역)이 아닌 경우
	 * Then InvalidSectionRemovalException 예외를 던진다.
	 */
	@DisplayName("구간 삭제시 하행 종점역 오류")
	@ParameterizedTest
	@MethodSource("provideLineAndNotLastDownStation")
	void invalidLastDownStationTest(Line 신분당선, Station 양재역) {
		assertThatThrownBy(() -> 신분당선.removeSection(양재역))
				.isInstanceOf(InvalidSectionRemovalException.class);
	}

}