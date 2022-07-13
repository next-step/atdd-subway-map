package nextstep.subway;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@ActiveProfiles("test")
@Sql("classpath:/truncate.sql")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BaseSpringBootTest {

}

