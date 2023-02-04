package subway.application.service.input;

import subway.domain.Line;
import subway.domain.LineLoadDto;

import java.util.List;

public interface LineLoadUseCase {

    LineLoadDto loadLineDto(Long createdLineId);

    Line loadLine(Long createdLineId);

    List<LineLoadDto> loadLineDtos();

}
