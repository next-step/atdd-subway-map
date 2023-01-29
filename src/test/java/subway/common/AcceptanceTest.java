package subway.common;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@Import(JpaDataBaseCleaner.class)
public abstract class AcceptanceTest extends RestAssuredTest {

	@Autowired
	private JpaDataBaseCleaner jpaDataBaseCleaner;

	@AfterEach
	void cleanTable() {
		jpaDataBaseCleaner.execute();
	}
}
