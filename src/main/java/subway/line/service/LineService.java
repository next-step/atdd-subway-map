package subway.line.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.exception.LineNotExistException;
import subway.line.repository.LineRepository;
import subway.line.repository.domain.Line;
import subway.line.repository.domain.Section;
import subway.line.service.dto.LineCreateRequest;
import subway.line.service.dto.LineResponse;
import subway.line.service.dto.LineUpdateRequest;
import subway.line.service.dto.SectionCreateRequest;
import subway.station.exception.StationNotExistException;
import subway.station.repository.StationRepository;
import subway.station.repository.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(final LineRepository lineRepository, final StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineResponse saveLine(final LineCreateRequest lineCreateRequest) {
        lineCreateRequest.validate();

        final Line line = lineRepository.save(
                new Line(lineCreateRequest.getName()
                        , lineCreateRequest.getColor()
                        , createSection(lineCreateRequest.toSectionCreateRequest())
                ));

        return LineResponse.from(line);
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAllWithLines().stream()
                .map(LineResponse::from)
                .collect(Collectors.toList());
    }

    public LineResponse findLineById(final Long id) {
        final Line line = lineRepository.findByIdWithSection(id).orElseThrow(() -> new LineNotExistException(id));
        return LineResponse.from(line);
    }

    @Transactional
    public void updateLine(final Long id, final LineUpdateRequest updateRequest) {
        updateRequest.validate();
        final Line line = findLine(id);
        line.changeName(updateRequest.getName());
        line.changeColor(updateRequest.getColor());
    }

    @Transactional
    public void deleteLine(final Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public void addSection(final Long lineId, final SectionCreateRequest createRequest) {
        createRequest.validate();
        final Section newSection = createSection(createRequest);

        final Line line = findLine(lineId);
        line.addSection(newSection);
    }

    private Section createSection(final SectionCreateRequest sectionCreateRequest) {
        final Station upStation = findStation(sectionCreateRequest.getUpStationId());
        final Station downStation = findStation(sectionCreateRequest.getDownStationId());
        return new Section(upStation, downStation, sectionCreateRequest.getDistance());
    }

    private Line findLine(final Long id) {
        return lineRepository.findByIdWithSection(id).orElseThrow(() -> new LineNotExistException(id));
    }

    private Station findStation(final Long stationId) {
        return stationRepository.findById(stationId).orElseThrow(() -> new StationNotExistException(stationId));
    }
}
