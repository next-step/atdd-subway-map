package sections;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import subway.SchemaInitSql;
import subway.StationFixture;
import subway.SubwayApplication;

@SchemaInitSql
@SpringBootTest(classes = SubwayApplication.class, webEnvironment = WebEnvironment.DEFINED_PORT)
public class SectionCreateServiceTest {


    StationFixture stationFixture = new StationFixture();

    @Test
    public void create() {

    }

}
