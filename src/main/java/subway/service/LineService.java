package subway.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.common.DomainException;
import subway.common.DomainExceptionType;
import subway.domain.Line;
import subway.controller.line.dto.CreateLineRequest;
import subway.controller.line.dto.LineResponse;
import subway.controller.line.dto.UpdateLineRequest;
import subway.repository.LineRepository;
import subway.controller.line.dto.AddSectionRequest;
import subway.domain.Section;
import subway.repository.SectionRepository;
import subway.domain.Station;
import subway.repository.StationRepository;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;

    @Transactional
    public LineResponse saveLine(CreateLineRequest request) {
        Line line = lineRepository.save(request.toEntity());

        Optional<Section> section = sectionRepository.findByDownStationId(line.getDownStationId());

        if (section.isPresent()) {
            return LineResponse.entityToResponse(line);
        }

        Station upStation = findStation(line.getUpStationId());
        Station downStation = findStation(line.getDownStationId());


        line.addSection(
                new Section(
                        line.getId(), downStation, upStation, line.getLineDistance()));

        return LineResponse.entityToResponse(line);
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(LineResponse::entityToResponse)
                .collect(Collectors.toList());
    }

    public LineResponse findLineById(Long id) {
        return lineRepository.findById(id).map(LineResponse::entityToResponse).orElse(null);
    }

    @Transactional
    public void updateLineById(Long lineId, UpdateLineRequest request) {
        Line line = findLine(lineId);

        line.updateNameAndColor(request.getName(), request.getColor());
    }

    @Transactional
    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public void addSection(Long lineId, AddSectionRequest request) {
        Line line = findLine(lineId);

        Station upStation = findStation(request.getUpStationId());
        Station downStation = findStation(request.getDownStationId());

        line.addSection(new Section(lineId, upStation, downStation, request.getDistance()));
    }

    @Transactional
    public void deleteSection(Long lineId, Long stationId) {
        Line line = findLine(lineId);

        Station station = findStation(stationId);

        line.deleteSection(station);
    }

    private Station findStation(Long stationId){
        return stationRepository.findById(stationId).orElseThrow(() -> new DomainException(DomainExceptionType.NO_STATION));
    }

    private Line findLine(Long lineId){
        return lineRepository.findById(lineId).orElseThrow(() -> new DomainException(DomainExceptionType.NO_LINE));
    }
}
