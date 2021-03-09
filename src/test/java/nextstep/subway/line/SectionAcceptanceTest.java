package nextstep.subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.line.LineRequestBuilder.*;
import static nextstep.subway.station.StationRequestBuilder.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선구간 관련 기능")
public class SectionAcceptanceTest {


  private long startStationId;
  private long endStationId;
  private long lineId;
  private ExtractableResponse<Response> createLineResponse;

  private void 지하철_노선_생성됨(){
    startStationId = 지하철역_생성_요청("광교역").body().jsonPath().getLong("id");
    endStationId = 지하철역_생성_요청("강남역").body().jsonPath().getLong("id");
    createLineResponse = 지하철_노선_생성요청("신분당선",LineColor.RED,startStationId,endStationId);
    lineId = createLineResponse.body().jsonPath().getLong("id");
  }

  private void 노선_구간_등록됨(ExtractableResponse<Response> response){
    assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
  }

  private ExtractableResponse<Response> 구간등록요청(int distance){

    Map params = new HashMap<>();
    params.put("upStationId",startStationId);
    params.put("downStationId",endStationId);
    params.put("distance",distance);

    return RestAssured.given().log().all()
        .body(params)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when()
        .post(MessageFormat.format("/lines/{1}/sections",lineId))
        .then().log().all()
        .extract();
  }

  @DisplayName("지하철 노선에 구간을 등록한다")
  @Test
  void createSection(){
    //given 지하철 노선 생성됨
    지하철_노선_생성됨();

    //when 지하철 노선에 구간등록 요청
    ExtractableResponse<Response> sectionResponse =  구간등록요청(30);

    //then 지하철 노선에 구간등록 완료됨
    노선_구간_등록됨(sectionResponse);
  }

}
