package nextstep.subway.applicaion;

import javassist.NotFoundException;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.domain.*;
import nextstep.subway.exception.NotFoundLineException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {
    private final LineRepository lineRepository;
    private final StationService stationService;

    public LineService(LineRepository lineRepository,
                       StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    public LineResponse saveLine(LineRequest request) {
        return addSectionToLine(
                lineRepository.save(Line.of(request)),
                request.toSectionRequest()
        );
    }

    public LineResponse addSectionToLine(Line line, SectionRequest sectionRequest) {
        Station upStation = stationService.findById(sectionRequest.getUpStationId());
        Station downStation = stationService.findById(sectionRequest.getDownStationId());
        line.addSection(Section.of(line,
                upStation,
                downStation,
                sectionRequest.getDistance()));
        return LineResponse.of(lineRepository.save(line));
    }

    public List<LineResponse> getLines() {
        return lineRepository.findAll().stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    public LineResponse getLine(long lineId) {
        return LineResponse.of(findLineById(lineId));
    }

    public void updateLine(long lineId, LineRequest lineRequest) {
        lineRepository.save(Line.of(lineId, lineRequest));
    }

    public void deleteLine(long lineId) {
        lineRepository.deleteById(lineId);
    }

    public LineResponse addSectionToLine(long lineId, SectionRequest sectionRequest) {
        return addSectionToLine(findLineById(lineId), sectionRequest);
    }

    public Line findLineById(long lineId) {
        return lineRepository.findById(lineId)
                .orElseThrow(() -> new NotFoundLineException(lineId));
    }
}
