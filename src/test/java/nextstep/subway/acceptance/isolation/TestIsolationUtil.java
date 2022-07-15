package nextstep.subway.acceptance.isolation;

import static java.lang.Boolean.*;
import static nextstep.subway.acceptance.isolation.SqlQueryTemplate.*;

import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TestIsolationUtil {

	private final EntityManager em;
	private final TableNames tableNames;

	public TestIsolationUtil(EntityManager em) {
		this.em = em;
		this.tableNames = new TableNames(entities());
	}

	private Set<EntityType<?>> entities() {
		return em.getMetamodel().getEntities();
	}

	@Transactional
	public void clean() {
		em.flush();
		executeQuery(FOREIGN_KEY_CONSTRAINT_ENABLE.param(FALSE.toString()));
		tableNames.clean(em);
		executeQuery(FOREIGN_KEY_CONSTRAINT_ENABLE.param(TRUE.toString()));

	}

	private void executeQuery(String query) {
		em.createNativeQuery(query).executeUpdate();
	}
}
