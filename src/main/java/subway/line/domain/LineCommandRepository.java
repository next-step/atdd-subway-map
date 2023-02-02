package subway.line.domain;

public interface LineCommandRepository {

    Line save(Line line);

    void deleteById(Long lineId);
}
