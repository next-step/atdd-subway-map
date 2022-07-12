package nextstep.subway.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface LineRepository extends JpaRepository<Line, Long> {

    @Modifying
    @Query("UPDATE Line l SET l.name = :name, l.color = :color WHERE l.id = :id ")
    void updateNameAndColor(Long id, String name , String color);
}
