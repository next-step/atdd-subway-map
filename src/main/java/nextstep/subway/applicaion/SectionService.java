package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.applicaion.dto.SectionResponse;
import nextstep.subway.domain.Line.Line;
import nextstep.subway.domain.Line.LineRepository;
import nextstep.subway.domain.section.Section;
import nextstep.subway.domain.section.SectionRepository;
import nextstep.subway.domain.station.Station;
import nextstep.subway.domain.station.StationRepository;
import nextstep.subway.exception.EntityNotFoundException;
import nextstep.subway.utils.ObjectMapUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class SectionService {

    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;
    private final LineRepository lineRepository;

    @Transactional
    public SectionResponse save(Long lineId, SectionRequest sectionRequest) {
        Line line = lineRepository.findById(lineId).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 노션입니다."));
        Station upStation = stationRepository.findById(sectionRequest.getUpStationId()).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 지하철 역입니다."));
        Station downStation = stationRepository.findById(sectionRequest.getDownStationId()).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 지하철 역입니다."));
        line.addSection(upStation, downStation, sectionRequest.getDistance());
        SectionResponse sectionResponse = ObjectMapUtils.map(line.getSections().lastSection(), SectionResponse.class);
        sectionResponse.addStation(line.getSections().lastSection());
        return sectionResponse;
    }

    public void delete(Long lineId, Long sectionDownStationId) {
        Station downStation = stationRepository.findById(sectionDownStationId).orElseThrow(() -> new EntityNotFoundException("station.not.found"));
        Line line = lineRepository.findById(lineId).orElseThrow(() -> new EntityNotFoundException("line.not.found"));
        line.validDeleteDownStation(downStation);
        deleteSection(line, downStation);
    }

    private void deleteSection(Line line, Station downStation) {
        for (Section section : line.getSections().getSections()) {
            if (Objects.equals(section, downStation)) {
                sectionRepository.delete(section);
            }
        }
    }
}
