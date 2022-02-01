package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.applicaion.exception.DuplicateException;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    private final String NOT_EXIST_LINE = "존재하지 않는 노선 입니다.";
    private final String NOT_EXIST_STATION = "존재하지 않는 역 입니다.";

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public LineResponse saveLine(LineRequest lineRequest) {
        validateDuplicatedLine(lineRequest);

        Station upStation = stationRepository.findById(lineRequest.getUpStationId())
                .orElseThrow(() -> new EntityNotFoundException(NOT_EXIST_STATION));

        Station downStation = stationRepository.findById(lineRequest.getDownStationId())
                .orElseThrow(() -> new EntityNotFoundException(NOT_EXIST_STATION));

        Line line = lineRepository.save(Line.of(lineRequest.getName(), lineRequest.getColor(), upStation, downStation, lineRequest.getDistance()));

        return LineResponse.of(line);
    }

    private void validateDuplicatedLine(LineRequest lineRequest) {
        boolean existsLine = lineRepository.existsLineByName(lineRequest.getName());

        if (existsLine) {
            throw new DuplicateException("중복된 지하철 노선은 생성할 수 없습니다.");
        }
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAllLines() {
        List<Line> lines = lineRepository.findAll();

        return lines.stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse findLine(Long id) {
        Line line = lineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(NOT_EXIST_LINE));

        return LineResponse.of(line);
    }

    public void updateLine(Long id, LineRequest lineRequest) {
        Line line = lineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(NOT_EXIST_LINE));

        line.update(lineRequest.getName(), lineRequest.getColor());
    }

    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }

    public LineResponse saveSection(Long lineId, SectionRequest sectionRequest) {
        Line line = lineRepository.findById(lineId)
                .orElseThrow(() -> new EntityNotFoundException(NOT_EXIST_LINE));

        Station upStation = stationRepository.findById(sectionRequest.getUpStationId())
                .orElseThrow(() -> new EntityNotFoundException(NOT_EXIST_STATION));

        Station downStation = stationRepository.findById(sectionRequest.getDownStationId())
                .orElseThrow(() -> new EntityNotFoundException(NOT_EXIST_STATION));

        if (line.isNotEqualDownStation(upStation)) {
            throw new IllegalArgumentException("노선의 하행선이 구간의 상행선과 다릅니다.");
        }

        if (line.existStation(downStation)) {
            throw new IllegalArgumentException("해당 역은 이미 노선에 등록되어 있습니다.");
        }

        line.addSection(upStation, downStation, sectionRequest.getDistance());

        return LineResponse.of(line);
    }

    @Transactional(readOnly = true)
    public LineResponse findLineInAllSections(Long lineId) {
        Line line = lineRepository.findById(lineId)
                .orElseThrow(() -> new EntityNotFoundException(NOT_EXIST_LINE));

        return LineResponse.of(line);
    }

    public void deleteSection(Long lineId, Long stationId) {
        Line line = lineRepository.findById(lineId)
                .orElseThrow(() -> new EntityNotFoundException(NOT_EXIST_LINE));

        Station station = stationRepository.findById(stationId)
                .orElseThrow(() -> new EntityNotFoundException(NOT_EXIST_STATION));

        line.deleteSection(station);
    }
}
