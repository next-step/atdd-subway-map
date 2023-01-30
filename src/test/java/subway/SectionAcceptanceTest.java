package subway;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.dto.LineRequest;
import subway.dto.SectionRequest;

@DisplayName("지하철 구간 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql("/init.sql")
public class SectionAcceptanceTest {
    private final SubwayRestApiClient client = new SubwayRestApiClient();
    private Long lineId;

    @BeforeEach
    void setup() {
        lineId = client.createLine(new LineRequest("신분당선", "bg-red-600", 1L, 2L, 10L))
            .jsonPath().getLong("id");
    }

    @Test
    @DisplayName("지하철 구간을 생성한다.")
    void createSection() {
        // when
        client.createStation("지하철역3");
        client.createSection(lineId, new SectionRequest(3L, 2L, 10L));

        // then
        ExtractableResponse<Response> response = client.findLineById(lineId);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList("stations.id", Long.class)).containsExactly(1L, 2L, 3L);
    }

    @Test
    @DisplayName("특정 지하철 구간을 삭제한다.")
    void deleteSection() {
        // given
        client.createStation("지하철역3");
        client.createSection(lineId, new SectionRequest(3L, 2L, 10L));

        // when
        client.deleteSection(1L, 3L);

        // then
        ExtractableResponse<Response> response = client.findLineById(1L);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList("stations.id", Long.class)).hasSize(2);
    }
}
