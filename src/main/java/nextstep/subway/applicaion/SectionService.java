package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.subway.domain.line.Line;
import nextstep.subway.domain.line.LineRepository;
import nextstep.subway.domain.line.Section;
import nextstep.subway.domain.line.SectionRepository;
import nextstep.subway.domain.station.Station;
import nextstep.subway.domain.station.StationRepository;
import nextstep.subway.exception.BadRequestException;
import nextstep.subway.exception.NotFoundException;
import nextstep.subway.ui.dto.SectionRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SectionService {

    private final LineRepository lineRepository;
    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;

    @Transactional
    public void createSection(Long lineId, SectionRequest sectionRequest) {
        Line line = lineRepository.findById(lineId)
                .orElseThrow(() -> new NotFoundException("line is not found"));

        Station newUpStation = stationRepository.findById(sectionRequest.getUpStationId())
                .orElseThrow(() -> new NotFoundException("station is not found"));

        Station newDownStation = stationRepository.findById(sectionRequest.getDownStationId())
                .orElseThrow(() -> new NotFoundException("station is not found"));


        // 1. 신규 상행선은 하행선에 값이 있어야 한다.
        boolean existDownStationByUpStation = sectionRepository.existsByLineAndDownStation(line, newUpStation);
        if (!existDownStationByUpStation) { // 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 한다.
            throw new BadRequestException("upStation required connected line downStation");
        }


        // 2. 신규 상행선은 상행선에 값이 있으면 안된다.
        boolean alreadyConnected = sectionRepository.existsByLineAndUpStation(line, newUpStation);
        if (alreadyConnected) {
            throw new BadRequestException("upStation is already connected");
        }

        // 3. 신규 하행선은 상행선 또는 하행선에 값이 있으면 안된다.
        boolean existNewDownStation = sectionRepository.existsByLineAndUpStationOrDownStation(line, newDownStation, newDownStation);
        if (existNewDownStation) {
            throw new BadRequestException("downStation is already connected");
        }

        sectionRepository.save(
                Section.builder()
                        .upStation(newUpStation)
                        .downStation(newDownStation)
                        .line(line)
                        .build()
        );

    }
}
