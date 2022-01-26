package nextstep.subway.line.application;

import nextstep.subway.common.exception.LineNotFoundException;
import nextstep.subway.line.application.dto.LineRequest;
import nextstep.subway.line.application.dto.LineResponse;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.section.application.SectionService;
import nextstep.subway.section.application.dto.SectionRequest;
import nextstep.subway.section.domain.manager.LineData;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import java.util.List;
import java.util.stream.Collectors;
import static nextstep.subway.common.ErrorMessages.DUPLICATE_LINE_NAME;

@Service
@Transactional
public class LineService {
    private final LineRepository lineRepository;
    private final SectionService sectionService;

    public LineService(LineRepository lineRepository, SectionService sectionService) {
        this.lineRepository = lineRepository;
        this.sectionService = sectionService;
    }

    public LineResponse saveLine(LineRequest request) {
        checkExistsLineName(request.getName());
        Line line = lineRepository.save(request.toLine());

        if (request.hasSectionAvailable()) {
            SectionRequest sectionRequest = new SectionRequest(request.getUpStationId(), request.getDownStationId(), request.getDistance());
            sectionService.addSection(line.getId(), sectionRequest);
        }

        return LineResponse.of(line);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAllLines() {
        List<LineData> lineDataList = lineRepository.findAll().stream()
                .map(LineData::of).collect(Collectors.toList());

        return sectionService.findAllStations(lineDataList).stream()
                .map(LineResponse::of).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse findLineById(Long id) {
        LineData lineData = LineData.of(getLine(id));
        return LineResponse.of(sectionService.findStations(lineData));
    }

    public void updateLine(Long id, LineRequest updateRequest) {
        Line line = getLine(id);
        if (!line.getName().equals(updateRequest.getName()))
            checkExistsLineName(updateRequest.getName());

        line.update(updateRequest);
    }

    public void deleteLineById(Long id) {
        getLine(id);
        sectionService.removeLineById(id);
        lineRepository.deleteById(id);
    }

    private Line getLine(Long id) {
        Line line = lineRepository.findById(id)
                .orElseThrow(() -> new LineNotFoundException());
        return line;
    }

    private void checkExistsLineName(String name) {
        Assert.isTrue(!lineRepository.existsByName(name), DUPLICATE_LINE_NAME.getMessage());
    }
}
