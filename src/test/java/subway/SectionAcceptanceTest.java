package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import subway.controller.dto.line.LineSaveRequest;
import subway.controller.dto.section.SectionSaveRequest;
import subway.controller.dto.station.StationResponse;
import subway.utils.LineApiHelper;
import subway.utils.StationApiHelper;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.utils.SectionApiHelper.callApiToCreateSection;
import static subway.utils.SectionApiHelper.callApiToDeleteSection;

@Sql("truncate_tables.sql")
@DisplayName("지하철 구간 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SectionAcceptanceTest {

    private Long stationId_A;
    private Long stationId_B;
    private Long stationId_C;
    private Long stationId_D;
    private String stationName_A = "지하철역 A";
    private String stationName_B = "지하철역 B";
    private String stationName_C = "지하철역 C";
    private String stationName_D = "지하철역 D";

    @BeforeEach
    public void init() {
        stationId_A = Station_생성(stationName_A);
        stationId_B = Station_생성(stationName_B);
        stationId_C = Station_생성(stationName_C);
        stationId_D = Station_생성(stationName_D);
    }

    @DisplayName("노선 생성 테스트")
    @Nested
    public class 노선_생성_테스트 {
        /**
         * Given 상행종점역이 A이고, 하행종점역이 B인 지하철 노선을 생성하고
         * When 지하철역 B와 C를 연결하는 구간을 생성하면
         * Then 해당 노선이 A-B-C로 연결되고,
         * Then 지하철 노선의 하행역이 C로 바뀐다.
         */
        @DisplayName("2개역이 있는 노선에 지하철 구간을 추가한다.")
        @Test
        void createSection_성공__2개역_노선() {

            // given
            Long lineId = LINE_생성(stationId_A, stationId_B);

            // when
            ExtractableResponse<Response> creationResponse = Section_생성(lineId, stationId_B, stationId_C);

            // then
            HTTP_STATUS_검증(creationResponse, HttpStatus.CREATED);

            Long[] expectedStationIds = {stationId_A, stationId_B, stationId_C};
            String[] expectedStationNames = {stationName_A, stationName_B, stationName_C};
            LINE의_스테이션_전체_조회_및_검증(lineId, expectedStationIds, expectedStationNames);
        }

        /**
         * Given 상행종점역이 A이고, 하행종점역이 C인 지하철 노선 A-B-C를 생성하고
         * When 지하철역 C와 D를 연결하는 구간을 생성하면
         * Then 해당 노선이 A-B-C-D로 연결되고,
         * Then 지하철 노선의 하행역이 D로 바뀐다.
         */
        @DisplayName("3개역이 있는 노선에 지하철 구간을 추가한다.")
        @Test
        void createSection_성공__3개역_노선() {

            // given
            Long lineId = 노선_A_B_C_생성();

            // when
            ExtractableResponse<Response> creationResponse = Section_생성(lineId, stationId_C, stationId_D);

            // then
            HTTP_STATUS_검증(creationResponse, HttpStatus.CREATED);

            Long[] expectedStationIds = {stationId_A, stationId_B, stationId_C, stationId_D};
            String[] expectedStationsNames = {stationName_A, stationName_B, stationName_C, stationName_D};
            LINE의_스테이션_전체_조회_및_검증(lineId, expectedStationIds, expectedStationsNames);
        }

        /**
         * Given 상행종점역이 A이고, 하행종점역이 B인 지하철 노선 A-B를 생성하고
         * When 지하철역 C와 하행종점역이 아닌 지하철역 A를 연결하는 구간을 생성하면
         * Then 에러를 응답한다.
         */
        @DisplayName("[오류] 노선의 하행 종점역이 아닌 지하철역을 상행역으로 하는 구간을 추가한다.")
        @Test
        void createSection_에러__하행종점역이_아닌_상행역_구간() {

            // given
            Long lineId = LINE_생성(stationId_A, stationId_B);

            // when
            ExtractableResponse<Response> creationResponse = Section_생성(lineId, stationId_C, stationId_A);

            // then
            HTTP_STATUS_검증(creationResponse, HttpStatus.INTERNAL_SERVER_ERROR);

        }

        /**
         * Given 상행종점역이 A이고, 하행종점역이 C인 지하철 노선 A-B-C를 생성하고
         * When 지하철역 C와 이미 노선에 존재하는 B를 연결하는 구간을 생성하면
         * Then 에러를 응답한다.
         */
        @DisplayName("[오류] 이미 노선에 등록된 지하철역을 하행역으로 하는 지하철 구간을 추가한다.")
        @Test
        void createSection_에러__노선에_등록된_하행역() {

            // given
            Long lineId = 노선_A_B_C_생성();

            // when
            ExtractableResponse<Response> creationResponse = Section_생성(lineId, stationId_C, stationId_B);

            // then
            HTTP_STATUS_검증(creationResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DisplayName("노선 삭제 테스트")
    @Nested
    public class 노선_삭제_테스트 {
        /**
         * Given 지하철 노선 A-B-C를 생성하고
         * When 지하철역 B-C 구간을 삭제하면
         * Then 노선이 A-B만 남는다.
         */
        @DisplayName("노선 A-B-C에서 B-C구간을 삭제한다.")
        @Test
        void deleteSection_성공() {

            // given
            Long lineId = 노선_A_B_C_생성();

            // when
            ExtractableResponse<Response> deletionResponse = 구간_삭제(lineId, stationId_C);

            // then
            HTTP_STATUS_검증(deletionResponse, HttpStatus.NO_CONTENT);

            Long[] expectedStationIds = {stationId_A, stationId_B};
            String[] expectedStationNames = {stationName_A, stationName_B};
            LINE의_스테이션_전체_조회_및_검증(lineId, expectedStationIds, expectedStationNames);
        }

        /**
         * Given 지하철 노선 A-B를 생성하고
         * When 지하철 A-B 구간을 삭제하려고 하면
         * Then 에러가 발생한다.
         */
        @DisplayName("[오류] 구간이 1개 뿐인 노선의 구간을 삭제한다.")
        @Test
        void deleteSection_에러__구간_1개_노선의_구간_삭제() {

            // given
            Long lineId = LINE_생성(stationId_A, stationId_B);

            // when
            ExtractableResponse<Response> deletionResponse = 구간_삭제(lineId, stationId_B);

            // then
            HTTP_STATUS_검증(deletionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        /**
         * Given 지하철 노선 A-B-C를 생성하고
         * When 지하철 A-B 구간을 삭제하려고 하면
         * Then 에러가 발생한다.
         */
        @DisplayName("[오류] 마지막 구간이 아닌 노선을 삭제한다.")
        @Test
        void deleteSection_에러__마지막_구간이_아닌_구간_삭제() {

            // given
            Long lineId = 노선_A_B_C_생성();

            // when
            ExtractableResponse<Response> deletionResponse = 구간_삭제(lineId, stationId_B);

            // then
            HTTP_STATUS_검증(deletionResponse, HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }




    private ExtractableResponse<Response> Section_생성(Long lineId, Long upStationId, Long downStationId) {
        return callApiToCreateSection(lineId, SectionSaveRequest.builder()
                                                                .upStationId(upStationId)
                                                                .downStationId(downStationId)
                                                                .distance(3L)
                                                                .build());
    }

    private static Long LINE_생성(Long upStationId, Long downStationId) {
        return LineApiHelper.callApiToCreateLine(LineSaveRequest.builder()
                                                                .name("테스트 노선")
                                                                .color("bg-red-600")
                                                                .upStationId(upStationId)
                                                                .downStationId(downStationId)
                                                                .distance(10L)
                                                                .build())
                            .jsonPath()
                            .getLong("id");
    }

    private long Station_생성(String stationName) {
        return StationApiHelper.callApiToCreateStation(stationName)
                               .jsonPath()
                               .getLong("id");
    }


    private static void HTTP_STATUS_검증(ExtractableResponse<Response> creationResponse, HttpStatus httpStatus) {
        assertThat(creationResponse.statusCode()).isEqualTo(httpStatus.value());
    }


    private static void LINE의_스테이션_전체_조회_및_검증(Long lineId, Long[] expectedStationIds, String[] expectedStationNames) {
        List<StationResponse> stations = LineApiHelper.callApiToGetSingleLine(lineId)
                                                      .jsonPath()
                                                      .getList("stations", StationResponse.class);
        Assertions.assertAll(
                () -> assertThat(stations).hasSize(expectedStationIds.length),
                () -> assertThat(stations.stream()
                               .map(StationResponse::getId)
                               .collect(Collectors.toList())).contains(expectedStationIds),
                () -> assertThat(stations.stream()
                               .map(StationResponse::getName)
                               .collect(Collectors.toList())).contains(expectedStationNames)
        );
    }

    private Long 노선_A_B_C_생성() {
        Long lineId = LINE_생성(stationId_A, stationId_B);
        Section_생성(lineId, stationId_B, stationId_C);
        return lineId;
    }

    private ExtractableResponse<Response> 구간_삭제(Long lineId, Long stationId) {
        return callApiToDeleteSection(lineId, stationId);
    }

}
