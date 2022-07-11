package nextstep.subway.applicaion;

import com.google.common.base.CaseFormat;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DatabaseCleanup implements InitializingBean {

	@PersistenceContext
	private EntityManager em;

	private List<String> tableNames;

	@Override
	public void afterPropertiesSet() throws Exception {
		tableNames = em.getMetamodel().getEntities().stream()
				.filter(e -> e.getJavaType().getAnnotation(Entity.class) != null)
				.map(e -> CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, e.getName()))
				.collect(Collectors.toList());
	}

	@Transactional
	public void execute() {
		em.flush();
		em.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();

		for (String tableName : tableNames) {
			em.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate();
			em.createNativeQuery("ALTER TABLE " + tableName + " ALTER COLUMN " +
					"ID RESTART WITH 1").executeUpdate();
		}
		em.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
	}
}
