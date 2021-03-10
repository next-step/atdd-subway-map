package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.line.exception.CreateSectionWithWrongUpStationException;
import nextstep.subway.line.exception.DeleteSectionWithNotLastException;
import nextstep.subway.line.exception.DeleteSectionWithOnlyOneException;
import nextstep.subway.line.exception.DownStationDuplicatedException;
import nextstep.subway.line.exception.LineNameDuplicatedException;
import nextstep.subway.line.exception.LineNotFoundException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.exception.StationNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public LineResponse saveLine(LineRequest lineRequest) {
        if (lineRepository.existsByName(lineRequest.getName())) {
            throw new LineNameDuplicatedException(lineRequest.getName());
        }

        Station upStation = getStationById(lineRequest.getUpStationId());
        Station downStation = getStationById(lineRequest.getDownStationId());
        Line persistLine = lineRepository.save(lineRequest.toLine(upStation, downStation));

        return LineResponse.of(persistLine);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> getLines() {
        return lineRepository.findAll()
                .stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse getLine(Long id) {
        return lineRepository.findById(id)
                .map(LineResponse::of)
                .orElseThrow(() -> new LineNotFoundException(id));
    }

    public void updateLine(Long id, LineRequest lineRequest) {
        Line line = lineRepository.findById(id).orElseThrow(() -> new LineNotFoundException(id));
        Station upStation = getStationById(lineRequest.getUpStationId());
        Station downStation = getStationById(lineRequest.getDownStationId());

        line.update(lineRequest.toLine(upStation, downStation));
    }

    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }

    public LineResponse saveSection(Long lineId, SectionRequest sectionRequest) {
        Line line = getLineById(lineId);

        if (isWrongUpStation(line, sectionRequest)) {
            throw new CreateSectionWithWrongUpStationException();
        }
        if (idDownStationDuplicated(line, sectionRequest)) {
            throw new DownStationDuplicatedException();
        }

        Station upStation = getStationById(sectionRequest.getUpStationId());
        Station downStation = getStationById(sectionRequest.getDownStationId());
        Section section = sectionRequest.toSection(upStation, downStation);
        line.addSection(section);
        lineRepository.flush();

        return LineResponse.of(line);
    }

    public void deleteSection(Long lineId, Long sectionId) {
        Line line = getLineById(lineId);

        if (isNotLastSection(line, sectionId)) {
            throw new DeleteSectionWithNotLastException();
        }
        if (isOnlyOneSection(line)) {
            throw new DeleteSectionWithOnlyOneException();
        }

        line.getSections()
                .removeIf(it -> it.getId().equals(lineId));
    }

    private Station getStationById(Long id) {
        return stationRepository.findById(id)
                .orElseThrow(() -> new StationNotFoundException(id));
    }

    private Line getLineById(Long lineId) {
        return lineRepository.findById(lineId)
                .orElseThrow(() -> new LineNotFoundException(lineId));
    }

    private boolean isWrongUpStation(Line line, SectionRequest sectionRequest) {
        return !line.getSections().isEmpty()
                && !line.getLastStationId().equals(sectionRequest.getUpStationId());
    }

    private boolean idDownStationDuplicated(Line line, SectionRequest sectionRequest) {
        List<Long> stationIds = line.getStations()
                .stream()
                .map(Station::getId)
                .collect(Collectors.toList());
        return stationIds.contains(sectionRequest.getDownStationId());
    }

    private boolean isNotLastSection(Line line, Long sectionId) {
        return !line.getLastSectionId().equals(sectionId);
    }

    private boolean isOnlyOneSection(Line line) {
        return line.getSections().size() == 1;
    }
}
