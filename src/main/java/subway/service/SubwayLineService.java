package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.entity.Station;
import subway.domain.entity.SubwayLine;
import subway.domain.request.SubwayLineRequest;
import subway.domain.response.StationResponse;
import subway.domain.response.SubwayLineResponse;
import subway.repository.StationRepository;
import subway.repository.SubwayLineRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class SubwayLineService {
    private SubwayLineRepository subwayLineRepository;
    private StationRepository stationRepository;

    public SubwayLineService(SubwayLineRepository subwayLineRepository, StationRepository stationRepository) {
        this.subwayLineRepository = subwayLineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public SubwayLineResponse saveSubwayLine(SubwayLineRequest request) {
        Station upStation = findStationById(request.getUpStationId());
        Station dwonStation = findStationById(request.getDownStationId());

        SubwayLine subwayLine = subwayLineRepository.save(new SubwayLine(request.getName(), request.getColor(), upStation, dwonStation, request.getDistance()));
        return createSubwayLineResponse(subwayLine);
    }

    public List<SubwayLineResponse> findAllSubwayLines() {
        return subwayLineRepository.findAll().stream()
                .map(this::createSubwayLineResponse)
                .collect(Collectors.toList());
    }

    public SubwayLineResponse findSubwayLineById(Long id) {
        SubwayLine subwayLine = subwayLineRepository.findById(id).get();

        return createSubwayLineResponse(subwayLine);

    }

    @Transactional
    public SubwayLineResponse updateSubwayLine(Long id, SubwayLineRequest request) {
        SubwayLine subwayLine = subwayLineRepository.findById(id).get();
        SubwayLine newSubwayLine = SubwayLine.builder()
                .id(subwayLine.getId())
                .name(request.getName())
                .color(request.getColor())
                .upStation(subwayLine.getUpStation())
                .downStation(subwayLine.getDownStation())
                .distance(subwayLine.getDistance())
                .build();

        SubwayLine updatedSubwayLine = subwayLineRepository.save(newSubwayLine);
        return createSubwayLineResponse(updatedSubwayLine);

    }

    @Transactional
    public void deleteSubwayLineById(Long id) {
        subwayLineRepository.deleteById(id);
    }

    public Station findStationById(Long id) {
        return this.stationRepository.findById(id).get();
    }

    private SubwayLineResponse createSubwayLineResponse(SubwayLine subwayLine) {
        Station upStation = findStationById(subwayLine.getUpStation().getId());
        Station downStation = findStationById(subwayLine.getDownStation().getId());

        return new SubwayLineResponse(
                subwayLine.getId(),
                subwayLine.getName(),
                subwayLine.getColor(),
                List.of(upStation, downStation),
                subwayLine.getDistance()
        );
    }
}
