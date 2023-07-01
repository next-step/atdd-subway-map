package line;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import subway.SubwayApplication;
import subway.line.LineCreateRequest;
import subway.line.LineCreateResponse;
import subway.line.LineService;


@SpringBootTest(classes = SubwayApplication.class, webEnvironment = WebEnvironment.DEFINED_PORT)
public class LineServiceTest {

    @Autowired
    private LineService lineService;

    @Test
    void createLine() {
        LineCreateRequest lineCreateRequest = new LineCreateRequest("신분당선", "bg-600", 1L, 2L, 10);

        LineCreateResponse lineCreateResponse = lineService.createStation(lineCreateRequest);

        Assertions.assertThat(lineCreateResponse.getName()).isEqualTo("신분당선");
    }

}
