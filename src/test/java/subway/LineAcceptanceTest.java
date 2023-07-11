package subway;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;
import static subway.helper.LineTestAssuredHelper.*;
import static subway.helper.LineTestRequestHelper.*;

@DisplayName("지하철노선 관련 기능")
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Sql(scripts = {"classpath:data/station-init.sql"})
public class LineAcceptanceTest {

    final String 신분당선 = "신분당선";
    final String 분당선 = "분당선";
    final String 빨간색 = "bg-red-600";
    final String 노란색 = "bg-yellow-600";
    final Long 청계산입구역_ID = 1L;
    final Long 판교역_ID = 2L;
    final Long 선릉역_ID = 3L;

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철노선 생성")
    @Test
    public void createStationLine(){
        //when
        지하철노선_생성(신분당선, 빨간색, 청계산입구역_ID, 판교역_ID, 10);
        //then
        var getResponse = 지하철노선_조회();
        지하철노선_검증하기(getResponse, 신분당선, 빨간색);
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다
     */
    @DisplayName("지하철노선 목록 조회")
    @Test
    public void viewStationLineList(){
        //given
        지하철노선_생성(신분당선, 빨간색, 청계산입구역_ID, 판교역_ID, 10);
        지하철노선_생성(분당선, 노란색, 청계산입구역_ID, 선릉역_ID, 10);

        //when
        var getResponse = 지하철노선_조회();

        //then
        지하철노선_검증하기(getResponse, 신분당선, 빨간색);
        지하철노선_검증하기(getResponse, 분당선, 노란색);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다
     */
    @DisplayName("지하철노선 조회")
    @Test
    public void viewSingleStationLine(){
        //given
        var createResponse = 지하철노선_생성(신분당선, 빨간색, 청계산입구역_ID, 판교역_ID, 10);

        //when
        var getResponse = 지하철노선_단건_조회(createResponse);

        //then
        지하철노선_검증하기(createResponse, getResponse);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선의 정보는 수정된다
     */
    @DisplayName("지하철노선 수정")
    @Test
    public void updateStationLine(){
        //given
        var createResponse = 지하철노선_생성(신분당선, 빨간색, 청계산입구역_ID, 판교역_ID, 10);

        //when
        final String 수내역 = "수내역";
        final String 파란색 = "bg-blue-600";
        지하철노선_수정(createResponse, 수내역, 파란색);

        //then
        var changeResponse = 지하철노선_단건_조회(createResponse);
        지하철노선_수정_검증(changeResponse.jsonPath().getString("name"), 수내역, changeResponse.jsonPath().getString("color"), 파란색);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철노선 삭제")
    @Test
    public void removeStationLine(){
        //given
        var createResponse = 지하철노선_생성(신분당선, 빨간색, 청계산입구역_ID, 판교역_ID, 10);

        //when
        지하철노선_삭제(createResponse);

        //then
        var getResponse = 지하철노선_조회();
        지하철노선_삭제_검증하기(getResponse, 신분당선, 빨간색);
    }
}
