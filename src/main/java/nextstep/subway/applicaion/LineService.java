package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.domain.*;
import nextstep.subway.exception.DuplicateLineException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
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
            throw new DuplicateLineException();
        }

        Line line = lineRepository.save(request.toEntity());

        return LineResponse.ofLine(line);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> getLines() {
        List<Line> lines = lineRepository.findAll();

        return lines
            .stream()
            .map(LineResponse::ofLine)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse getLine(Long id) {
        Line line = getLineById(id);

        return LineResponse.ofLine(line);
    }

    public void editLine(Long id, LineRequest lineRequest) {
        Line line = getLineById(id);

        line.update(lineRequest.getName(), lineRequest.getColor());
    }

    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }

    public void createSection(Long id, SectionRequest sectionRequest) {
        Line line = getLineById(id);
        Station upStation = getStationById(sectionRequest.getUpStationId());
        Station downStation = getStationById(sectionRequest.getDownStationId());

        line.addSection(new Section(upStation, downStation, sectionRequest.getDistance()));
    }

    public void deleteSection(Long id, Long lastDownStationId) {
        Line line = getLineById(id);

        line.deleteSection(lastDownStationId);
    }

    private Line getLineById(Long id) {
        return lineRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("일치하는 라인이 없습니다."));
    }

    private Station getStationById(Long id) {
        return stationRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("일치하는 지하철역이 없습니다."));
    }
}
