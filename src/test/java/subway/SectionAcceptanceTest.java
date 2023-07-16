package subway;

import static subway.factory.SubwayNameFactory.강남역;
import static subway.factory.SubwayNameFactory.광교역;
import static subway.factory.SubwayNameFactory.논현역;
import static subway.factory.SubwayNameFactory.신논현역;
import static subway.factory.SubwayNameFactory.신분당선;
import static subway.factory.SubwayNameFactory.양재역;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import subway.dto.LineResponse;
import subway.dto.SectionRequest;
import subway.dto.SectionResponse;
import subway.dto.StationRequest;
import subway.exception.NoMatchUpStationException;
import subway.exception.error.SectionErrorCode;
import subway.factory.LineRequestFactory;
import subway.utils.RestAssuredClient;

@DisplayName("구간 관련 기능")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SectionAcceptanceTest {

    private final String linePath = "/lines";

    private LineResponse lineResponse;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        saveStations();
        saveLine();
    }

    /**
     * Given: 2개의 지하철역이 등록되어 있다.
     * And: 1개의 노선이 등록되어 있다.
     * When: 구간을 생성한다.
     * Then: 성공(200 OK) 응답을 받는다.
     * And: 새롭게 등록된 지하철 구간 정보를 응답받는다.
     */
    @Test
    @DisplayName("새로운 구간을 등록한다.")
    void createSection() {
        // Given (Fixture)
        // When
        SectionRequest sectionRequest = SectionRequest.builder()
            .upStationId(2L)
            .downStationId(3L)
            .distance(20L)
            .build();
        ExtractableResponse<Response> response = RestAssuredClient.requestPost(
            generateSectionPathForLineId(lineResponse.getId()),
            sectionRequest).extract();

        // Then
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        SectionResponse sectionResponse = response.as(SectionResponse.class);
        Assertions.assertThat(sectionResponse.getId()).isNotNull();
        Assertions.assertThat(sectionResponse.getLineId()).isEqualTo(this.lineResponse.getId());
    }

    /**
     * Given: 2개의 지하철역이 등록되어 있다.
     * And: 1개의 노선이 등록되어 있다.
     * When: 상행역이 노선의 하행 종점역이 아닌 구간을 등록한다.
     * Then: 실패(400 Bad Request) 응답을 받는다.
     * And: '구간의 상행역과 노선의 하행 종점역이 일치하지 않습니다.' 메시지를 응답받는다.
     */
    @Test
    @DisplayName("상행역이 노선의 하행 종점역이 아닌 구간을 등록한다.")
    void createSectionWithNoMatchesUpStation() {
        // Given (Fixture)
        // When
        SectionRequest sectionRequest = SectionRequest.builder()
            // 노선은 상행(1) 하행(2) 으로 종점이 등록되어있는 상태이며,
            // 새로운 구간은 상행(3) 으로 등록하고자 함
            .upStationId(3L)
            .downStationId(4L)
            .distance(20L)
            .build();

        // Then
        Assertions.assertThatThrownBy(() -> {
            RestAssuredClient.requestPost(
                generateSectionPathForLineId(lineResponse.getId()),
                sectionRequest)
                .statusCode(HttpStatus.BAD_REQUEST.value());
        })
            .isInstanceOf(NoMatchUpStationException.class)
            .hasMessage(SectionErrorCode.NO_MATCH_UP_STATION.getMessage());
    }

    /**
     * Given: 2개의 지하철역이 등록되어 있다.
     * And: 1개의 노선이 등록되어 있다.
     * When: 하행역이 노선에 등록되어있는 구간을 등록한다.
     * Then: 실패(400 Bad Request) 응답을 받는다.
     * And: '구간의 하행역이 노선에 이미 등록되어있습니다.' 메시지를 응답받는다.
     */
    @Test
    @DisplayName("하행역이 노선에 등록되어 있는 구간을 등록한다.")
    void createSectionWithAlreadyExistDownStation() {
        // Given (Fixture)
        // When
        SectionRequest sectionRequest = SectionRequest.builder()
            // 노선은 상행(1) 하행(2) 으로 종점이 등록되어있는 상태이며,
            // 새로운 구간의 하행(1) 으로 등록하고자 함
            .upStationId(2L)
            .downStationId(1L)
            .distance(20L)
            .build();

        // Then
        Assertions.assertThatThrownBy(() -> {
                RestAssuredClient.requestPost(
                        generateSectionPathForLineId(lineResponse.getId()),
                        sectionRequest)
                    .statusCode(HttpStatus.BAD_REQUEST.value());
            })
            .isInstanceOf(NoMatchUpStationException.class)
            .hasMessage(SectionErrorCode.ALREADY_EXIST_DOWN_STATION.getMessage());
    }

    private String generateSectionPathForLineId(long id) {
        return new StringBuilder()
            .append(linePath)
            .append("/")
            .append(id)
            .append("sections")
            .toString();
    }

    private void saveStations() {
        String stationPath = "/stations";
        RestAssuredClient.requestPost(stationPath, StationRequest.builder().name(강남역).build());
        RestAssuredClient.requestPost(stationPath, StationRequest.builder().name(광교역).build());
        RestAssuredClient.requestPost(stationPath, StationRequest.builder().name(양재역).build());
        RestAssuredClient.requestPost(stationPath, StationRequest.builder().name(논현역).build());
        RestAssuredClient.requestPost(stationPath, StationRequest.builder().name(신논현역).build());
    }

    private void saveLine() {
        this.lineResponse = RestAssuredClient.requestPost("/lines",
            LineRequestFactory.create(신분당선)).extract().as(LineResponse.class);
    }
}
