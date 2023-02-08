package subway.section;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.line.LineRestAssured.Location_조회;
import static subway.line.LineRestAssured.노선_생성;
import static subway.section.SectionAssert.구간_등록_검증;
import static subway.section.SectionAssert.구간_조회_검증;
import static subway.section.SectionRestAssured.구간_등록;
import static subway.section.SectionRestAssured.구간_제거;
import static subway.station.StationRestAssured.역_생성;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import subway.util.DatabaseCleanup;

@ActiveProfiles("acceptance")
@DisplayName("구간 관련 기능")
@TestConstructor(autowireMode = AutowireMode.ALL)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class SectionAcceptanceTest {

    @LocalServerPort
    private int port;

    private final DatabaseCleanup databaseCleanup;

    private Long lineId;
    private Long upStationId;
    private Long downStationId;
    private Long registerStationId;
    private String lineLocation;
    private int distance;

    public SectionAcceptanceTest(final DatabaseCleanup databaseCleanup) {
        this.databaseCleanup = databaseCleanup;
    }

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        databaseCleanup.truncateTable();
        upStationId = 역_생성("상행역").jsonPath().getLong("id");
        downStationId = 역_생성("하행역").jsonPath().getLong("id");
        registerStationId = 역_생성("새로운역").jsonPath().getLong("id");
        lineLocation = 노선_생성("2호선", "bg-red-600", upStationId, downStationId, distance)
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
            ExtractableResponse<Response> response = 구간_등록(lineId, registerStationId, downStationId, distance);

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

            // then
            구간_등록_검증(lineId, registerStationId, downStationId);
        }

        /**
         * When 노선에 구간을 등록했을 때 구간의 상행역이 해당 노선에 하행역이 아닐 경우
         * Then 에러 처리한다.
         */
        @DisplayName("노선에 구간을 등록했을 때 구간의 상행역이 해당 노선에 하행역이 아닐 경우 에러 처리한다.")
        @Test
        void registerSection_Error_SectionUpStationIsNotLineDownStation() {
            // when
            ExtractableResponse<Response> response = 구간_등록(lineId, registerStationId, registerStationId, distance);

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
        void registerSection_Error_SectionDownStationAlreadyRegisteredSection() {
            // given
            노선_생성("새로운 노선", "색깔", registerStationId, downStationId, 10);

            // when
            ExtractableResponse<Response> response = 구간_등록(lineId, registerStationId, downStationId, distance);

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
            구간_등록(lineId, registerStationId, downStationId, distance);

            // when
            구간_제거(lineId, registerStationId);

            // then
            구간_조회_검증(lineLocation, downStationId);
        }

        /**
         * Given 구간을 생성하고
         * When 제거할 구간이 노선의 하행 종점역이 아닌 경우
         * Then 에러 처리한다.
         */
        @DisplayName("제거할 구간이 노선의 하행 종점역이 아닌 경우 에러 처리한다.")
        @Test
        void removeSection_Error_RemoveSectionIsNotDownStation() {
            // given
            구간_등록(lineId, registerStationId, downStationId, distance);

            // when
            ExtractableResponse<Response> response = 구간_제거(lineId, downStationId);

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
        void removeSection_Error_LineOnlyHasUpStationAndDownStation() {
            // given
            구간_등록(lineId, registerStationId, downStationId, distance);

            // when
            ExtractableResponse<Response> response = 구간_제거(lineId, downStationId);

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }
    }
}
