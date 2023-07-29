package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import lombok.val;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import subway.line.dto.CreateLineRequest;
import subway.line.dto.LineResponse;
import subway.section.dto.CreateSectionRequest;
import subway.station.dto.StationRequest;
import subway.station.dto.StationResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.RestAssuredWrapper.*;

class SectionAcceptanceTest extends AcceptanceTestBase {
    private static final String TEST_COLOR = "bg-test-600";
    private static final String BASE_PATH = "/lines/%d/sections";
    private static final String DELETE_PATH = "/lines/%d/sections?stationId=%d";
    private static final Long NON_EXISTENT_SUBWAY_LINE_ID = 100L;
    private static final String 신분당선 = "신분당선";
    private static Long 신분당선_ID;
    private static Long 신분당선_상행종점역_ID;
    private static Long 신분당선_하행종점역_ID;

    @BeforeEach
    void init() {
        // 신분당선 생성
        신분당선_상행종점역_ID = createStation("신사");
        신분당선_하행종점역_ID = createStation("광교");
        신분당선_ID = createLine(신분당선, 10, 신분당선_상행종점역_ID, 신분당선_하행종점역_ID).as(LineResponse.class).getId();
    }

    @Nested
    @DisplayName("지하철 구간 등록")
    class CreateSection {
        @Test
        @DisplayName("새로운 구간 등록 성공")
        void createSection() {
            // When: 새로운 지하철역을 등록하고
            Long 신분당선_신규역_ID = createStation("신규역");

            // When: 노선의 하행종점역을 상행역으로 구간을 등록하면
            CreateSectionRequest createSectionRequest = new CreateSectionRequest(신분당선_하행종점역_ID, 신분당선_신규역_ID, 5);
            val postSectionResponse = post(String.format(BASE_PATH, 신분당선_ID), createSectionRequest);
            assertThat(postSectionResponse.statusCode()).isEqualTo(HttpStatus.SC_CREATED);

            // Then: 구간 목록 조회 시 생성한 구간을 찾을 수 있다
            ExtractableResponse<Response> getResponse = get(String.format(BASE_PATH, 신분당선_ID));
            assertThat(getResponse.jsonPath().getList("upStationId", Long.class)).containsAnyOf(신분당선_하행종점역_ID);
            assertThat(getResponse.jsonPath().getList("downStationId", Long.class)).containsAnyOf(신분당선_신규역_ID);
            assertThat(getResponse.jsonPath().getList("distance")).containsAnyOf(5);
        }

        @Nested
        @DisplayName("새로운 구간 등록 실패")
        class CreateSectionWithInvalidRequest {
            @Test
            @DisplayName("새로운 구간의 상행역이 해당 노선에 등록되어있는 하행 종점역이 아닐 때")
            void createSectionWithInvalidUpStation() {
                // When: 새로운 지하철역을 등록하고
                Long 신분당선_신규역_ID = createStation("신규역");

                // When: 해당 노선의 하행종점역이 아닌 역을 상행역으로 구간을 등록하면
                CreateSectionRequest invalidSectionRequest = new CreateSectionRequest(신분당선_상행종점역_ID, 신분당선_신규역_ID, 5);
                ExtractableResponse<Response> postResponse = post(String.format(BASE_PATH, 신분당선_ID), invalidSectionRequest);

                // Then: 구간 등록에 실패한다.
                assertThat(postResponse.statusCode()).isEqualTo(HttpStatus.SC_BAD_REQUEST);
            }

            @Test
            @DisplayName("이미 노선에 등록된 역을 새로운 구간의 하행역으로 등록할 때")
            void createSectionWithInvalidDownStation() {
                // When: 이미 노선에 등록된 역을 하행역으로 구간을 등록하면
                CreateSectionRequest invalidSectionRequest = new CreateSectionRequest(신분당선_하행종점역_ID, 신분당선_상행종점역_ID, 10);
                ExtractableResponse<Response> postResponse = post(String.format(BASE_PATH, 신분당선_ID), invalidSectionRequest);

                // Then: 구간 등록에 실패한다.
                assertThat(postResponse.statusCode()).isEqualTo(HttpStatus.SC_BAD_REQUEST);
            }

            @Test
            @DisplayName("존재하지 않는 노선에 구간을 등록할 때")
            void createSectionWithInvalidLine() {
                // When: 새로운 지하철역을 등록하고
                Long 신분당선_신규역_ID = createStation("신규역");

                // When: 존재하지 않는 노선에 구간을 등록하면
                CreateSectionRequest invalidSectionRequest = new CreateSectionRequest(신분당선_하행종점역_ID, 신분당선_신규역_ID, 10);
                ExtractableResponse<Response> postResponse = post(String.format(BASE_PATH, NON_EXISTENT_SUBWAY_LINE_ID), invalidSectionRequest);

                // Then: 구간 등록에 실패한다.
                assertThat(postResponse.statusCode()).isEqualTo(HttpStatus.SC_BAD_REQUEST);
            }
        }
    }

