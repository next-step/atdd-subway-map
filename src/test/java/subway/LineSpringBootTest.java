package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import subway.controller.dto.line.LineModifyRequest;
import subway.controller.dto.line.LineSaveRequest;
import subway.model.line.Line;
import subway.model.line.LineRepository;
import subway.model.station.Station;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

//@Sql("truncate_station.sql")
@Transactional
@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineSpringBootTest {

    @Autowired
    private LineRepository lineRepository;

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        lineRepository.save(Line.builder().color("abc").name("222").downStation(Station.builder().name("dur").build()).build());
    }
}
