package subway.subwayline;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.Station;
import subway.station.StationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SubwayLineService {

    private final SubwayLineRepository subwayLineRepository;
    private final StationRepository stationRepository;

    public SubwayLineService(SubwayLineRepository subwayLineRepository, StationRepository stationRepository) {
        this.subwayLineRepository = subwayLineRepository;
        this.stationRepository = stationRepository;
    }

    public SubwayLineDto createSubwayLine(SubwayLineDto dto) {
        Station upStation = stationRepository.findById(dto.getUpStationId())
                .orElseThrow(() -> new IllegalArgumentException("상행역이 존재하지 않습니다."));
        Station downStation = stationRepository.findById(dto.getDownStationId())
                .orElseThrow(() -> new IllegalArgumentException("하행역 존재하지 않습니다."));
        SubwayLine savedSubWayLine = subwayLineRepository.save(dto.toEntity(upStation, downStation));

        return SubwayLineDto.of(savedSubWayLine);
    }

    @Transactional(readOnly = true)
    public List<SubwayLineDto> getSubwayLines() {

        return subwayLineRepository.findAll().stream()
                .map(SubwayLineDto::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SubwayLineDto getSubwayLine(Long id) {
        SubwayLine subwayLine = subwayLineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("노선이 존재하지 않습니다."));
        return SubwayLineDto.of(subwayLine);
    }

    public void modifySubwayLine(Long id, ModifySubwayLineRequest request) {
        SubwayLine subwayLine = subwayLineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("노선이 존재하지 않습니다."));
        subwayLine.modifySubwayLine(request.getName(), request.getColor());
    }


    public void deleteSubwayLine(Long id) {
        SubwayLine subwayLine = subwayLineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("노선이 존재하지 않습니다."));
        subwayLineRepository.delete(subwayLine);
    }
}
