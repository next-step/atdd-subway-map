package subway.line.acceptance;

import core.AcceptanceTest;
import core.RestAssuredHelper;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import subway.common.LineApiHelper;
import subway.common.SectionApiHelper;
import subway.common.StationApiHelper;
import subway.line.service.dto.LineResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertAll;

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
    private final int 구간_distance = 5;


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
            final ExtractableResponse<Response> response = SectionApiHelper.createSection(신분당선_Id, 새로운지하철역_Id, 또다른지하철역_Id, 구간_distance);

            // then
            assertAll(
                    () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                    SectionAcceptanceTest.this::assertSectionAdded
            );
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
            final ExtractableResponse<Response> response = SectionApiHelper.createSection(신분당선_Id, 지하철역_Id, 또다른지하철역_Id, 구간_distance);

            // then
            assertAll(
                    () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                    SectionAcceptanceTest.this::assertSectionsNotChanged
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
            final ExtractableResponse<Response> response = SectionApiHelper.createSection(신분당선_Id, 새로운지하철역_Id, 지하철역_Id, 구간_distance);

            // then
            assertAll(
                    () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                    SectionAcceptanceTest.this::assertSectionsNotChanged
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
            SectionApiHelper.createSection(신분당선_Id, 새로운지하철역_Id, 또다른지하철역_Id, 구간_distance);

            // when
            final ExtractableResponse<Response> response = SectionApiHelper.removeSection(신분당선_Id, 또다른지하철역_Id);

            // then
            assertAll(
                    () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                    SectionAcceptanceTest.this::assertSectionsNotChanged
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
            assertAll(
                    () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                    SectionAcceptanceTest.this::assertSectionsNotChanged
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
            SectionApiHelper.createSection(신분당선_Id, 새로운지하철역_Id, 또다른지하철역_Id, 구간_distance);

            // when
            final ExtractableResponse<Response> response = SectionApiHelper.removeSection(신분당선_Id, 새로운지하철역_Id);

            // then
            assertAll(
                    () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                    SectionAcceptanceTest.this::assertSectionAdded
            );
        }

    }

    private void assertSectionAdded() {
        assertSoftly(softly -> {
            final LineResponse lineResponse = LineApiHelper.fetchLineById(신분당선_Id).as(LineResponse.class);
            softly.assertThat(lineResponse.getDistance()).isEqualTo(신분당선_distance + 구간_distance);
            softly.assertThat(lineResponse.getStations())
                    .extracting("id").containsExactly(지하철역_Id, 새로운지하철역_Id, 또다른지하철역_Id);
        });
    }

    private void assertSectionsNotChanged() {
        assertSoftly(softly -> {
            final LineResponse lineResponse = LineApiHelper.fetchLineById(신분당선_Id).as(LineResponse.class);
            softly.assertThat(lineResponse.getDistance()).isEqualTo(신분당선_distance);
            softly.assertThat(lineResponse.getStations())
                    .extracting("id").containsExactly(지하철역_Id, 새로운지하철역_Id);
        });
    }


}
