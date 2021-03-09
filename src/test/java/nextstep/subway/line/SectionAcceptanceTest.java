package nextstep.subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
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


  private long 광교역;
  private long 광교중앙역;
  private long lineId;
  private ExtractableResponse<Response> createLineResponse;

  private void 지하철_노선_생성됨(){
    광교역 = 지하철역_생성_요청("광교역").body().jsonPath().getLong("id");
    광교중앙역 = 지하철역_생성_요청("광교중앙역").body().jsonPath().getLong("id");
    createLineResponse = 지하철_노선_생성요청("신분당선",LineColor.RED,광교역,광교중앙역);
    lineId = createLineResponse.body().jsonPath().getLong("id");
  }

  private void 노선_구간_등록됨(ExtractableResponse<Response> response){
    assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
  }
  private void 노선_구간_등록실패됨(ExtractableResponse<Response> response){
    assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
  }

  private void 노선_구간_삭제됨(ExtractableResponse<Response> response){
    assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
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

  private ExtractableResponse<Response> 구간삭제요청(long downStationId){
    return RestAssured.given().log().all()
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when()
        .delete("/lines/{lineId}/sections?stationId={downStationId}",lineId,downStationId)
        .then().log().all()
        .extract();
  }


  @DisplayName("지하철 노선에 구간을 등록한다")
  @Test
  void createSection(){
    //given 지하철 노선 생성됨
    지하철_노선_생성됨();

    //when 지하철 노선에 구간등록 요청
    ExtractableResponse<Response> sectionResponse =  구간등록요청(광교역,광교중앙역,30);
    long 상현역 =  지하철역_생성_요청("상현역").body().jsonPath().getLong("id");
    long 성복역 =  지하철역_생성_요청("성복역").body().jsonPath().getLong("id");
    구간등록요청(광교중앙역,상현역,30);
    구간등록요청(상현역,성복역,30);

    //then 지하철 노선에 구간등록 완료됨
    노선_구간_등록됨(sectionResponse);
  }

  @DisplayName("신규구간의 상행역이 노선의 종점역이 아닌 구간을 등록한다")
  @Test
  void createSectionWithoutEndSection() {
    //given 지하철 노선 생성됨
    지하철_노선_생성됨();
    long 동천역 =  지하철역_생성_요청("동천역").body().jsonPath().getLong("id");
    구간등록요청(광교역,광교중앙역,30);

    //when 지하철 노선에 구간등록 요청
    ExtractableResponse<Response> sectionResponse =  구간등록요청(동천역,광교중앙역,30);

    //then 지하철 노선에 구간등록 완료됨
    노선_구간_등록실패됨(sectionResponse);
  }


  @DisplayName("신규구간의 상행역이 노선의 종점역이 아닌 구간을 등록한다")
  @Test
  void createSectionWithDuplicatedSection() {
    //given 지하철 노선 생성됨
    지하철_노선_생성됨();
    long 상현역 =  지하철역_생성_요청("상현역").body().jsonPath().getLong("id");
    구간등록요청(광교역,광교중앙역,30);
    구간등록요청(광교중앙역,상현역,30);

    //when 지하철 노선에 구간등록 요청
    ExtractableResponse<Response> sectionResponse =  구간등록요청(상현역,광교중앙역,30);

    //then 지하철 노선에 구간등록 완료됨
    노선_구간_등록실패됨(sectionResponse);
  }


  @DisplayName("노선의 구간을 삭제한다")
  @Test
  void deleteSection(){

    //given 지하철 노선 생성됨
    지하철_노선_생성됨();
    ExtractableResponse<Response> sectionResponse =  구간등록요청(광교역,광교중앙역,30);
    long 상현역 =  지하철역_생성_요청("상현역").body().jsonPath().getLong("id");
    long 성복역 =  지하철역_생성_요청("성복역").body().jsonPath().getLong("id");
    구간등록요청(광교중앙역,상현역,30);
    구간등록요청(상현역,성복역,30);

    //when 노선의 구간 삭제요청함
    ExtractableResponse<Response> removeResponse =  구간삭제요청(성복역);


    //then 노선의 구간 삭제됨
    노선_구간_삭제됨(removeResponse);
  }


}
