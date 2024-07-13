package subway.section;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import subway.line.payload.AddSectionRequest;
import subway.station.StationApiRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static subway.line.LineApiRequest.create;
import static subway.line.LineApiRequest.노선을_조회한다;
import static subway.section.SectionApiRequest.구간을_추가한다;
import static subway.section.SectionApiRequest.노선에서_역을_삭제한다;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("지하철 구간 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SectionAcceptanceTest {

    private Long 최초상행종점역;
    private Long 최초하행종점역;
    private Long 삼성역;
    private Long 잠실역;

    private Long 수인분당선;

    @BeforeEach
    void setUp() {
        최초상행종점역 = StationApiRequest.create("최초상행종점역").jsonPath().getLong("id");
        최초하행종점역 = StationApiRequest.create("최초하행종점역").jsonPath().getLong("id");
        삼성역 = StationApiRequest.create("삼성역").jsonPath().getLong("id");
        잠실역 = StationApiRequest.create("잠실역").jsonPath().getLong("id");

        수인분당선 = create("수인분당선", "yellow", 최초상행종점역, 최초하행종점역, 10L).jsonPath().getLong("id");
    }

    @Nested
    class WhenAddSection {


        @DisplayName("새로운 구간 등록후 해당 노선을 조회하면 등록된 모든 역을 확인 할 수 있다.")
        @Test
        void addSection() {
            //given 새로운 구간 등록에 성공하면
            var 신규구간 = new AddSectionRequest(최초하행종점역, 삼성역, 10L);
            var 생성_결과 = 구간을_추가한다(수인분당선, 신규구간);

            //when 노선 조회시
            var 노선목록 = 노선을_조회한다(String.format("/lines/%d", 수인분당선)).jsonPath();

            //then 등록된 모든 역을 확인할 수 있다.
            assertAll(() -> {
                        assertThat(생성_결과.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
                        assertThat(노선목록.getList("stations.name", String.class)).containsExactly("최초상행종점역", "최초하행종점역", "삼성역");
                    }
            );

        }


        @DisplayName("기존 하행종점역이 새로운 구간의 상행종점역이 아니면 새 구간 등록시 400 상태코드를 반환한다.")
        @Test
        void failTest1() {
            //given 기존 구간에
            //when 추가하는 역이 기존의 하행종점역이 아닌 경우
            var 신규구간 = new AddSectionRequest(최초상행종점역, 삼성역, 10L);
            var 생성_결과 = 구간을_추가한다(수인분당선, 신규구간);

            //then 400 상태코드를 반환한다
            assertThat(생성_결과.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }

        @DisplayName("이미 노선에 등록되어있는 역을 하행종점역으로 등록하면 400 상태코드를 반환한다.")
        @Test
        void failTest2() {
            //given 기존 구간에
            //when 이미 노선에 등록되어있는 역을 하행종점역으로 등록하면
            var 신규구간 = new AddSectionRequest(최초하행종점역, 최초상행종점역, 10L);
            var 생성_결과 = 구간을_추가한다(수인분당선, 신규구간);

            //then 400 상태코드를 반환한다
            assertThat(생성_결과.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }

    }

    @Nested
    class WhenDeleteSection {

        @DisplayName("역 삭제에 성공후 노선을 조회하면 삭제된 역이 조회되지 않는다.")
        @Test
        void deleteSection() {
            //given 신규구간 추가 후
            var 신규구간 = new AddSectionRequest(최초하행종점역, 삼성역, 10L);
            구간을_추가한다(수인분당선, 신규구간);


            //when 마지막 역 삭제에 성공하면 노선 조회시
            var 삭제_결과 = 노선에서_역을_삭제한다(수인분당선, 삼성역);
            var 노선 = 노선을_조회한다(String.format("/lines/%d", 수인분당선)).jsonPath();

            //then 삭제된 역이 다시 조회되지 않는다
            assertAll(() -> {
                        assertThat(삭제_결과.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
                        assertThat(노선.getList("stations.name", String.class)).containsExactly("최초상행종점역", "최초하행종점역");
                    }
            );
        }


        @DisplayName("제거하려는 역이 선택된 노선의 하행종점역이 아니면 400 상태코드를 반환한다.")
        @Test
        void failTest1() {
            //given 신규구간 추가 후
            var 신규구간 = new AddSectionRequest(최초하행종점역, 삼성역, 10L);
            구간을_추가한다(수인분당선, 신규구간);

            //when 하행종점역이 아닌 역을 삭제하면
            var 삭제_결과 = 노선에서_역을_삭제한다(수인분당선, 최초하행종점역);

            //then 400 상태코드를 반환한다
            assertThat(삭제_결과.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }

        @DisplayName("역이 2개 이하로 존재하는 경우 삭제시 400 상태코드를 반환한다.")
        @Test
        void failTest2() {
            //given 역이 2개 이하로 존재하는 경우

            //when 삭제시
            var 삭제_결과 = 노선에서_역을_삭제한다(수인분당선, 최초하행종점역);

            //then 400 상태코드를 반환한다
            assertThat(삭제_결과.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }

    }

}
