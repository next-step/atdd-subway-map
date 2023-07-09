package subway;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

//TODO: EntityManager를 이용한 Truncate 시도 해보기
@Sql(scripts = "/beforeclass.sql")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public abstract class AcceptanceTest {
}
