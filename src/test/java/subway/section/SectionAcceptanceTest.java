package subway.section;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.line.LineRestAssured.Location_조회;
import static subway.line.LineRestAssured.노선_생성;
import static subway.section.SectionAssert.구간_등록_검증;
import static subway.section.SectionAssert.구간_조회_검증;
import static subway.section.SectionRestAssured.구간_등록;
import static subway.section.SectionRestAssured.구간_제거;
import static subway.station.StationRestAssured.역_생성;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import subway.util.RandomPortAcceptanceTest;

@DisplayName("구간 관련 기능")
public class SectionAcceptanceTest extends RandomPortAcceptanceTest {

    private Long lineId;
    private Long 강남역;
    private Long 양재역;
    private Long 선릉역;
    private Long 정자역;
    private String lineLocation;
    private int distance;

    @Override
    @BeforeEach
    protected void setUp() {
        super.setUp();
        강남역 = 역_생성("강남역").jsonPath().getLong("id");
        양재역 = 역_생성("양재역").jsonPath().getLong("id");
        선릉역 = 역_생성("선릉역").jsonPath().getLong("id");
        정자역 = 역_생성("정자역").jsonPath().getLong("id");
        lineLocation = 노선_생성("2호선", "bg-red-600", 강남역, 양재역, distance)
                .header(HttpHeaders.LOCATION);
        lineId = Location_조회(lineLocation).jsonPath().getLong("id");
        distance = 10;
    }

    @DisplayName("구간 등록 관련 기능")
    @Nested
    class RegisterSectionTest {

        /**
         * When 지하철 노선에 새로운 구간을 등록하면
         * Then 새로운 구간은 노선에 등록되어있는 하행 종점역이어야 한다.
         * And 새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 한다.
         */
        @DisplayName("노선에 새로운 구간을 등록한다.")
        @Test
        void registerSection() {
            // when
            ExtractableResponse<Response> response = 구간_등록(lineId, 선릉역, 양재역, distance);

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

            // then
            구간_등록_검증(lineId, 선릉역);
        }

        /**
         * When 노선에 구간을 등록했을 때 구간의 상행역이 해당 노선에 하행역이 아닐 경우
         * Then 에러 처리한다.
         */
        @DisplayName("노선에 구간을 등록했을 때 구간의 상행역이 해당 노선에 하행역이 아닐 경우 에러 처리한다.")
        @Test
        void registerSectionUpStationIsNotLineDownStation() {
            // when
            ExtractableResponse<Response> response = 구간_등록(lineId, 선릉역, 정자역, distance);

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }

        /**
         * Given 노선을 생성하고
         * When 노선에 구간을 등록했을 때 구간의 하행역이 해당 노선에 등록되어 있는 역일 경우
         * Then 에러 처리한다.
         */
        @DisplayName("노선에 새로운 구간 등록시 새로운 구간의 하행역이 해당 노선에 등록되어 있는 역일 경우 에러 처리한다.")
        @Test
        void registerSectionDownStationAlreadyRegisteredSection() {
            // given
            노선_생성("새로운 노선", "색깔", 선릉역, 양재역, 10);

            // when
            ExtractableResponse<Response> response = 구간_등록(lineId, 선릉역, 양재역, distance);

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }
    }

    @DisplayName("구간 제거 관련 기능")
    @Nested
    class RemoveSectionTest {

        /**
         * Given 구간을 생성하고
         * When 노선의 구간을 제거하면
         * Then 노선의 하행 종점역은 제거한 구간의 상행역이어야 한다.
         */
        @DisplayName("노선의 구간을 제거한다.")
        @Test
        void removeSection() {
            // given
            구간_등록(lineId, 선릉역, 양재역, distance);

            // when
            구간_제거(lineId, 선릉역);

            // then
            구간_조회_검증(lineLocation, 양재역);
        }

        /**
         * Given 구간을 생성하고
         * When 제거할 구간이 노선의 하행 종점역이 아닌 경우
         * Then 에러 처리한다.
         */
        @DisplayName("제거할 구간이 노선의 하행 종점역이 아닌 경우 에러 처리한다.")
        @Test
        void removeSectionIsNotDownStation() {
            // given
            구간_등록(lineId, 선릉역, 양재역, distance);

            // when
            ExtractableResponse<Response> response = 구간_제거(lineId, 양재역);

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }

        /**
         * Given 구간을 생성하고
         * When 구간 제거시 노선에 상행 종점역과 하행 종점역만 있는 경우
         * Then 에러 처리한다.
         */
        @DisplayName("구간 제거시 노선에 상행 종점역과 하행 종점역만 있는 경우 에러 처리한다.")
        @Test
        void removeSectionLineOnlyHasUpStationAndDownStation() {
            // given
            구간_등록(lineId, 선릉역, 양재역, distance);

            // when
            ExtractableResponse<Response> response = 구간_제거(lineId, 양재역);

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }
    }
}
