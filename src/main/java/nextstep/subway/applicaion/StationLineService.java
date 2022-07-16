package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.applicaion.dto.SectionResponse;
import nextstep.subway.applicaion.dto.StationLineRequest;
import nextstep.subway.applicaion.dto.StationLineResponse;
import nextstep.subway.applicaion.dto.StationRequest;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.constant.Message;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.SectionRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationLine;
import nextstep.subway.domain.StationLineRepository;
import nextstep.subway.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class StationLineService {
    private final StationLineRepository stationLineRepository;
    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;

    @Transactional
    public StationLineResponse createStationLine(StationLineRequest stationLineRequest) {
        List<StationResponse> stations = findByUpStationAndDownStation(stationLineRequest.getUpStationId(),
            stationLineRequest.getDownStationId());

        StationLine stationLine = stationLineRepository.save(stationLineRequest.toEntity());

        sectionRepository.save(Section.builder()
                .stationLine(stationLine)
                .upStation(findByStationById(stationLineRequest.getUpStationId()))
                .downStation(findByStationById(stationLineRequest.getDownStationId()))
                .distance(stationLineRequest.getDistance())
                .build());

        return StationLineResponse.of(stationLine, stations);
    }

    public List<StationLineResponse> findAllStationLine() {
        return stationLineRepository.findAll()
                .stream()
                .map(stationLine -> StationLineResponse.of(stationLine,
                    findByUpStationAndDownStation(stationLine.getUpStationId(),
                        stationLine.getDownStationId())))
                .collect(Collectors.toList());
    }

    public StationLineResponse findByStationLineId(Long stationLineId) {
        StationLine stationLine = findByStationLine(stationLineId);
        return StationLineResponse.of(stationLine, findByUpStationAndDownStation(stationLine.getUpStationId(), stationLine.getDownStationId()));
    }

    @Transactional
    public void updateByStationLineId(Long stationLineId, StationLineRequest stationLineRequest) {
        StationLine stationLine = findByStationLine(stationLineId);
        stationLine.update(stationLineRequest.getName(), stationLineRequest.getColor());
    }

    @Transactional
    public void deleteByStationLineId(Long stationLineId) {
        stationLineRepository.deleteById(stationLineId);
    }

    private StationLine findByStationLine(Long stationLineId) {
        return stationLineRepository.findById(stationLineId).get();
    }

    @Transactional
    public SectionResponse createSection(Long id, SectionRequest sectionRequest) {
        StationLine stationLine = findByStationLine(id);

        Station upStation = findByStationById(sectionRequest.getUpStationId());
        Station downStation = findByStationById(sectionRequest.getDownStationId());

        Section section = stationLine.addSection(upStation, downStation, sectionRequest.getDistance());
        return SectionResponse.of(sectionRepository.save(section));
    }

    @Transactional
    public Section deleteSection(Long id, Long stationId) {
        StationLine stationLine = findByStationLine(id);

        return stationLine.removeSection(stationId);
    }

    private Station findByStationById(Long stationId) {
        return stationRepository.findById(stationId)
            .orElseThrow(() -> new IllegalArgumentException(Message.NOT_FOUND_STATION.getValue()));
    }

    private List<StationResponse> findByUpStationAndDownStation(Long upStationLineId, Long downStationLineId) {
        return stationRepository.findAllById(List.of(upStationLineId, downStationLineId))
                .stream()
                .map(station -> StationResponse.of(station))
                .collect(Collectors.toList());
    }


}
