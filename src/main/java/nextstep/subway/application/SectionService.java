package nextstep.subway.application;

import lombok.RequiredArgsConstructor;
import nextstep.subway.common.exception.CustomException;
import nextstep.subway.common.exception.ResponseCode;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.SectionRepository;
import nextstep.subway.ui.dto.section.CreateSectionRequest;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SectionService {
    private final SectionRepository sectionRepository;
    private final LineRepository lineRepository;

    public void createSection(long lineId, CreateSectionRequest request) {
        Line line = lineRepository.findById(lineId)
                                  .orElseThrow(() -> new CustomException(ResponseCode.LINE_NOT_FOUND));

        if(line.getDownStation().getId() != request.getUpStationId()){
            throw new CustomException(ResponseCode.LINE_NOT_FOUND);
        }
    }
}
