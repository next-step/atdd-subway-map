package subway.application.service.input;

import subway.domain.Line;

import java.util.List;

public interface LineLoadUseCase {

    Line loadLine(Long createdLineId);

    List<Line> loadLines();

}
