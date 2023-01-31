package subway.common;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Table;

import org.springframework.boot.test.context.TestComponent;
import org.springframework.util.StringUtils;

import com.google.common.base.CaseFormat;

@TestComponent
public class JpaDataBaseCleaner {

	private final Set<String> tableNames = new HashSet<>();

	private final EntityManager entityManager;

	public JpaDataBaseCleaner(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@PostConstruct
	void extractEntityTableNames() {
		this.tableNames.addAll(getTableNamesFromTableAnnotation());
		this.tableNames.addAll(getTableNamesFromEntityClassName());
	}

	public void execute() {
		entityManager.flush();
		entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();

		for (String tableName : this.tableNames) {
			entityManager.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate();
			entityManager.createNativeQuery("ALTER TABLE " + tableName +
				" ALTER COLUMN ID RESTART WITH 1").executeUpdate();
		}
	}

	private Set<String> getTableNamesFromTableAnnotation() {
		return entityManager.getMetamodel()
			.getEntities()
			.stream()
			.filter(entityType -> entityType.getJavaType().getAnnotation(Entity.class) != null)
			.filter(entityType -> entityType.getJavaType().isAnnotationPresent(Table.class))
			.filter(entityType -> StringUtils.hasText(entityType.getJavaType().getAnnotation(Table.class).name()))
			.map(entityType -> entityType.getJavaType().getAnnotation(Table.class).name())
			.collect(Collectors.toUnmodifiableSet());
	}

	private Set<String> getTableNamesFromEntityClassName() {
		return entityManager.getMetamodel()
			.getEntities()
			.stream()
			.filter(entityType -> entityType.getJavaType().getAnnotation(Entity.class) != null)
			.filter(entityType -> !entityType.getJavaType().isAnnotationPresent(Table.class))
			.map(entityType -> CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, entityType.getName()))
			.collect(Collectors.toUnmodifiableSet());
	}
}
