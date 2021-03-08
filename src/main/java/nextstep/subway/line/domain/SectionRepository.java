package nextstep.subway.line.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SectionRepository extends JpaRepository<Section, Long> {

    @Query("select s from Section s where s.line.id = :lineId and s = (select max(s1) from Section s1 where s1.line = s.line)")
    Section findLastSectionByLineId(@Param("lineId") Long lineId);
}
