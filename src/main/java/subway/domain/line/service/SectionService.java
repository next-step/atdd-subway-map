package subway.domain.line.service;

import subway.domain.line.repository.SectionRepository;
import subway.global.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.line.domain.Line;
import subway.domain.line.domain.Section;
import subway.domain.line.dto.request.CreateSectionRequest;
import subway.domain.line.dto.response.SectionResponse;
import subway.domain.line.repository.LineRepository;
import subway.domain.station.domain.Station;
import subway.domain.station.repository.StationRepository;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class SectionService {

    private final SectionRepository sectionRepository;
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    @Transactional
    public SectionResponse createSection(Long lineId, CreateSectionRequest request) {

        Line line = lineRepository.getLineById(lineId);

        if (!line.isStationDirectionEqual(request.getUpStationId())) {
            throw new GlobalException("새로운 구간의 상행역은 해당 노선의 하행 종점역과 일치해야합니다.");
        }

        if (!line.containsSectionByStation(request.getDownStationId())) {
            throw new GlobalException("이미 존재하는 역입니다.");
        }

        Section section = Section.create(
                line,
                getStation(request, "upStation"),
                getStation(request, "downStation"),
                request.getDistance()
        );

        Section savedSection = sectionRepository.save(section);

        return SectionResponse.from(savedSection);
    }

    private Station getStation(CreateSectionRequest request, String type) {
        if (type.equals("upStation")) {
            return stationRepository.getById(request.getUpStationId());
        }
        return stationRepository.getById(request.getDownStationId());
    }
}
