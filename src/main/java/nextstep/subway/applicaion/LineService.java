package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.domain.*;
import nextstep.subway.exception.DuplicatedNameException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
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

    public LineResponse saveLine(LineRequest request) throws DuplicatedNameException, NoSuchElementException {
        if(isDuplicatedNameOfLine(request.getName())) {
            throw new DuplicatedNameException();
        }

        Station upStation = stationRepository.findById(request.getUpStationId()).orElseThrow(NoSuchElementException::new);
        Station downStation = stationRepository.findById(request.getDownStationId()).orElseThrow(NoSuchElementException::new);

        Line line = new Line(request.getName(), request.getColor());
        Section section = new Section(line, upStation, downStation, request.getDistance());
        line.getSections().add(section);
        lineRepository.save(line);
        return createLineResponse(line);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAllLines() {
        List<Line> lines = lineRepository.findAll();
        return lines.stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    private LineResponse createLineResponse(Line line) {
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                line.getCreatedDate(),
                line.getModifiedDate()
        );
    }

    @Transactional(readOnly = true)
    public LineResponse findLine(Long id) {
        Line line = lineRepository.findById(id).orElseThrow(NoSuchElementException::new);
        return createLineResponse(line);
    }

    public LineResponse updateLine(Long id, LineRequest request) {
        Line line = lineRepository.findById(id).orElseThrow(NoSuchElementException::new);
        line.updateLine(request.getName(), request.getColor());
        return createLineResponse(line);
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public boolean isDuplicatedNameOfLine(String name) {
        Optional<Line> line = lineRepository.findByName(name);
        return line.isPresent();
    }
}
