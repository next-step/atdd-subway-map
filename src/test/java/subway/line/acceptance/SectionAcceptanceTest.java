package subway.line.acceptance;

import core.AcceptanceTest;
import core.RestAssuredHelper;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;
import subway.common.LineApiHelper;
import subway.common.SectionApiHelper;
import subway.common.StationApiHelper;
import subway.line.service.dto.LineResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DisplayName("구간 관련 기능")
@AcceptanceTest
public class SectionAcceptanceTest {

    private Long 지하철역_Id;
    private Long 새로운지하철역_Id;
    private Long 또다른지하철역_Id;
    private Long 신분당선_Id;
    private final String 신분당선 = "신분당선";
    private final String 신분당선_color = "bg-red-600";
    private final int 신분당선_distance = 10;

    @BeforeEach
    void setUp() {
        지하철역_Id = RestAssuredHelper.getIdFrom(StationApiHelper.createStation("지하철역"));
        새로운지하철역_Id = RestAssuredHelper.getIdFrom(StationApiHelper.createStation("새로운지하철역"));
        또다른지하철역_Id = RestAssuredHelper.getIdFrom(StationApiHelper.createStation("또다른지하철역"));
        신분당선_Id = RestAssuredHelper.getIdFrom((LineApiHelper.createLine(신분당선, 신분당선_color, 지하철역_Id, 새로운지하철역_Id, 신분당선_distance)));
    }

    @Nested
    @DisplayName("구간 생성")
    class Creation {
        /**
         * When 지하철 구간을 생성하면
         * Then 지하철 노선 조회시 구간 정보와 함께 조회할 수 있다.
         */
        @DisplayName("성공")
        @Test
        void createSectionTest() {
            // when
            final int 구간_distance = 5;
            final ExtractableResponse<Response> response = SectionApiHelper.createSection(신분당선_Id, 새로운지하철역_Id, 또다른지하철역_Id, 구간_distance);

            // then
            assertSoftly(softly -> {
                softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
                final LineResponse lineResponse = LineApiHelper.fetchLineById(신분당선_Id).as(LineResponse.class);
                softly.assertThat(lineResponse.getDistance()).isEqualTo(신분당선_distance + 구간_distance);
                softly.assertThat(lineResponse.getStations())
                        .extracting("id").containsExactly(지하철역_Id, 새로운지하철역_Id, 또다른지하철역_Id);
            });
        }

        /**
         * When 지하철 구간을 생성하는데
         * When 구간 상행역이 해당 노선의 하행 종점역이 아니라면
         * Then 에러가 난다.
         */
        @DisplayName("실패 - 구간 상행역이 해당 노선의 하행 종점역이 아닐경우 실패한다.")
        @Test
        void createSectionFail_UpStationIsNotTheSameWithDownStationOfLineTest() {
            // when
            final int 구간_distance = 5;
            final ExtractableResponse<Response> response = SectionApiHelper.createSection(신분당선_Id, 지하철역_Id, 또다른지하철역_Id, 구간_distance);

            // then
            Assertions.assertAll(
                    () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                    SectionAcceptanceTest.this::assertStationNotChanged
            );
        }

        /**
         * When 지하철 구간을 생성하는데
         * When 구간 하행역이 해당 노선에 등록되어 있다면
         * Then 에러가 난다.
         */
        @DisplayName("실패 - 구간 하행역이 해당 노선에 등록되어 있다면 실패한다.")
        @Test
        void createSectionFail_DownStationIsAlreadyInTest() {
            // when
            final int 구간_distance = 5;
            final ExtractableResponse<Response> response = SectionApiHelper.createSection(신분당선_Id, 새로운지하철역_Id, 지하철역_Id, 구간_distance);

            // then
            Assertions.assertAll(
                    () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                    SectionAcceptanceTest.this::assertStationNotChanged
            );
        }

    }

    @Nested
    @DisplayName("구간 제거")
    class Deletion {
        /**
         * Given 지하철 구간을 생성하고
         * When 지하철 구간을 제거하면
         * Then 지하철 노선 조회시 해당 구간 정보가 제외되고 조회된다.
         */
        @DisplayName("성공")
        @Test
        void removeSectionTest() {
            // given
            final int 구간_distance = 5;
            SectionApiHelper.createSection(신분당선_Id, 새로운지하철역_Id, 또다른지하철역_Id, 구간_distance);

            // when
            final ExtractableResponse<Response> response = SectionApiHelper.removeSection(신분당선_Id, 또다른지하철역_Id);

            // then
            Assertions.assertAll(
                    () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                    SectionAcceptanceTest.this::assertStationNotChanged
            );
        }

        /**
         * When 지하철 구간을 제거하는데
         * When 해당 지하철 구간이 한개만 남아 있다면
         * Then 에러가 난다.
         */
        @DisplayName("실패 - 해당 지하철 노선에 구간이 한개만 남아 있다면 실패한다.")
        @Test
        void removeSectionFail_OnlyOneSectionLeftTest() {
            // when
            final ExtractableResponse<Response> response = SectionApiHelper.removeSection(신분당선_Id, 새로운지하철역_Id);

            // then
            Assertions.assertAll(
                    () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                    SectionAcceptanceTest.this::assertStationNotChanged
            );
        }

        /**
         * Given 지하철 구간을 생성하고
         * When 지하철 구간을 제거하는데
         * When 해당 지하철 구간이 마지막 구간이 아니면
         * Then 에러가 난다.
         */
        @DisplayName("실패 - 삭제 구간이 해당 지하철 노선 구간의 마지막 구간이 아니면 실패한다.")
        @Test
        void createSectionFail_TargetSectionIsNotLastSectionTest() {
            // given
            final int 구간_distance = 5;
            SectionApiHelper.createSection(신분당선_Id, 새로운지하철역_Id, 또다른지하철역_Id, 구간_distance);

            // when
            final ExtractableResponse<Response> response = SectionApiHelper.removeSection(신분당선_Id, 새로운지하철역_Id);

            // then
            assertSoftly(softly -> {
                softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
                final LineResponse lineResponse = LineApiHelper.fetchLineById(신분당선_Id).as(LineResponse.class);
                softly.assertThat(lineResponse.getDistance()).isEqualTo(신분당선_distance + 구간_distance);
                softly.assertThat(lineResponse.getStations())
                        .extracting("id").containsExactly(지하철역_Id, 새로운지하철역_Id, 또다른지하철역_Id);
            });
        }

    }

    private void assertStationNotChanged() {
        assertSoftly(softly -> {
            final LineResponse lineResponse = LineApiHelper.fetchLineById(신분당선_Id).as(LineResponse.class);
            softly.assertThat(lineResponse.getDistance()).isEqualTo(신분당선_distance);
            softly.assertThat(lineResponse.getStations())
                    .extracting("id").containsExactly(지하철역_Id, 새로운지하철역_Id);
        });
    }


}
