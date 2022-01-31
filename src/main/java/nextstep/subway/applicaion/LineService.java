package nextstep.subway.applicaion;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.applicaion.dto.SectionResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import nextstep.subway.exception.DuplicateStoreException;
import nextstep.subway.exception.NotFoundException;
import nextstep.subway.ui.ExceptionMessage;

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
        if(lineRepository.existsByName(request.getName())) {
            throw new DuplicateStoreException(ExceptionMessage.NOT_EXISTS_NOTION.getMessage());
        }

        Line line = new Line(request.getName(), request.getColor());
        Section section = createSection(line, request.getUpStationId(), request.getDownStationId(),
            request.getDistance());
        line.addSection(section);

        Line result = lineRepository.save(line);
        return LineResponse.of(result);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAllLines() {
        List<Line> lines = lineRepository.findAll();

        return lines.stream()
            .map(LineResponse::of)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse findLineById(Long id) {
        Line line = lineRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(ExceptionMessage.NOT_EXISTS_NOTION.getMessage()));
        return LineResponse.of(line);
    }

    public void updateLineById(Long id, LineRequest lineRequest) {
        Line line = lineRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(ExceptionMessage.NOT_EXISTS_NOTION.getMessage()));

        line.update(lineRequest.getName(), lineRequest.getColor());
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    public void addSection(Long lineId, SectionRequest sectionRequest) {
        Line line = lineRepository.findById(lineId)
            .orElseThrow(() -> new NotFoundException(ExceptionMessage.NOT_EXISTS_NOTION.getMessage()));

        Section section = createSection(line, sectionRequest.getUpStationId(), sectionRequest.getDownStationId(),
            sectionRequest.getDistance());

        line.addSection(section);
    }

    public void deleteSection(Long lineId, Long stationId) {
        Line line = lineRepository.findById(lineId)
            .orElseThrow(() -> new NotFoundException(ExceptionMessage.NOT_EXISTS_NOTION.getMessage()));

        Station station = findStationById(stationId);

        line.deleteSection(station);
    }

    private Section createSection(Line line, Long upStationId, Long downStationId, int distance) {
        Station upStation = findStationById(upStationId);
        Station downStation = findStationById(downStationId);

        return Section.of(line, upStation, downStation, distance);
    }

    private Station findStationById(Long stationId) {
        return stationRepository.findById(stationId)
            .orElseThrow(() -> new NotFoundException(ExceptionMessage.NOT_EXISTS_STATION.getMessage()));
    }
}
