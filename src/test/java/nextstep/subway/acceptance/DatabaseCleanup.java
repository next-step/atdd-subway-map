package nextstep.subway.acceptance;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class DatabaseCleanup {

	@PersistenceContext
	private EntityManager entityManager;

	private List<String> tableNames;

	@PostConstruct
	public void init() {
		tableNames = entityManager.getMetamodel().getEntities()
			.stream()
			.map(e -> e.getName().toUpperCase())
			.collect(Collectors.toList());

		log.info("all table list = {}", tableNames);
	}

	@Transactional
	public void execute() {
		entityManager.flush();
		entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();

		for (String tableName : tableNames) {
			entityManager.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate();
			entityManager.createNativeQuery("ALTER TABLE " + tableName + " ALTER COLUMN ID RESTART WITH 1").executeUpdate();
		}

		entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
	}
}
