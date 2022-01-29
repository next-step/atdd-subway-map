package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.dto.*;
import nextstep.subway.domain.*;
import nextstep.subway.exception.BadRequestException;
import nextstep.subway.exception.LineNotFoundException;
import nextstep.subway.exception.StationNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class LineService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineResponse saveLine(LineRequest request) {
        validateDuplicateLineName(request.getName());

        Station upStation = findStationById(request.getUpStationId());
        Station downStation = findStationById(request.getDownStationId());

        Line line = new Line(request.getName(), request.getColor());
        line.addSection(upStation, downStation, request.getDistance());
        lineRepository.save(line);

        return LineResponse.of(line);
    }

    private void validateDuplicateLineName(String name) {
        lineRepository.findByName(name).ifPresent(line -> {
            throw new BadRequestException("해당 노선은 이미 존재합니다.");
        });
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    public LineResponse findLine(Long id) {
        Line line = findLineById(id);
        return LineResponse.of(line);
    }

    public LineResponse updateLine(Long id, LineRequest request) {
        Line line = findLineById(id);
        line.update(request.getName(), request.getColor());
        return LineResponse.of(line);
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    public Line findLineById(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new LineNotFoundException(id));
    }

    public Station findStationById(Long id) {
        return stationRepository.findById(id)
                .orElseThrow(() -> new StationNotFoundException(id));
    }

    public void addSection(Long lineId, SectionRequest request) {
        Line line = findLineById(lineId);
        Station upStation = findStationById(request.getUpStationId());
        Station downStation = findStationById(request.getDownStationId());

        line.addSection(upStation, downStation, request.getDistance());
    }

    public void deleteSection(Long lineId, Station station) {
        Line line = findLineById(lineId);
        line.removeSection(station);
    }



}
