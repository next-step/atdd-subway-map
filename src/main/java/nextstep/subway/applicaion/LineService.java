package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineChangeRequest;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.domain.*;
import nextstep.subway.exception.LineNotFoundException;
import nextstep.subway.exception.StationNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.*;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        Station findUpStation = findStationById(lineRequest.getUpStationId());
        Station findDownStation = findStationById(lineRequest.getDownStationId());

        Line line = Line.of(lineRequest.getName(), lineRequest.getColor());
        line.addSection(Section.of(findUpStation, findDownStation, lineRequest.getDistance()));

        Line createLine = lineRepository.save(line);
        return createLineResponse(createLine);
    }

    public List<LineResponse> findAllLines() {
        List<Line> findLines = lineRepository.findAll();
        return findLines.stream()
                .map(this::createLineResponse)
                .collect(toList());
    }

    public LineResponse getLineById(Long id) {
        Line findLine = findLineById(id);
        return createLineResponse(findLine);
    }

    @Transactional
    public void changeLineById(Long lineId, LineChangeRequest lineChangeRequest) {
        Line findLine = findLineById(lineId);
        findLine.change(lineChangeRequest.getName(), lineChangeRequest.getColor());
    }

    @Transactional
    public void deleteLineById(Long lineId) {
        lineRepository.deleteById(lineId);
    }

    @Transactional
    public void addSection(Long lineId, SectionRequest sectionRequest) {
        Line findLine = findLineById(lineId);

        Station findUpStation = findStationById(sectionRequest.getUpStationId());
        Station findDownStation = findStationById(sectionRequest.getDownStationId());

        Section section = Section.of(findUpStation, findDownStation, sectionRequest.getDistance());

        findLine.addSection(section);
    }

    private Station findStationById(Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new StationNotFoundException("존재하지 않는 지하철역입니다."));
    }

    private LineResponse createLineResponse(Line line) {
        return LineResponse.from(line, line.allStations());
    }

    private Line findLineById(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new LineNotFoundException("존재하지 않는 노선입니다."));
    }
}
