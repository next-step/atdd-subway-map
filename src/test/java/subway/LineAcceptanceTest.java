package subway;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import io.restassured.path.json.JsonPath;

@DisplayName("지하철 노선도 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("노선 생성 인수 테스트")    
    @Test
    void createLine()
    {
        String upStationName = "독립문역";
        String downStationName = "경복궁역";
        long upStationId = StationTest.지하철역을_생성한다(upStationName);
        long downStationId = StationTest.지하철역을_생성한다(downStationName);

        String lineName = "3호선";
        String lineColor = "주황색";
        LineTest.노선을_생성한다(lineName, lineColor, upStationId, downStationId, 10);
        List<String> lineList = LineTest.노선_이름목록을_조회한다();

        assertThat(lineList).containsAnyOf(lineName);
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("노선 목록 조회 인수 테스트")
    @Test
    void getLinesList()
    {
        String[] upStationName = {"독립문역", "서대문역"};
        String[] downStationName = {"경복궁역", "광화문역"};
        String[] lineName = {"3호선", "5호선"};
        String[] lineColor = {"주황색", "보라색"};

        long[] upStationId = new long[2];
        long[] downStationId = new long[2];

        upStationId[0]= StationTest.지하철역을_생성한다(upStationName[0]);
        upStationId[1]= StationTest.지하철역을_생성한다(upStationName[1]);
        downStationId[0]  = StationTest.지하철역을_생성한다(downStationName[0]);
        downStationId[1]  = StationTest.지하철역을_생성한다(downStationName[1]);

        LineTest.노선을_생성한다(lineName[0], lineColor[0], upStationId[0], downStationId[0], 10);
        LineTest.노선을_생성한다(lineName[1], lineColor[1], upStationId[1], downStationId[1], 10);

        List<String> lineList = LineTest.노선_이름목록을_조회한다();

        assertAll(
            () -> assertThat(lineList).containsAnyOf(lineName[0]),
            () -> assertThat(lineList).containsAnyOf(lineName[1]),
            () -> assertThat(lineList).size().isEqualTo(2)
        );
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("노선 정보 조회 인수 테스트")
    @Test
    void getLine()
    {
        String upStationName = "독립문역";
        String downStationName = "경복궁역";
        long upStationId = StationTest.지하철역을_생성한다(upStationName);
        long downStationId = StationTest.지하철역을_생성한다(downStationName);

        String lineName = "3호선";
        String lineColor = "주황색";

        Long lineId = LineTest.노선을_생성한다(lineName, lineColor, upStationId, downStationId, 10);
        JsonPath result = LineTest.노선을_조회한다(lineId);

        List<String> stationList = result.getList("stations.name", String.class);

        assertAll(
            () -> assertThat(result.getString("name")).isEqualTo(lineName),
            () -> assertThat(result.getString("color")).isEqualTo(lineColor),
            () -> assertThat(stationList).containsAnyOf(upStationName),
            () -> assertThat(stationList).containsAnyOf(downStationName)
        );
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다.
     */
    @DisplayName("노선 수정 인수 테스트")
    @Test
    void modifyLine()
    {
        String upStationName = "독립문역";
        String downStationName = "경복궁역";
        long upStationId = StationTest.지하철역을_생성한다(upStationName);
        long downStationId = StationTest.지하철역을_생성한다(downStationName);

        String lineName = "3호선";
        String lineColor = "주황색";
        Long lineId = LineTest.노선을_생성한다(lineName, lineColor, upStationId, downStationId, 10);

        LineTest.노선을_수정한다(lineId, "GTX-A", lineColor, upStationId, downStationId, 10);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다.
     */
    @DisplayName("노선 삭제 인수 테스트")
    @Test
    void deleteLine()
    {
        String upStationName = "독립문역";
        String downStationName = "경복궁역";
        long upStationId = StationTest.지하철역을_생성한다(upStationName);
        long downStationId = StationTest.지하철역을_생성한다(downStationName);

        String lineName = "3호선";
        String lineColor = "주황색";
        long lineId = LineTest.노선을_생성한다(lineName, lineColor, upStationId, downStationId, 10);

        LineTest.노선을_삭제한다(lineId);

        List<String> lineList = LineTest.노선_이름목록을_조회한다();

        assertThat(lineList).doesNotContain(lineName);
    }
}