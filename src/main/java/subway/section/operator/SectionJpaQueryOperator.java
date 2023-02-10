package subway.section.operator;


import org.springframework.data.jpa.repository.JpaRepository;
import subway.section.domain.Section;
import subway.section.repository.SectionQueryRepository;


public interface SectionJpaQueryOperator
        extends SectionQueryRepository, JpaRepository<Section, Long> {
}