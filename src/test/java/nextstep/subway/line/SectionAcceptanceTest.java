package nextstep.subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.line.LineRequestBuilder.*;
import static nextstep.subway.station.StationRequestBuilder.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선구간 관련 기능")
public class SectionAcceptanceTest extends AcceptanceTest {


  private long startStationId;
  private long endStationId;
  private long lineId;
  private ExtractableResponse<Response> createLineResponse;

  private void 지하철_노선_생성됨(){
    startStationId = 지하철역_생성_요청("광교역").body().jsonPath().getLong("id");
    endStationId = 지하철역_생성_요청("광교중앙역").body().jsonPath().getLong("id");
    createLineResponse = 지하철_노선_생성요청("신분당선",LineColor.RED,startStationId,endStationId);
    lineId = createLineResponse.body().jsonPath().getLong("id");
  }

  private void 노선_구간_등록됨(ExtractableResponse<Response> response){
    assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
  }
  private void 노선_구간_실패됨(ExtractableResponse<Response> response){
    assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
  }

  private ExtractableResponse<Response> 구간등록요청(long upStationId, long downStationId,int distance){

    Map params = new HashMap<>();
    params.put("upStationId",upStationId);
    params.put("downStationId",downStationId);
    params.put("distance",distance);

    return RestAssured.given().log().all()
        .body(params)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when()
        .post("/lines/{lineId}/sections",lineId)
        .then().log().all()
        .extract();
  }

  @DisplayName("지하철 노선에 구간을 등록한다")
  @Test
  void createSection(){
    //given 지하철 노선 생성됨
    지하철_노선_생성됨();

    //when 지하철 노선에 구간등록 요청
    ExtractableResponse<Response> sectionResponse =  구간등록요청(startStationId,endStationId,30);

    //then 지하철 노선에 구간등록 완료됨
    노선_구간_등록됨(sectionResponse);
  }

  @DisplayName("신규구간의 상행역이 노선의 종점역이 아닌 구간을 등록한다")
  @Test
  void createSectionWithoutEndSection() {
    //given 지하철 노선 생성됨
    지하철_노선_생성됨();
    long stationId =  지하철역_생성_요청("동천역").body().jsonPath().getLong("id");
    구간등록요청(startStationId,endStationId,30);

    //when 지하철 노선에 구간등록 요청
    ExtractableResponse<Response> sectionResponse =  구간등록요청(stationId,endStationId,30);

    //then 지하철 노선에 구간등록 완료됨
    노선_구간_실패됨(sectionResponse);
  }




}
