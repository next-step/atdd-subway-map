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

    public SubwayLineResponse findSubwayLineById(Long id) {
        SubwayLine subwayLine = subwayLineRepository.findById(id).stream()
                .findFirst().get();

        return SubwayLineResponse.createResponseByEntity(subwayLine);

    }

    @Transactional
    public SubwayLineResponse updateSubwayLine(Long id, SubwayLineRequest request) {
        SubwayLine subwayLine = subwayLineRepository.findById(id).stream().findFirst().get();
        SubwayLine newSubwayLine = SubwayLine.builder()
                .id(subwayLine.getId())
                .name(request.getName())
                .color(request.getColor())
                .upStation(subwayLine.getUpStation())
                .downStation(subwayLine.getDownStation())
                .distance(subwayLine.getDistance())
                .build();

        SubwayLine savedSubwayLine = subwayLineRepository.save(newSubwayLine);
        return createSubwayLineResponse(savedSubwayLine);

    }

    @Transactional
    public void deleteSubwayLineById(Long id) {
        subwayLineRepository.deleteById(id);
    }

    private SubwayLineResponse createSubwayLineResponse(SubwayLine subwayLine) {
        return SubwayLineResponse.createResponseByEntity(subwayLine);
    }

    public Station findStatinoById(Long id) {
        return stationRepository.findById(id).stream()
                .findFirst().orElseThrow(() -> new IllegalArgumentException("존재하지 않는 역입니다."));
    }


}
