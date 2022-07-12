package nextstep.subway.applicaion.line;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.dto.LineCreationRequest;
import nextstep.subway.applicaion.dto.LineModificationRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.domain.line.Line;
import nextstep.subway.domain.line.LineRepository;
import nextstep.subway.domain.section.Section;
import nextstep.subway.domain.section.SectionRepository;
import nextstep.subway.domain.station.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;

    public LineResponse create(LineCreationRequest request) {
        var upStation = stationRepository.findById(request.getUpStationId())
                .orElseThrow(() -> new IllegalArgumentException("올바르지 않은 역 ID 입니다."));
        var downStation = stationRepository.findById(request.getDownStationId())
                .orElseThrow(() -> new IllegalArgumentException("올바르지 않은 역 ID 입니다."));
        var firstSection = sectionRepository.save(new Section(upStation, request.getDistance()));
        var secondSection = sectionRepository.save(new Section(downStation));
        firstSection.setNextSection(secondSection);

        var line = new Line(request.getName(), request.getColor(), firstSection);

        return LineResponse.fromLine(lineRepository.save(line));
    }

    public LineResponse modify(Long lineId, LineModificationRequest request) {
        var line = lineRepository.findById(lineId)
                .orElseThrow(() -> new IllegalArgumentException("올바르지 않은 노선 ID 입니다."));

        line.changeNameAndColor(request.getName(), request.getColor());

        return LineResponse.fromLine(line);
    }

    public void remove(Long lineId) {
        lineRepository.deleteById(lineId);
    }
}
