package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.subwayLine.CreateSubwayLineRequest;
import nextstep.subway.applicaion.dto.subwayLine.SubwayLineResponse;
import nextstep.subway.domain.station.Station;
import nextstep.subway.domain.station.StationRepository;
import nextstep.subway.domain.subwayLine.SubwayLine;
import nextstep.subway.domain.subwayLine.SubwayLineRepository;
import nextstep.subway.domain.subwayLineColor.SubwayLineColor;
import nextstep.subway.domain.subwayLineColor.SubwayLineColorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class SubwayLineService {

    private StationRepository stationRepository;
    private SubwayLineRepository subwayLineRepository;
    private SubwayLineColorRepository subwayLineColorRepository;

    public SubwayLineService(
            StationRepository stationRepository,
            SubwayLineRepository subwayLineRepository,
            SubwayLineColorRepository subwayLineColorRepository) {
        this.stationRepository = stationRepository;
        this.subwayLineRepository = subwayLineRepository;
        this.subwayLineColorRepository = subwayLineColorRepository;
    }

    @Transactional
    public SubwayLineResponse saveSubwayLine(CreateSubwayLineRequest createSubwayLineRequest) {
        final Station upStation = getStationByIdIfExists(createSubwayLineRequest.getUpStationId());
        final Station downStation = getStationByIdIfExists(createSubwayLineRequest.getDownStationId());
        final SubwayLineColor subwayLineColor = getSubwayLineColorByCodeIfExists(createSubwayLineRequest.getColor());
        final SubwayLine savedSubwayLine = subwayLineRepository.save(new SubwayLine(
                createSubwayLineRequest.getName(),
                createSubwayLineRequest.getDistance(),
                subwayLineColor,
                upStation,
                downStation
        ));
        savedSubwayLine.updateStations(List.of(upStation, downStation));

        return new SubwayLineResponse(savedSubwayLine);
    }

    public List<SubwayLineResponse> findAllSubwayLines() {
        final List<SubwayLineResponse> findSubwayLines = subwayLineRepository.findAll()
                .stream()
                .map(SubwayLineResponse::new)
                .collect(Collectors.toList());

        return findSubwayLines;
    }

    private Station getStationByIdIfExists(Long stationId) {
        final Optional<Station> findStation = stationRepository.findById(stationId);
        if (findStation.isEmpty()) {
            throw new RuntimeException("존재하지 않는 지하철 역입니다.");
        }

        return findStation.get();
    }

    private SubwayLineColor getSubwayLineColorByCodeIfExists(String stationLineColorCode) {
        final Optional<SubwayLineColor> findSubwayLineColor = subwayLineColorRepository.findByCode(stationLineColorCode);
        if (findSubwayLineColor.isEmpty()) {
            throw new RuntimeException("사용할 수 없는 지하철 노선 색 코드입니다.");
        }

        return findSubwayLineColor.get();
    }
}
