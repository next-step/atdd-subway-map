package nextstep.subway.acceptance.isolation;

import static nextstep.subway.acceptance.isolation.SqlQueryTemplate.*;

import javax.persistence.EntityManager;
import javax.persistence.Table;
import javax.persistence.metamodel.EntityType;

import groovy.transform.EqualsAndHashCode;

@EqualsAndHashCode
public class TableName {

	private static final Class<Table> TABLE_ANNOTATION = Table.class;

	private final String value;

	public TableName(EntityType<?> entityType) {
		if (entityType == null) {
			throw new IllegalArgumentException("entityType is null");
		}
		this.value = parseTableNames(entityType.getJavaType());
	}

	private String parseTableNames(Class<?> javaType) {
		if (javaType.isAnnotationPresent(TABLE_ANNOTATION)) {
			return javaType.getAnnotation(TABLE_ANNOTATION).name();
		}
		return javaType.getSimpleName();
	}

	public void clean(EntityManager em) {
		em.createNativeQuery(TRUNCATE_TABLE.param(value)).executeUpdate();
		em.createNativeQuery(INITIALIZE_AUTO_INCREMENT.param(value)).executeUpdate();
	}
}
