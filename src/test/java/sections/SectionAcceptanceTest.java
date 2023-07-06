package sections;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import line.LineFixture;
import subway.SchemaInitSql;
import subway.StationFixture;
import subway.SubwayApplication;
import subway.line.view.LineResponse;
import subway.section.model.SectionCreateRequest;
import subway.station.view.StationResponse;
import subway.support.ErrorCode;
import subway.support.ErrorResponse;

@SchemaInitSql
@DisplayName("지하철 구간 관련 기능")
@SpringBootTest(classes = SubwayApplication.class, webEnvironment = WebEnvironment.DEFINED_PORT)
public class SectionAcceptanceTest {
    LineFixture lineFixture = new LineFixture();
    StationFixture stationFixture = new StationFixture();

    @DisplayName("새로운 구간의 상행역이 노선의 하행 종점역이 아닐 때")
    @Nested
    class Given_section_upstation_is_not_lines_downstation {

        @DisplayName("구간을 등록하면")
        @Nested
        class When_create_section {

            @DisplayName("오류가 발생한다")
            @Test
            void shouldThrowError() {
                StationResponse lineUpstationA = stationFixture.지하철역_생성("upstationA");
                StationResponse lineDownstationB = stationFixture.지하철역_생성("downStationB");
                LineResponse lineAB = lineFixture.노선생성("line-ab", "yellow", lineUpstationA.getId(), lineDownstationB.getId(), 10);

                StationResponse lineUpstationC = stationFixture.지하철역_생성("upstationC");
                StationResponse lineDownstationD = stationFixture.지하철역_생성("downStationD");
                LineResponse lineCD = lineFixture.노선생성("line-cd", "blue", lineUpstationC.getId(), lineDownstationD.getId(), 5);


                SectionCreateRequest request = new SectionCreateRequest();
                request.setUpStationId(lineUpstationA.getId());
                request.setDownStationId(lineUpstationC.getId());
                request.setDistance(3);

                ExtractableResponse<Response> response = RestAssured.given().log().all()
                                                                    .body(request).contentType(MediaType.APPLICATION_JSON_VALUE)
                                                                    .when().post(getCreateSectionUrl(lineAB.getId()))
                                                                    .then().log().all()
                                                                    .extract();

                assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
                assertThat(response.as(ErrorResponse.class).getErrorCode()).isEqualTo(ErrorCode.SECTION_CREATE_FAIL_BY_UPSTATION);
            }
        }
    }

    private String getCreateSectionUrl(Long lineId) {
        return "/lines/" + lineId + "/sections";
    }

    @DisplayName("given_새로운 구간의 하행역이 노선에 존재할때")
    @Nested
    class Given_section_downstation_on_same_line {

        @DisplayName("when_구간을 등록하면")
        @Nested
        class When_create_section {

            @DisplayName("then_오류가 발생한다")
            @Test
            void shouldThrowError() {
                StationResponse lineUpstationA = stationFixture.지하철역_생성("upstationA");
                StationResponse lineDownstationB = stationFixture.지하철역_생성("downStationB");
                LineResponse lineAB = lineFixture.노선생성("line-ab", "yellow", lineUpstationA.getId(), lineDownstationB.getId(), 10);

                StationResponse lineUpstationC = stationFixture.지하철역_생성("upstationC");
                StationResponse lineDownstationD = stationFixture.지하철역_생성("downStationD");
                LineResponse lineCD = lineFixture.노선생성("line-cd", "blue", lineUpstationC.getId(), lineDownstationD.getId(), 5);


                SectionCreateRequest request = new SectionCreateRequest();
                request.setUpStationId(lineDownstationB.getId());
                request.setDownStationId(lineUpstationA.getId());
                request.setDistance(3);

                ExtractableResponse<Response> response = RestAssured.given().log().all()
                                                                    .body(request).contentType(MediaType.APPLICATION_JSON_VALUE)
                                                                    .when().post(getCreateSectionUrl(lineAB.getId()))
                                                                    .then().log().all()
                                                                    .extract();

                assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
                assertThat(response.as(ErrorResponse.class).getErrorCode()).isEqualTo(ErrorCode.SECTION_CREATE_FAIL_BY_DOWNSTATION);
            }
        }
    }

    @Nested
    class Given_2개의_노선이_있을때 {

        @DisplayName("given_상행역은 노선의 하행으로, 하행역은 다른 노선의 역으로 설정하면")
        @Nested
        class When_구간을_등록하면 {


            @DisplayName("then_구간을 등록할 수 있다")
            @Test
            void registerSection() {
                StationResponse lineUpstationA = stationFixture.지하철역_생성("upstationA");
                StationResponse lineDownstationB = stationFixture.지하철역_생성("downStationB");
                LineResponse lineAB = lineFixture.노선생성("line-ab", "yellow", lineUpstationA.getId(), lineDownstationB.getId(), 10);

                StationResponse lineUpstationC = stationFixture.지하철역_생성("upstationC");
                StationResponse lineDownstationD = stationFixture.지하철역_생성("downStationD");
                LineResponse lineCD = lineFixture.노선생성("line-cd", "blue", lineUpstationC.getId(), lineDownstationD.getId(), 5);


                SectionCreateRequest request = new SectionCreateRequest();
                request.setUpStationId(lineDownstationB.getId());
                request.setDownStationId(lineUpstationC.getId());
                request.setDistance(4);

                ExtractableResponse<Response> response = RestAssured.given().log().all()
                                                                    .body(request).contentType(MediaType.APPLICATION_JSON_VALUE)
                                                                    .when().post(getCreateSectionUrl(lineAB.getId()))
                                                                    .then().log().all()
                                                                    .extract();

                assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
            }
        }
    }
}
