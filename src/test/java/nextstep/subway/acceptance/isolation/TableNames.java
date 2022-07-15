package nextstep.subway.acceptance.isolation;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;

public class TableNames {

	private final Set<TableName> values = new HashSet<>();

	public TableNames(Set<EntityType<?>> entityTypes) {
		if (entityTypes == null) {
			throw new IllegalArgumentException("entityTypes is Null");
		}

		Set<TableName> result = entityTypes.stream()
			.map(TableName::new)
			.collect(Collectors.toUnmodifiableSet());

		values.addAll(result);
	}

	public void clean(EntityManager em) {
		values.forEach(tableName -> tableName.clean(em));
	}
}
