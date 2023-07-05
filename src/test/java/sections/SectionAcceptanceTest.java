package sections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import subway.SchemaInitSql;
import subway.StationInitSql;
import subway.SubwayApplication;

@SchemaInitSql
@StationInitSql
@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(classes = SubwayApplication.class, webEnvironment = WebEnvironment.DEFINED_PORT)
public class SectionAcceptanceTest {


    /**
     * given
     * when
     * then
     */
    @Test
    void asdf() {

    }
}
