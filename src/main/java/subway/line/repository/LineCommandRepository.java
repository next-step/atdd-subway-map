package subway.line.repository;

import subway.line.domain.Line;


public interface LineCommandRepository {

    Line save(Line station);

    void deleteById(Long id);

}