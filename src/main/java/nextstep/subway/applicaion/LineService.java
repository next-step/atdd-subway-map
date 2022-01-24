package nextstep.subway.applicaion;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import nextstep.subway.exception.DuplicatedElementException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            throw new DuplicatedElementException("중복된 지하철 라인 이름은 넣을 수 없습니다.");
        }

        Line line = lineRepository.save(request.toEntity());
        return LineResponse.of(line);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
            .map(LineResponse::of)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse findLine(Long lineId) {
        Line line = lineRepository.findById(lineId)
            .orElseThrow(NoSuchElementException::new);

        return LineResponse.of(line);
    }

    public LineResponse updateLine(Long lineId, LineRequest lineRequest) {
        Line savedLine = lineRepository.findById(lineId)
            .orElseThrow(NoSuchElementException::new);
        savedLine.update(lineRequest.toEntity());

        return LineResponse.of(savedLine);
    }

    public void deleteLine(Long lineId) {
        lineRepository.deleteById(lineId);
    }

    @Transactional(readOnly = true)
    public Line findLineById(Long id) {
        return lineRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    public void addLineStation(Long lineId, SectionRequest sectionRequest) {
        Line persistLine = findLineById(lineId);
        Station upStation = stationRepository.findById(sectionRequest.getUpStationId())
            .orElseThrow(NoSuchElementException::new);
        Station downStation = stationRepository.findById(sectionRequest.getDownStationId())
            .orElseThrow(NoSuchElementException::new);
        Section newSection = Section.of(persistLine, upStation, downStation, sectionRequest.getDistance());

        persistLine.addSection(newSection);
    }

    public void removeLineStation(Long lineId, Long stationId) {

    }
}
