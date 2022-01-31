package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.requests.LineRequests.specificLineReadRequest;
import static nextstep.subway.acceptance.requests.SectionRequests.*;
import static nextstep.subway.acceptance.requests.StationRequest.stationCreateRequest;
import static nextstep.subway.acceptance.type.GeneralNameType.*;
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
        Long upStationId = stationCreateRequest(강남역.stationName()).jsonPath().getLong(ID.getType());
        Long downStationId =
                stationCreateRequest(역삼역.stationName()).jsonPath().getLong(ID.getType());
        Long newDownStationId =
                stationCreateRequest(선릉역.stationName()).jsonPath().getLong(ID.getType());

        Long lineId =
                lineCreateRequest(
                                NEW_BUN_DANG_LINE.lineName(),
                                NEW_BUN_DANG_LINE.lineColor(),
                                upStationId,
                                downStationId,
                                FIRST_DISTANCE)
                        .jsonPath()
                        .getLong(ID.getType());

        // when
        ExtractableResponse<Response> newSectionResponse =
                sectionAddRequest(lineId, downStationId, newDownStationId, 3);

        // then
        assertThat(newSectionResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("노선 구간 제거")
    @Test
    void deleteStationSectionTest() {
        // given
        Long upStationId = stationCreateRequest(강남역.stationName()).jsonPath().getLong(ID.getType());
        Long downStationId =
                stationCreateRequest(역삼역.stationName()).jsonPath().getLong(ID.getType());
        Long newDownStationId =
                stationCreateRequest(선릉역.stationName()).jsonPath().getLong(ID.getType());

        Long lineId =
                lineCreateRequest(
                                NEW_BUN_DANG_LINE.lineName(),
                                NEW_BUN_DANG_LINE.lineColor(),
                                upStationId,
                                downStationId,
                                FIRST_DISTANCE)
                        .jsonPath()
                        .getLong(ID.getType());

        sectionAddRequest(lineId, downStationId, newDownStationId, 3);

        // when
        ExtractableResponse<Response> deleteSectionResponse =
                sectionDeleteRequest(lineId, newDownStationId);

        // then
        assertThat(deleteSectionResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("노선 구간 조회")
    @Test
    void listSectionsFromQueryToLineTest() {
        Long upStationId = stationCreateRequest(강남역.stationName()).jsonPath().getLong(ID.getType());
        Long downStationId =
                stationCreateRequest(역삼역.stationName()).jsonPath().getLong(ID.getType());

        Long lineId =
                lineCreateRequest(
                                NEW_BUN_DANG_LINE.lineName(),
                                NEW_BUN_DANG_LINE.lineColor(),
                                upStationId,
                                downStationId,
                                FIRST_DISTANCE)
                        .jsonPath()
                        .getLong(ID.getType());

        ExtractableResponse<Response> queryResponse = specificLineReadRequest(lineId);

        assertThat(queryResponse.jsonPath().getList("stations.name", String.class))
                .containsExactly(강남역.stationName(), 역삼역.stationName());
    }
}
