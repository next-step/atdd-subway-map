package nextstep.subway.domain;

import nextstep.subway.application.exception.DownStationInvalidException;
import nextstep.subway.application.exception.UpStationInvalidException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CreateSectionValidationTest {

	static Stream<Arguments> provideInvalidDownStationSectionParameter() {
        final Line 신분당선 = Line.of("신분당선", "red");
        final Station 강남역 = Station.of("강남역");
        final Station 판교역 = Station.of("판교역");
        final Station 양재역 = Station.of("양재역");

        final Section 강남_판교_구간 = Section.of(강남역, 판교역, 10);
        final Section 양재_판교_구간 = Section.of(양재역, 판교역, 10);

        // 상행 종점역과 / 하행 종점역을 추가
        신분당선.addSection(강남_판교_구간);

        return Stream.of(
                Arguments.of(
                        신분당선,
                        양재_판교_구간
                )
        );
	}

	static Stream<Arguments> provideInvalidUpStationSectionParameter() {
        final Line 신분당선 = Line.of("신분당선", "red");
        final Station 강남역 = Station.of("강남역");
        final Station 양재역 = Station.of("양재역");
        final Station 판교역 = Station.of("판교역");
        final Station 정자역 = Station.of("정자역");
        final Section 강남_양재_구간 = Section.of(강남역, 양재역, 10);
        final Section 양재_정자_구간 = Section.of(양재역, 정자역, 10);

        // 양재역이 신분당선의 하행 종점역이 아님
        final Section 양재_판교_구간 = Section.of(양재역, 판교역, 10);

        신분당선.addSection(강남_양재_구간);
        신분당선.addSection(양재_정자_구간);

        return Stream.of(
                Arguments.of(
                        신분당선,
						양재_판교_구간
				)
		);
	}

	/**
	 * Given 새로운 구간의 하행역이 노선에 이미 등록된 역일 경우
	 * Then DownStationInvalidException 예외를 던진다.
	 */
	@DisplayName("구간 등록시 하행역 오류")
	@ParameterizedTest
	@MethodSource("provideInvalidDownStationSectionParameter")
	void invalidDownStationTest(Line 신분당선, Section 양재_판교_구간) {
		assertThatThrownBy(() -> 신분당선.addSection(양재_판교_구간))
				.isInstanceOf(DownStationInvalidException.class);
	}

	/**
	 * Given 새로운 구간의 상행역이 등록될 노선의 하행 종점역이 아닌 경우
	 * Then UpStationInvalidException 예외를 던진다.
	 */
	@DisplayName("구간 등록시 상행역 오류")
	@ParameterizedTest
	@MethodSource("provideInvalidUpStationSectionParameter")
	void invalidUpStationTest(Line 신분당선, Section 양재_판교_구간) {
		assertThatThrownBy(() -> 신분당선.addSection(양재_판교_구간))
				.isInstanceOf(UpStationInvalidException.class);
	}

}