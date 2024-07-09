package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("지하철노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class SubwayLineAcceptanceTest {
    /*
     * 스토리 1: 나는 관리자로서 지하철 노선을 생성하여 새로운 노선을 추가하고 싶다.
     * given 지하철 정보를 입력하고
     * when 지하철역 노선을 생성하면
     * /then 지하철역 노선이 생성된다
     */

    @Test
    void createSubwayLine() {
        //when
        var createdResponse = requestCreateSubwayLine("신분당선", "bg-red", 1L, 10L);

        //then
        assertThat(createdResponse.jsonPath().getString("name")).isEqualTo("신분당선");
        assertThat(createdResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

    }

//    class MockSubwayLineRequest{
//        private String name;
//        private String color;
//        private Long upStationId;
//        private Long downStationId;
//        private Long distance;
//
//        SubwayLineRequest toRequest(String name = "newtork"){
//
//        }
//        String toJson(){
//            var objectMapper = new ObjectMapper();
//            return toRequest().writeValueAsString()
//
//        }
//    }

    private ExtractableResponse<Response> requestCreateSubwayLine(String name, String color, Long upStationId, Long downStationId) {

        return RestAssured.given().body(
//                        SubwayLineRequest(
//                                name,
//                                color,
//                                upStationId,
//                                downStationId
//                        ).toJsonString()
                        Map.of("name", name, "color", color, "upStationId", 1, "downStationId", 2, "distance", 10)
                )
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/subwayline")
                .then()
                .extract();
    }

    /**
     * 스토리 2: 나는 관리자로서 지하철 노선 목록을 조회하여 모든 노선을 관리하고 싶다.
     * given 지하철노선이 3개일때
     * when 지하철역 노선목록을 조회하면
     * then 지하철역 노선3개가 조회된다.
     */


    /**
     * 스토리 3: 나는 관리자로서 특정 지하철 노선을 조회하여 해당 노선의 정보를 확인하고 싶다.
     * given 지하철역 노선이 등록되어있는 경우
     *when 해당 지하철역 노선을 조회한다
     * then: 지하철역 노선이 조회된다.
     */

    /**
     * 스토리 3: 나는 관리자로서 특정 지하철 노선을 조회하여 해당 노선의 정보를 확인하고 싶다.
     * given 지하철역 노선이 등록되지 않은 경우
     * when 해당 지하철역 노선을 조회한다
     * then: 지하철역 노선이 조회되지 않는다..
     */

    /**
     * 스토리 4: 나는 관리자로서 지하철 노선을 수정하여 변경된 정보를 반영하고 싶다.
     * given: 노선이 등로고딘 경우
     * when: 노선의 이름을 수정한다..
     * then: 이름이 수정된다.
     */


    /**
     * 스토리 5: 나는 관리자로서 특정 지하철 노선을 삭제하여 불필요한 노선을 제거하고 싶다.
     * given: 노선이 등록된 경우
     * when: 노선을 삭제한다.
     * then: 노선이 삭제된다.
     */

}
