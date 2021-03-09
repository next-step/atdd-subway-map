package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.exception.LineAlreadyExistsException;
import nextstep.subway.line.exception.LineIllegalStationException;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {
    private LineRepository lineRepository;
    private StationService stationService;

    public LineService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    public LineResponse saveLine(LineRequest request) {
        validateReduplicationLine(request);
        Station upStation = stationService.findStationById(request.getUpStationId());
        Station downStation = stationService.findStationById(request.getDownStationId());
        Line persistLine = lineRepository.save(request.toLine(upStation, downStation));
        return LineResponse.of(persistLine);
    }

    @Transactional(readOnly = true)
    public void validateReduplicationLine(LineRequest request) {
        List<Line> lines = lineRepository.findByNameContaining(request.getName());

        if(lines.size() > 0) {
            throw new LineAlreadyExistsException();
        };
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAllLines() {
        List<Line> lines = lineRepository.findAll();

        return lines.stream()
                .map(line -> LineResponse.of(line))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse findLine(Long id) {
        Line line = lineRepository.findById(id).get();
        return LineResponse.of(line);
    }

    public LineResponse updateLineById(Long id, LineRequest lineRequest) {
        Line line = lineRepository.findById(id).get();
        line.update(lineRequest.toLine());
        return LineResponse.of(line);
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    public void saveSections(Long id, LineRequest lineRequest) {
        Line line = lineRepository.findById(id).get();
        Station upStation = stationService.findStationById(lineRequest.getUpStationId());
        Station downStation = stationService.findStationById(lineRequest.getDownStationId());
        validateStationMatching(line, upStation, downStation);
        line.addSections(line, upStation, downStation, lineRequest.getDistance());
    }

    private void validateStationMatching(Line line, Station upStation, Station downStation) {
        List<Section> sections = line.getSections();
        Section lastSection = sections.get(sections.size() - 1);
        if (lastSection.getDownStation() != upStation) {
            throw new LineIllegalStationException("새 구간의 상행역은 기존 하행 종점역이어야 합니다.");
        }
        sections.stream()
                .filter(s -> s.getUpStation() == downStation || s.getDownStation() == downStation)
                .findAny()
                .ifPresent(s -> {
                    throw new LineIllegalStationException("새 구간의 하행역은 기존에 존재할 수 없습니다.");
                });
    }
}

