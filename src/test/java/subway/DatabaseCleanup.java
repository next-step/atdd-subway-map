package subway;

import com.google.common.base.CaseFormat;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Profile("acceptance")
public class DatabaseCleanup implements InitializingBean{
    @PersistenceContext
    private EntityManager entityManager;
    private List<String> tableNames;
    @Override
    public void afterPropertiesSet() {
        tableNames = entityManager.getMetamodel().getEntities().stream()
                .filter(e -> e.getJavaType().getAnnotation(Entity.class) != null)
                .map(e -> CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, e.getName()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void execute() {
        // 쓰기 지연 저장소에 남은 SQL을 마저 수행
        entityManager.flush();
        // 연관 관계 맵핑된 테이블이 있는 경우 참조 무결성을 해제해줘야 Trancate 가능
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();

        for (String tableName : tableNames) {
            // 테이블 이름 순회하며 Trancate SQL 수행
            entityManager.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate();
            // 테이블의 내부가 지워지면 그 다음부터는 ID값을 다시 1부터 시작할 수 있도록 기본 값 초기화
            entityManager.createNativeQuery("ALTER TABLE " + tableName + " ALTER COLUMN "
                    + "ID RESTART WITH 1").executeUpdate();
        }
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
    }
}
