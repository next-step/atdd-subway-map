package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.applicaion.exception.DuplicationNameException;
import nextstep.subway.applicaion.exception.EntityNotFoundException;
import nextstep.subway.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {
    private LineRepository lineRepository;
    private StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public LineResponse saveLine(LineRequest request) {
        if (lineRepository.existsByName(request.getName())) {
            throw new DuplicationNameException();
        }

        Line line = new Line(request.getName(), request.getColor());
        Section section = createSection(line, request.getUpStationId(), request.getDownStationId(), request.getDistance());
        line.addSection(section);

        lineRepository.save(line);
        return LineResponse.from(line);
    }

    private Section createSection(Line line, Long upStationId, Long downStationId, int distance) {
        Station upStation = findStationById(upStationId);
        Station downStation = findStationById(downStationId);
        return Section.of(line, upStation, downStation, distance);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAllLines() {
        List<Line> lines = lineRepository.findAll();

        return lines.stream()
                .map(LineResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse findLine(Long id) {
        return LineResponse.from(findById(id));
    }

    @Transactional(readOnly = true)
    public Line findById(Long id) {
        return lineRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public LineResponse updateLine(Long id, LineRequest lineRequest) {
        Line line = findById(id);
        line.update(lineRequest.getName(), lineRequest.getColor());
        return LineResponse.from(line);
    }

    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }

    public void addSection(Long lineId, SectionRequest request) {
        Line line = findById(lineId);
        Section section = createSection(line, request.getUpStationId(), request.getDownStationId(), request.getDistance());

        line.addSection(section);
    }

    private Station findStationById(Long stationId) {
        return stationRepository.findById(stationId).orElseThrow(EntityNotFoundException::new);
    }

    public LineResponse removeSection(Long lineId, Long stationId) {
        Line line = findById(lineId);
        Station station = findStationById(stationId);

        line.removeSection(station);
        return LineResponse.from(line);
    }
}
