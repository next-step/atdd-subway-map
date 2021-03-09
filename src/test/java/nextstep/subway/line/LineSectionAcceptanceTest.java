package nextstep.subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.utils.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.*;
import java.util.stream.Collectors;

import static nextstep.subway.line.LineSectionSteps.*;
import static nextstep.subway.line.LineSteps.지하철_노선_생성_요청;
import static nextstep.subway.station.StationSteps.지하철_역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("지하철 구간 관련 테스트")
public class LineSectionAcceptanceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private DatabaseCleanup databaseCleanup;

    private StationResponse 강남역, 양재역, 양재시민의숲, 광교역;
    private LineResponse 신분당선;
    Map<String, String> lineParams;

    @BeforeEach
    void setUp(){
        RestAssured.port = port; // 랜덤포트 테스트시 각 RestAssured에 포트를 동일하게 하기위한 세팅
        databaseCleanup.execute(); // 각 테스트간의 격리를 위한 레포 초기화

        // given
        강남역 = 지하철_역_생성_요청("강남역").as(StationResponse.class);
        양재역 = 지하철_역_생성_요청("양재역").as(StationResponse.class);
        양재시민의숲 = 지하철_역_생성_요청("양재시민의숲").as(StationResponse.class);
        광교역 = 지하철_역_생성_요청("광교역").as(StationResponse.class);

        lineParams = new HashMap<>();
        lineParams.put("name", "신분당선");
        lineParams.put("color", "bg-red-600");
        lineParams.put("upStationId", 강남역.getId() + "");
        lineParams.put("downStationId", 양재역.getId() + "");
        lineParams.put("distance", "10");
        신분당선 = 지하철_노선_생성_요청(lineParams).as(LineResponse.class);
    }

    Map<String, String> createParams(StationResponse upStation, StationResponse downStation){
        Map<String, String> params = new HashMap<>();
        params.put("downStationId", upStation.getId() + "");
        params.put("upStationId", downStation.getId() + "");
        params.put("distance","10");
        return params;
    }

    String createUri(LineResponse line){
        return "/lines/" + line.getId() + "sections";
    }

    @DisplayName("지하철 노선에 구간을 등록한다.")
    @Test
    void createLineSection(){
        // given
        지하철_노선에_구간_등록_요청(createParams(강남역, 양재역), createUri(신분당선));

        //when
        ExtractableResponse<Response> response = 지하철_노선에_구간_등록_요청(createParams(양재역, 양재시민의숲), createUri(신분당선));

        // then
        지하철_노선에_구간_생성됨(response);
        지하철_노선_순서대로_정렬됨(response, Arrays.asList(강남역.getId(), 양재역.getId(), 양재시민의숲.getId()));
    }

    @DisplayName("하행 종점역이 아닌 상행역을 등록한다.")
    @Test
    void createLineSectionWithUpStation(){
        // given
        지하철_노선에_구간_등록_요청(createParams(강남역, 양재역), createUri(신분당선));

        // when
        ExtractableResponse<Response> response = 지하철_노선에_구간_등록_요청(createParams(광교역, 양재시민의숲), createUri(신분당선));

        // then
        지하철_노선에_구간_생성_실패됨(response);
    }

    @DisplayName("이미 등록된 역을 하행역으로 등록한다.")
    @Test
    void createLineSectionWithDownStation(){
        // given
        지하철_노선에_구간_등록_요청(createParams(강남역, 양재역), createUri(신분당선));
        지하철_노선에_구간_등록_요청(createParams(양재역, 양재시민의숲), createUri(신분당선));

        // when
        ExtractableResponse<Response> response = 지하철_노선에_구간_등록_요청(createParams(양재시민의숲, 강남역), createUri(신분당선));

        // then
        지하철_노선에_구간_생성_실패됨(response);
    }
}
