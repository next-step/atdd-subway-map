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

        Optional<Station> downStation = stationRepository.findById(line.getDownStationId());
        Optional<Station> upStation = stationRepository.findById(line.getUpStationId());

        if (downStation.isEmpty() || upStation.isEmpty())
            throw new DomainException(DomainExceptionType.NO_STATION);

        line.addSection(
                new Section(
                        line.getId(), downStation.get(), upStation.get(), line.getLineDistance()));

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
    public void updateLineById(Long id, UpdateLineRequest request) {
        Line line =
                lineRepository
                        .findById(id)
                        .orElseThrow(() -> new DomainException(DomainExceptionType.NO_LINE));

        line.updateNameAndColor(request.getName(), request.getColor());
    }

    @Transactional
    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public void addSection(Long lineId, AddSectionRequest request) {
        Line line =
                lineRepository
                        .findById(lineId)
                        .orElseThrow(() -> new DomainException(DomainExceptionType.NO_LINE));

        Optional<Station> upStation = stationRepository.findById(request.getUpStationId());
        Optional<Station> downStation = stationRepository.findById(request.getDownStationId());

        // 새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 한다.
        if (upStation.get().getId() != line.getDownStationId())
            throw new DomainException(DomainExceptionType.UPDOWN_STATION_MISS_MATCH);

        // 새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없다.
        if (line.getStationList().contains(downStation.get()))
            throw new DomainException(DomainExceptionType.DOWN_STATION_EXIST_IN_LINE);

        if (upStation.isEmpty() || downStation.isEmpty())
            throw new DomainException(DomainExceptionType.NO_STATION);

        line.addSection(
                new Section(lineId, upStation.get(), downStation.get(), request.getDistance()));
    }

    @Transactional
    public void deleteSection(Long lineId, Long stationId) {
        Line line =
                lineRepository
                        .findById(lineId)
                        .orElseThrow(() -> new DomainException(DomainExceptionType.NO_LINE));

        if (line.getDownStationId() != stationId)
            throw new DomainException(DomainExceptionType.NOT_DOWN_STATION);

        if (line.getSectionCount() == 1)
            throw new DomainException(DomainExceptionType.CANT_DELETE_SECTION);

        Optional<Station> station = stationRepository.findById(stationId);

        if (station.isEmpty()) throw new DomainException(DomainExceptionType.NO_STATION);

        line.deleteSection(station.get());
    }
}
