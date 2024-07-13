package subway.line.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.line.exception.InsufficientStationsException;
import subway.line.exception.InvalidDownStationException;
import subway.line.exception.InvalidUpStationException;
import subway.line.exception.NotTerminusStationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LineTest {

    private Line line;
    private Long 상행역;
    private Long 하행역;

    @BeforeEach
    void setUp() {
        상행역 = 1L;
        하행역 = 2L;
        line = new Line("2호선", "green", new Section(상행역, 하행역, 10L));
    }

    @DisplayName("새로 등록하려는 상행역이 기존 하행역이 아니면 에러를 발생시킨다")
    @Test
    void test1() {
        //when 새로 등록하려는 상행역이 기존 하행역이 아니면
        var 새상행역 = 3L;
        var 새하행역 = 4L;
        //then 에러를 발생시킨다
        assertThrows(InvalidUpStationException.class, () -> {
            line.addSection(새상행역, 새하행역, 20L);
        });

    }

    @DisplayName("새로 등록하려는 하행역이 이미 등록된 경우 에러를 발생시킨다")
    @Test
    void test2() {
        //when 새로 등록하려는 하행역이 이미 등록된 경우
        var 새상행역 = 2L;
        var 새하행역 = 1L;
        //then 에러를 발생시킨다
        assertThrows(InvalidDownStationException.class, () ->
                line.addSection(새상행역, 새하행역, 20L)
        );

    }

    @DisplayName("역이 3개 이상일 때 삭제하려는 역이 기존 종착역이 아닌 경우 에러를 발생시킨다")
    @Test
    void test3() {
        var 역3 = 3L;
        line.addSection(하행역, 역3, 10L);

        //when 삭제하려는 역이 현재 종착역이 아닌 경우
        //then 에러를 발생시킨다
        assertThrows(NotTerminusStationException.class, () ->
                line.removeLastStation(하행역)
        );
    }

    @DisplayName("역이 2개 이하로 존재하는 경우 하행역 삭제시 에러를 발생시킨다.")
    @Test
    void test4() {
        //역이 2개 이하로 존재하는 경우
        //when 하행역 삭제시
        //then 에러를 발생시킨다
        assertThrows(InsufficientStationsException.class, () ->
                line.removeLastStation(하행역)
        );
    }

    @DisplayName("역이 3개 이상일때 마지막역을 삭제하면 조회되지 않는다")
    @Test
    void test5() {
        //역이 3개 이상일때
        var 역3 = 3L;
        line.addSection(하행역, 역3, 10L);

        //마지막역을 삭제하면
        line.removeLastStation(역3);

        //then 조회되지 않는다
        assertThat(line.getStationIds()).containsExactly(상행역, 하행역);
    }

}
