package nextstep.subway.acceptance;

import com.google.common.base.CaseFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Profile("acceptance")
public class DbCleanup {

    @Autowired
    private EntityManager em;

    private List<String> tableNames;

    @PostConstruct
    public void init() {
        tableNames = em.getMetamodel().getEntities().stream()
                .filter(e -> e.getJavaType().getAnnotation(Entity.class) != null)
                .map(e -> CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, e.getName()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void execute() {
        // 네이티브 쿼리 수행전에 쓰기 지연 저장소의 SQL을 DB에 반영
        em.flush();
        // TRUNCATE 하기위해 참조 무결성 일시 정지
        em.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();
        truncateEntities();
        truncateEmbeddables();
        // TRUNCATE을 마치고 참조 무결성 켜주기
        em.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
    }

    private void truncateEntities() {
        for (String tableName : tableNames) {
            // 테이블을 순회하면서 TRUNCATE
            em.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate();
            // ID값을 1로 재설정
            em.createNativeQuery("ALTER TABLE " + tableName + " ALTER COLUMN id RESTART WITH 1").executeUpdate();
        }
    }

    private void truncateEmbeddables() {
        em.createNativeQuery("TRUNCATE TABLE line_section").executeUpdate();
    }

}
