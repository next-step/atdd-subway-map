package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.entity.Line;
import subway.domain.entity.Section;
import subway.domain.entity.Station;
import subway.domain.request.LineRequest;
import subway.domain.request.SectionRequest;
import subway.domain.response.LineResponse;
import subway.exception.ApplicationException;
import subway.exception.ExceptionMessage;
import subway.repository.LineRepository;
import subway.repository.SectionRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {
    private final StationService stationService;
    private final LineRepository lineRepository;
    private final SectionRepository sectionRepository;

    public LineService(LineRepository lineRepository, StationService stationService, SectionRepository sectionRepository) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
        this.sectionRepository = sectionRepository;
    }

    @Transactional
    public LineResponse saveLine(LineRequest request) {
        Station upStation = findStationById(request.getUpStationId());
        Station dwonStation = findStationById(request.getDownStationId());

        Line line = lineRepository.save(new Line(request.getName(), request.getColor(), upStation, dwonStation, request.getDistance()));
        Section section = sectionRepository.save(new Section(line, upStation, dwonStation, request.getDistance()));
        return createLineResponse(line);
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    public LineResponse findLineById(Long id) {
        Line line = lineRepository.findById(id).get();
        return createLineResponse(line);
     }

    @Transactional
    public LineResponse updateLine(Long id, LineRequest request) {
        Line line = lineRepository.findById(id).get();

        // Todo
        Line newLine = new Line(line.getId(), request.getName(), request.getColor(), line.getUpStation(), line.getDownStation(), line.getDistance());

        Line updatedLine = lineRepository.save(newLine);
        return createLineResponse(updatedLine);
    }

    @Transactional
    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    public Station findStationById(Long id) {
        return this.stationService.findById(id);
    }

    private LineResponse createLineResponse(Line line) {
        Station upStation = findStationById(line.getUpStation().getId());
        Station downStation = findStationById(line.getDownStation().getId());

        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                List.of(upStation, downStation),
                line.getDistance()
        );
    }

    @Transactional
    public void addSection(Long lineId, SectionRequest sectionRequest) {
        Line line = lineRepository.findById(lineId).get();
        Station upStation = stationService.findById(sectionRequest.getUpStationId());
        Station downStation = stationService.findById(sectionRequest.getDownStationId());

        // 새로운 구간의 상행역이 등록된 노선의 하행 종점역이 아니면 에러
        List<Section> sections = line.getSections();
        Section section = sections.get(sections.size() - 1);
        if (!section.getDownStation().equals(upStation)) {
            throw new ApplicationException(ExceptionMessage.UPSTATION_VALIDATION_EXCEPTION.getMessage());
        }

        // 새로운 구간의 하행역이 노선에 등록되어있는 역과 같으면 에러
        isRegisteredStation(sections, downStation);

        // 새로운 구간의 상행역과 하행역이 같으면 에러
        if (sectionRequest.getUpStationId().equals(sectionRequest.getDownStationId())) {
            throw new ApplicationException(ExceptionMessage.NEW_SECTION_VALIDATION_EXCEPTION.getMessage());
        }

        line.addSection(upStation, downStation, sectionRequest.getDistance());
    }

    private void isRegisteredStation(List<Section> sections, Station downStation) {
        for (Section section : sections) {
            if(section.getUpStation().equals(downStation)){
                throw new ApplicationException(ExceptionMessage.DOWNSTATION_VALIDATION_EXCEPTION.getMessage());
            }
        }
    }

}
