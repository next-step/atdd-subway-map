package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.application.dto.*;
import nextstep.subway.steps.LineSteps;
import nextstep.subway.steps.SectionSteps;
import nextstep.subway.steps.StationSteps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("구간 관리 기능")
public class SectionAcceptanceTest extends AcceptanceTest {
    private static final Long INVALID_ID = -1L;
    private SectionRequestBuilder defaultSectionRequestBuilder;
    private Long lineId;
    private Long firstUpStationId;
    private Long downStationId;
    private Long extendedStationId;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        ExtractableResponse<Response> upStationResponse = StationSteps.executeStationCreateRequest("강남역");
        Long upStationId = upStationResponse.jsonPath().getLong("id");
        firstUpStationId = upStationId;

        ExtractableResponse<Response> downStationResponse = StationSteps.executeStationCreateRequest("판교역");
        downStationId = downStationResponse.jsonPath().getLong("id");

        ExtractableResponse<Response> extendedStationResponse = StationSteps.executeStationCreateRequest("광교역");
        extendedStationId = extendedStationResponse.jsonPath().getLong("id");

        LineRequestBuilder lineRequestBuilder = LineRequestBuilder.ofDefault()
                .withUpStationId(upStationId)
                .withDownStationId(downStationId);
        ExtractableResponse<Response> lineResponse = LineSteps.executeLineCreateRequest(lineRequestBuilder.build());
        lineId = lineResponse.jsonPath().getLong("id");

        defaultSectionRequestBuilder = SectionRequestBuilder.ofDefault()
                .withUpStationId(downStationId)
                .withDownStationId(extendedStationId);
    }

    /**
     * Given 구간을 등록하려는 노선이 존재하지 않을 때
     * When 구간 등록 요청을 하면
     * Then 구간 등록이 실패한다.
     */
    @DisplayName("구간을 등록하려는 노선이 존재하지 않을 때")
    @Test
    void createNotExistsLineSection() {
        SectionRequest request = SectionRequestBuilder.ofDefault().build();
        ExtractableResponse<Response> response = SectionSteps.executeSectionCreateRequest(INVALID_ID, request);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.jsonPath().getString("message")).contains("Not found line");
    }

    /**
     * Given 상행역이 존재하지 않을 때
     * When 구간 등록 요청을 하면
     * Then 구간 등록이 실패한다.
     */
    @DisplayName("상행역이 존재하지 않을 때")
    @Test
    void createNotExistsUpStationSection() {
        SectionRequest request = defaultSectionRequestBuilder.withUpStationId(INVALID_ID).build();
        ExtractableResponse<Response> response = SectionSteps.executeSectionCreateRequest(lineId, request);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.jsonPath().getString("message")).contains("Not found station");
    }

    /**
     * Given 하행역이 존재하지 않을 때
     * When 구간 등록 요청을 하면
     * Then 구간 등록이 실패한다.
     */
    @DisplayName("하행역이 존재하지 않을 때")
    @Test
    void createNotExistsDownStationSection() {
        SectionRequest request = defaultSectionRequestBuilder.withDownStationId(INVALID_ID).build();
        ExtractableResponse<Response> response = SectionSteps.executeSectionCreateRequest(lineId, request);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.jsonPath().getString("message")).contains("Not found station");
    }

    /**
     * Given 새로운 구간의 상행역이 노선의 하행 종점역이 아닐 때
     * When 구간 등록 요청을 하면
     * Then 구간 등록이 실패한다.
     */
    @DisplayName("상행역이 노선의 하행 종점역이 아닐 때")
    @Test
    void createNotConsistsUpStationSection() {
        ExtractableResponse<Response> otherStationResponse = StationSteps.executeStationCreateRequest("정자역");
        Long otherStationId = otherStationResponse.jsonPath().getLong("id");

        SectionRequest request = defaultSectionRequestBuilder.withUpStationId(otherStationId).build();
        ExtractableResponse<Response> response = SectionSteps.executeSectionCreateRequest(lineId, request);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.jsonPath().getString("message")).contains("Illegal Section");
    }

    /**
     * Given 새로운 구간의 하행역이 노선에 이미 등록되어 있을 때
     * When 구간 등록 요청을 하면
     * Then 구간 등록이 실패한다.
     */
    @DisplayName("하행역이 노선에 이미 등록되어 있을 때")
    @Test
    void createAlreadyConsistsDownStationSection() {
        SectionRequest request = defaultSectionRequestBuilder.withDownStationId(firstUpStationId).build();
        ExtractableResponse<Response> response = SectionSteps.executeSectionCreateRequest(lineId, request);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.jsonPath().getString("message")).contains("Illegal Section");
    }

    /**
     * When 구간 등록 요청을 하면
     * Then 구간 등록이 성공한다.
     */
    @DisplayName("구간 등록")
    @Test
    void createSection() {
        SectionRequest request = defaultSectionRequestBuilder.build();
        ExtractableResponse<Response> response = SectionSteps.executeSectionCreateRequest(lineId, request);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * Given 하행 종점역이 아닌 역에 대해
     * When 구간 삭제 요청을 하면
     * Then 구간 삭제가 실패한다.
     */
    @DisplayName("하행 종점역이 아닌 역에 대한 구간 삭제 요청")
    @Test
    void deleteNotDownStationSection() {
        ExtractableResponse<Response> response = SectionSteps.executeSectionDeleteRequest(lineId, firstUpStationId);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.jsonPath().getString("message")).contains("Illegal Station");
    }

    /**
     * Given 구간이 하나인 노선이 있을 때
     * When 해당 구간 삭제 요청을 하면
     * Then 구간 삭제가 실패한다.
     */
    @DisplayName("구간이 하나인 노선이 있을 때 구간 삭제 요청")
    @Test
    void deleteOnlyOneSection() {
        ExtractableResponse<Response> response = SectionSteps.executeSectionDeleteRequest(lineId, downStationId);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.jsonPath().getString("message")).contains("Violation Line policy");
    }

    /**
     * When 구간 삭제 요청을 하면
     * Then 구간 삭제가 성공한다.
     */
    @DisplayName("구간 삭제")
    @Test
    void deleteSection() {
        SectionRequest request = defaultSectionRequestBuilder.build();
        SectionSteps.executeSectionCreateRequest(lineId, request);

        ExtractableResponse<Response> response = SectionSteps.executeSectionDeleteRequest(lineId, extendedStationId);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}

