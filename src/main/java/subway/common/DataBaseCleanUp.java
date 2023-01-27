package subway.common;

import com.google.common.base.CaseFormat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import javax.transaction.Transactional;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataBaseCleanUp {

    private final EntityManager entityManager;
    private List<String> tableNames;

    @Transactional
    public void cleanUp() {
        // 쓰기 지연 저장소에 남은 SQL 마저 수행
        entityManager.flush();

        // 연관 관계 매핑 테이블의 참조 무결성을 해제
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();

        // 테이블 삭제
        for (String tableName : tableNames) {
            try {
                entityManager.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate();
            } catch (Exception e) {
                log.error("{}", e);
            }
        }

        // 참조 무결성 설정
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
    }

    @PostConstruct
    public void init() {
        tableNames = entityManager.getMetamodel()
                .getEntities()
                .stream()
                .filter(isEntity())
                .map(EntityType::getName)
                .map(name -> CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name))
                .collect(Collectors.toList());
    }

    private Predicate<EntityType<?>> isEntity() {
        return e -> e.getJavaType().getAnnotation(Entity.class) != null;
    }
}