    @Nested
    @DisplayName("지하철 구간 삭제")
    class DeleteSection {
        @Test
        @DisplayName("구간 삭제 성공")
        void deleteLastSection() {
            // Given: 여러개의 구간을 등록하고
            Long 신분당선_신규역_ID = createStation("신규역");
            Long 신분당선_신규역2_ID = createStation("신규역2");
            Long 신분당선_신규역3_ID = createStation("신규역3");
            post(String.format(BASE_PATH, 신분당선_ID), new CreateSectionRequest(신분당선_하행종점역_ID, 신분당선_신규역_ID, 5));
            post(String.format(BASE_PATH, 신분당선_ID), new CreateSectionRequest(신분당선_신규역_ID, 신분당선_신규역2_ID, 5));
            post(String.format(BASE_PATH, 신분당선_ID), new CreateSectionRequest(신분당선_신규역2_ID, 신분당선_신규역3_ID, 5));

            // When: 마지막 구간을 삭제하면
            ExtractableResponse<Response> deleteSectionResponse = delete(String.format(DELETE_PATH, 신분당선_ID, 신분당선_신규역3_ID));

            // Then: 해당 구간 정보는 삭제된다
            assertThat(deleteSectionResponse.statusCode()).isEqualTo(HttpStatus.SC_NO_CONTENT);
        }

        @Nested
        @DisplayName("구간 삭제 실패")
        class DeleteSectionWithInvalidRequest {
            @Test
            @DisplayName("지하철 노선에 등록된 구간이 1개일 때")
            void deleteSectionWithSingleSection() {
                // When: 지하철 노선에 등록된 구간이 1개일 때 마지막 구간을 삭제하면
                ExtractableResponse<Response> deleteSectionResponse = delete(String.format(DELETE_PATH, 신분당선_ID, 신분당선_하행종점역_ID));

                // Then: 구간 삭제에 실패한다
                assertThat(deleteSectionResponse.statusCode()).isEqualTo(HttpStatus.SC_BAD_REQUEST);
            }

            @Test
            @DisplayName("마지막 구간이 아닌 구간을 삭제 할 때")
            void deleteNonLastSection() {
                // Given: 여러개의 구간을 등록하고
                Long 신분당선_신규역_ID = createStation("신규역");
                Long 신분당선_신규역2_ID = createStation("신규역2");
                post(String.format(BASE_PATH, 신분당선_ID), new CreateSectionRequest(신분당선_하행종점역_ID, 신분당선_신규역_ID, 5));
                post(String.format(BASE_PATH, 신분당선_ID), new CreateSectionRequest(신분당선_신규역_ID, 신분당선_신규역2_ID, 5));

                // When: 마지막 구간이 아닌 구간을 삭제하면
                ExtractableResponse<Response> deleteSectionResponse = delete(String.format(DELETE_PATH, 신분당선_ID, 신분당선_신규역_ID));

                // Then: 구간 삭제에 실패한다
                assertThat(deleteSectionResponse.statusCode()).isEqualTo(HttpStatus.SC_BAD_REQUEST);
            }

            @Test
            @DisplayName("등록되지 않은 구간을 삭제하려고 할 때")
            void deleteUnregisteredSection() {
                // Given: 여러개의 구간을 등록하고
                Long 신분당선_신규역_ID = createStation("신규역");
                Long 신분당선_신규역2_ID = createStation("신규역2");
                post(String.format(BASE_PATH, 신분당선_ID), new CreateSectionRequest(신분당선_하행종점역_ID, 신분당선_신규역_ID, 5));
                post(String.format(BASE_PATH, 신분당선_ID), new CreateSectionRequest(신분당선_신규역_ID, 신분당선_신규역2_ID, 5));

                // When: 등록되지 않은 구간을 삭제하면
                ExtractableResponse<Response> deleteSectionResponse = delete(String.format(DELETE_PATH, 신분당선_ID, createStation("신규역3")));

                // Then: 구간 삭제에 실패한다
                assertThat(deleteSectionResponse.statusCode()).isEqualTo(HttpStatus.SC_BAD_REQUEST);
            }
        }
    }

    private Long createStation(String stationName) {
        return post("/stations", StationRequest.from(stationName)).as(StationResponse.class).id();
    }

    private ExtractableResponse<Response> createLine(String lineName, int distance, Long upStationId, Long downStationId) {
        CreateLineRequest line = CreateLineRequest.builder()
                .name(lineName)
                .color(TEST_COLOR)
                .distance(distance)
                .upStationId(upStationId)
                .downStationId(downStationId)
                .build();
        return post("/lines", line);
    }
}
