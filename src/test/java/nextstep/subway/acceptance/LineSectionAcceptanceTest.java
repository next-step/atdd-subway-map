package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.requests.LineRequests.LOCATION;
import static nextstep.subway.acceptance.requests.LineRequests.specificLineReadRequest;
import static nextstep.subway.acceptance.requests.SectionRequests.*;
import static nextstep.subway.acceptance.requests.StationRequest.stationCreateRequest;
import static nextstep.subway.acceptance.type.LineNameType.*;
import static nextstep.subway.acceptance.type.StationNameType.*;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class LineSectionAcceptanceTest extends AcceptanceTest {

    private static final int FIRST_DISTANCE = 2;

    @DisplayName("노선에 구간 추가")
    @Test
    void addNewStationSectionTest() {
        // given
        Long upStationId = stationCreateRequest(강남역.stationName()).jsonPath().getLong("id");
        Long downStationId = stationCreateRequest(역삼역.stationName()).jsonPath().getLong("id");
        Long newDownStationId = stationCreateRequest(선릉역.stationName()).jsonPath().getLong("id");

        String uri =
                lineCreateRequest(
                                NEW_BUN_DANG_LINE.lineName(),
                                NEW_BUN_DANG_LINE.lineColor(),
                                upStationId,
                                downStationId,
                                FIRST_DISTANCE)
                        .header(LOCATION);

        // when
        ExtractableResponse<Response> newSectionResponse =
                sectionAddRequest(uri, downStationId, newDownStationId, 3);

        // then
        assertThat(newSectionResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("노선 구간 제거")
    @Test
    void deleteStationSectionTest() {
        // given
        Long upStationId = stationCreateRequest(강남역.stationName()).jsonPath().getLong("id");
        Long downStationId = stationCreateRequest(역삼역.stationName()).jsonPath().getLong("id");
        Long newDownStationId = stationCreateRequest(선릉역.stationName()).jsonPath().getLong("id");

        ExtractableResponse<Response> response =
                lineCreateRequest(
                        NEW_BUN_DANG_LINE.lineName(),
                        NEW_BUN_DANG_LINE.lineColor(),
                        upStationId,
                        downStationId,
                        FIRST_DISTANCE);

        String uri =
                lineCreateRequest(
                                NEW_BUN_DANG_LINE.lineName(),
                                NEW_BUN_DANG_LINE.lineColor(),
                                upStationId,
                                downStationId,
                                FIRST_DISTANCE)
                        .header(LOCATION);

        ExtractableResponse<Response> newSectionResponse =
                sectionAddRequest(uri, downStationId, newDownStationId, 3);

        // when
        ExtractableResponse<Response> deleteSectionResponse =
                sectionDeleteRequest(uri, newDownStationId);

        // then
        assertThat(deleteSectionResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("노선 구간 조회")
    @Test
    void listSectionsFromQueryToLineTest() {
        Long upStationId = stationCreateRequest(강남역.stationName()).jsonPath().getLong("id");
        Long downStationId = stationCreateRequest(역삼역.stationName()).jsonPath().getLong("id");

        ExtractableResponse<Response> response =
                lineCreateRequest(
                        NEW_BUN_DANG_LINE.lineName(),
                        NEW_BUN_DANG_LINE.lineColor(),
                        upStationId,
                        downStationId,
                        FIRST_DISTANCE);

        String lineUrl = response.header(LOCATION);

        ExtractableResponse<Response> queryResponse = specificLineReadRequest(lineUrl);

        assertThat(queryResponse.jsonPath().getList("stations.name", String.class))
                .containsExactly(강남역.stationName(), 역삼역.stationName());
    }
}
