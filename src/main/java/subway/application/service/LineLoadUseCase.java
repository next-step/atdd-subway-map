package subway.application.service;

import subway.domain.Line;

import java.util.List;

public interface LineLoadUseCase {

    Line loadLine(Long createdLineId);

    List<Line> loadLines();

}
