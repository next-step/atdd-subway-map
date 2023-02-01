package subway.line.repository;

import subway.line.domain.Line;

import java.util.List;
import java.util.Optional;


public interface LineQueryRepository {

    Line save(Line station);

    List<Line> findAll();

    Optional<Line> findById(Long id);

}