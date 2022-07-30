package nextstep.subway.acceptance.tool;

import org.apache.commons.text.CaseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TestObjectDestroyer {

    @Autowired
    EntityManager em;

    List<String> tableNames;

    @PostConstruct
    public void gatherTableNames() {
        tableNames = em.getMetamodel().getEntities().stream().map(e -> CaseUtils.toCamelCase(e.getName(), false, '_').toLowerCase()).collect(Collectors.toList());
    }

    @Transactional
    public void destroyAll() {
        tableNames.forEach((it) -> em.createNativeQuery("TRUNCATE TABLE " + it).executeUpdate());
    }

    @Transactional
    public void destroy(List<String> targetTables) {
        targetTables.forEach((it) -> em.createNativeQuery("TRUNCATE TABLE " + it).executeUpdate());
    }
}
