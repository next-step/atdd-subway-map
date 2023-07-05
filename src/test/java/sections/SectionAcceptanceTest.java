package sections;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;

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
import subway.SchemaInitSql;
import subway.StationInitSql;
import subway.SubwayApplication;
import subway.section.model.SectionCreateRequest;

@SchemaInitSql
@StationInitSql
@DisplayName("지하철 구간 관련 기능")
@SpringBootTest(classes = SubwayApplication.class, webEnvironment = WebEnvironment.DEFINED_PORT)
public class SectionAcceptanceTest {


    @Nested
    class Given_2개의_노선이_있을때 {

        @Nested
        class When_구간을_등록하면 {


            @DisplayName("Then_구간을 등록할 수 있다")
            @Test
            void registerSection() {
                Map<String, Object> requestBody = new HashMap<>();
                requestBody.put("downStationId", 1L);
                requestBody.put("upStationId", 2L);
                requestBody.put("distance", 10);

                ExtractableResponse<Response> response = RestAssured.given().log().all()
                                                                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                                    .body(new SectionCreateRequest())
                                                                    .when().post(buildCreateSectionUrl(1L))
                                                                    .then().extract();

                assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
            }

            private String buildCreateSectionUrl(Long lineId) {
                return "/lines/" + lineId + "/sections";
            }
        }
    }
}
