package subway.common;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

@Import(JpaDataBaseCleaner.class)
@Transactional
public abstract class AcceptanceTest extends RestAssuredTest {

	@Autowired
	private JpaDataBaseCleaner jpaDataBaseCleaner;

	@AfterEach
	void cleanTable() {
		jpaDataBaseCleaner.execute();
	}
}
