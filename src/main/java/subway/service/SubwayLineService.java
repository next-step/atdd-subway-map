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
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class SubwayLineService {
    private SubwayLineRepository subwayLineRepository;
    private StationRepository stationRepository;

    public SubwayLineService(SubwayLineRepository subwayLineRepository) {
        this.subwayLineRepository = subwayLineRepository;
    }

    @Transactional
    public SubwayLineResponse saveSubwayLine(SubwayLineRequest request) {
        Station upStation = findStatinoById(request.getUpStationId());
        Station dwonStation = findStatinoById(request.getDownStationId());

        SubwayLine subwayLine = subwayLineRepository.save(new SubwayLine(request.getName(), request.getColor(), upStation, dwonStation, request.getDistance()));
        return createSubwayLineResponse(subwayLine);
    }

    public List<SubwayLineResponse> findAllSubwayLines() {
        return subwayLineRepository.findAll().stream()
                .map(this::createSubwayLineResponse)
                .collect(Collectors.toList());
    }

    public Station findStatinoById(Long id) {
        Station station = stationRepository.findById(id).stream()
                .findFirst().orElseThrow(() -> new IllegalArgumentException("존재하지 않는 역입니다."));
        return station;
    }

    @Transactional
    public void deleteSubwayLineById(Long id) {
        subwayLineRepository.deleteById(id);
    }

    private SubwayLineResponse createSubwayLineResponse(SubwayLine subwayLine) {
        Station upStation = subwayLine.getUpStation();
        Station downStation = subwayLine.getDownStation();

        return new SubwayLineResponse(
                subwayLine.getId(),
                subwayLine.getName(),
                subwayLine.getColor(),
                List.of(new StationResponse(upStation.getId(), upStation.getName()),
                        new StationResponse(downStation.getId(), downStation.getName())
                )
        );
    }
}
