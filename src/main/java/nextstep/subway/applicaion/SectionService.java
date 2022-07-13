package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.applicaion.dto.SectionResponse;
import nextstep.subway.domain.Line.Line;
import nextstep.subway.domain.Line.LineRepository;
import nextstep.subway.domain.section.Section;
import nextstep.subway.domain.section.SectionRepository;
import nextstep.subway.domain.station.Station;
import nextstep.subway.domain.station.StationRepository;
import nextstep.subway.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional(readOnly = true)
public class SectionService {

    private SectionRepository sectionRepository;
    private StationRepository stationRepository;
    private LineRepository lineRepository;

    public SectionService(SectionRepository sectionRepository, StationRepository stationRepository, LineRepository lineRepository) {
        this.lineRepository = lineRepository;
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public SectionResponse save(Long lineId, SectionRequest sectionRequest) {

        Line line = lineRepository.findById(lineId).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 노션입니다."));
        Station upStation = stationRepository.findById(sectionRequest.getUpStationId()).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 지하철 역입니다."));
        Station downStation = stationRepository.findById(sectionRequest.getDownStationId()).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 지하철 역입니다."));

        validSectionStation(line, upStation, downStation);

        Section section = new Section(upStation, downStation, sectionRequest.getDistance());
        return createLineResponse(sectionRepository.save(section));
    }

    private void validSectionStation(Line line, Station sectionUpStation, Station sectionDownStation) {
        validSectionUpStation(line, sectionUpStation);
        validSectionDownStation(line, sectionDownStation);
    }

    private void validSectionDownStation(Line line, Station sectionDownStation) {
        if (isLineUpStationDuplicated(line.getUpStation(), sectionDownStation) || isLineDownStationDuplicate(line.getDownStation(), sectionDownStation)) {
            throw new IllegalArgumentException("section.downStation.line.duplicate");
        }
    }

    private boolean isLineDownStationDuplicate(Station lineDownStation, Station sectionDownStation) {
        return Objects.equals(lineDownStation.getId(), sectionDownStation.getId());
    }

    private boolean isLineUpStationDuplicated(Station lineUpStation, Station sectionDownStation) {
        return Objects.equals(lineUpStation.getId(), sectionDownStation.getId());
    }

    private void validSectionUpStation(Line line, Station sectionUpStation) {
        if (!Objects.equals(line.getDownStation().getId(), sectionUpStation.getId())) {
            throw new IllegalArgumentException("section.upStation.line.downStation");
        }
    }

    private SectionResponse createLineResponse(Section section) {
        return new SectionResponse(section.getId(), section.getUpStation().getId(), section.getDownStation().getId(), section.getDistance());
    }

}
