package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.domain.*;
import nextstep.subway.exception.NotFoundStationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {
    private final LineRepository lineRepository;
    private final SectionRepository sectionRepository;
    private final StationService stationService;

    public LineService(LineRepository lineRepository,
                       SectionRepository sectionRepository,
                       StationService stationService) {
        this.lineRepository = lineRepository;
        this.sectionRepository = sectionRepository;
        this.stationService = stationService;
    }

    public LineResponse saveLine(LineRequest request) {
        Line line = lineRepository.save(Line.of(request));
        Station upStation = stationService.findById(request.getUpStationId());
        Station downStation = stationService.findById(request.getDownStationId());
        line.addSection(sectionRepository.save(Section.of(line,
                upStation,
                downStation,
                request.getDistance())));
        return LineResponse.of(lineRepository.save(line));
    }

    public List<LineResponse> getLines() {
        return lineRepository.findAll().stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    public Optional<LineResponse> getLine(long lineId) {
        Optional<Line> lineOptional = lineRepository.findById(lineId);
        if (lineOptional.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(LineResponse.of(lineOptional.get()));
    }

    public void updateLine(long lineId, LineRequest lineRequest) {
        lineRepository.save(Line.of(lineId, lineRequest));
    }

    public void deleteLine(long lineId) {
        lineRepository.deleteById(lineId);
    }
}
