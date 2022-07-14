package nextstep.subway.test.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.test.acceptance.LineTestMethod.*;
import static nextstep.subway.test.acceptance.StationTestMethod.*;
import static nextstep.subway.test.acceptance.SectionTestMethod.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

/**
 * @author a1101466 on 2022/07/10
 * @project subway
 * @description
 */
public class SectionAcceptanceTest extends AcceptanceTest{

    public Long 파랑선_시작ID;
    public Long 파랑선_종착ID;
    public Long 초록선_시작ID;
    public Long 초록선_종착ID;

    public Long 초록선_라인ID;

    public Long 새로운구간_종착ID;
    @BeforeEach
    public void lineTestSetUp() {
        파랑선_시작ID = 지하철역_생성("구일역").jsonPath().getLong("id");
        파랑선_종착ID = 지하철역_생성("구로역").jsonPath().getLong("id");;
        초록선_시작ID = 지하철역_생성("신도림역").jsonPath().getLong("id");;
        초록선_종착ID = 지하철역_생성("문래역").jsonPath().getLong("id");;

        지하철노선_생성("파랑선", 파랑선_시작ID, 파랑선_종착ID, "blue", DEFAULT_DISTANCE);
        초록선_라인ID = 지하철노선_생성("초록선", 초록선_시작ID, 초록선_종착ID, "green", DEFAULT_DISTANCE)
                .jsonPath().getLong("id");

        새로운구간_종착ID = 지하철역_생성("합정역").jsonPath().getLong("id");

        구간_생성(초록선_종착ID, 새로운구간_종착ID, 초록선_라인ID);
    }
    /**
     * given 초록선 노선을 생성한다.
     * when 초록선 노선에 구간을 생성한다.
     * then 등록한 노선의 구간에 지하철역이 모두 조회 가능하다.
     */
    @Test
    @DisplayName("지하철구간을 생성한다")
    void createSection(){

        Long 구간생성테스트_종착역ID = 지하철역_생성("홍대입구역").jsonPath().getLong("id");
        구간_생성(새로운구간_종착ID, 구간생성테스트_종착역ID, 초록선_라인ID);

        assertAll(
                () -> assertThat(지하철노선_단일조회(초록선_라인ID).jsonPath().getLong("downStationId")).isEqualTo(구간생성테스트_종착역ID),
                () -> assertThat(지하철노선_단일조회(초록선_라인ID).jsonPath()
                        .getList("stations.name"))
                        .contains("신도림역","문래역","합정역","홍대입구역")
        );


    }

    /**
     * given 초록선 노선을 생성한다.
     * given 초록선 노선에 구간을 생성한다.(2개)
     * when  구간을 삭제하면
     * then  구간 목록에서 해당 구간을 찾을 수 없다. (종착역)
     */
    @Test
    @DisplayName("구간을 제거한다")
    void deleteSection(){

        Long 구간제거테스트_종작역ID = 지하철역_생성("홍대입구역").jsonPath().getLong("id");

        구간_생성(새로운구간_종착ID, 구간제거테스트_종작역ID, 초록선_라인ID);

        구간_제거(초록선_라인ID, 구간제거테스트_종작역ID);
        ExtractableResponse<Response> response = 지하철노선_단일조회(초록선_라인ID);

        assertThat(response.jsonPath()
                        .getList("stations.id"))
                        .doesNotContain(구간제거테스트_종작역ID);
    }

    @Test
    @DisplayName("구간을 조회한다")
    public void getSections(){

       ExtractableResponse<Response> response = 구간목록을_조회한다();

       assertAll(
               () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
               () -> assertThat(response.jsonPath().getList("id").size()).isEqualTo(3)
       );

    }

    /**
     * given 초록선 노선을 생성한다.
     * given 초록선 노선에 구간을 생성한다.(2개)
     * when  해당 노선의 하행 종점역에 맞지 않는 새로운 구간의 상행역 생성을 오청하면
     * then  구간을 생성 할 수 없다.
     */
    @Test
    @DisplayName("구간생성을 실패한다.")
    void failCreateSection(){
        //given
        Long 구간생성테스트_종착역ID = 지하철역_생성("홍대입구역").jsonPath().getLong("id");

        //when
        ExtractableResponse<Response> response = 구간_생성(초록선_시작ID, 구간생성테스트_종착역ID, 초록선_라인ID);
        //새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 한다
        // then 실패한다.
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    /**
     * given 초록선 노선을 생성한다.
     * given 초록선 노선에 구간을 생성한다.(2개)
     * when  새로운 구간의 하행역을 해당 노선에 등록되어 있는 역으로 생성한다.
     * then  구간을 생성 할 수 없다.
     */
    @Test
    @DisplayName("구간생성을 실패한다 두번째 시나리오.")
    void failCreateSection2(){
        // given
        Long 구간생성테스트_종착역ID = 지하철역_생성("홍대입구역").jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> response = 구간_생성(새로운구간_종착ID, 초록선_시작ID, 초록선_라인ID);
        //새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없다.
        // then 실패한다.
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    /**
     * given 초록선 노선을 생성한다.
     * given 초록선 노선에 구간을 생성한다.(2개)
     * when  마지막 구간이 아닌 구간삭제 요청을 한다.
     * then  구간을 제거할 수 없다.
     */
    @Test
    @DisplayName("구간을 제거한다_실페 케이스")
    void failDeleteSection(){
        //given
        Long 구간제거테스트_종작역ID = 지하철역_생성("홍대입구역").jsonPath().getLong("id");
        구간_생성(새로운구간_종착ID, 구간제거테스트_종작역ID, 초록선_라인ID);

        //when
        ExtractableResponse<Response> response = 구간_제거(초록선_라인ID, 새로운구간_종착ID);

        //지하철 노선에 등록된 역(하행 종점역)만 제거할 수 있다. 즉, 마지막 구간만 제거할 수 있다.
        // then 실패한다.
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());

    }

}