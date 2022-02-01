package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.applicaion.dto.SectionResponse;
import nextstep.subway.applicaion.exception.NotFoundException;
import nextstep.subway.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SectionService {
    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;
    private final LineRepository lineRepository;

    public SectionService(SectionRepository sectionRepository,
                          StationRepository stationRepository,
                          LineRepository lineRepository) {
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
    }

    public SectionResponse saveSection(SectionRequest sectionRequest, long lineId) {
        final long downStationId = sectionRequest.getDownStationId();
        final long upStationId = sectionRequest.getUpStationId();

        checkExistingStation(downStationId, upStationId);

        final Station downStation = stationRepository.getById(downStationId);
        final Station upStation = stationRepository.getById(upStationId);

        final Line foundLine = lineRepository.getById(lineId);
        validateStationInSection(downStation, upStation, foundLine);

        final Section section = sectionRepository.save(
                new Section(
                        foundLine,
                        downStation,
                        upStation,
                        sectionRequest.getDistance()
                        ));

        return new SectionResponse(section.getId(),
                section.getLine().getId(),
                section.getDownStation().getId(),
                section.getUpStation().getId(),
                section.getDistance(),
                section.getCreatedDate(),
                section.getModifiedDate()
        );
    }

    private void validateStationInSection(Station downStation, Station upStation, Line foundLine) {
        final List<Section> sections = foundLine.getSections();
        if (!sections.isEmpty()) {
            checkUpStation(upStation, sections);
            checkDownStation(downStation, sections);
        }
    }

    private void checkDownStation(Station downStation, List<Section> sections) {
        if (downStation.equals(sections.get(0).getUpStation())
        || downStation.equals(sections.get(0).getDownStation())) {
            throw new IllegalArgumentException("등록할 하행종점역은 노선에 등록되지 않은 역만 가능합니다.");
        }
    }

    private void checkUpStation(Station upStation, List<Section> sections) {
        if (upStation.equals(sections.get(0).getDownStation())) {
            throw new IllegalArgumentException("등록할 상행종점역은 노선의 하행종점역이어야 합니다.");
        }
    }

    private void checkExistingStation(long downStationId, long upStationId) {
        if (!stationRepository.existsById(downStationId)) {
            throw new NotFoundException(downStationId);
        }

        if (!stationRepository.existsById(upStationId)) {
            throw  new NotFoundException(upStationId);
        }
    }

//    public void deleteSection(long lineId, long stationId) {
//        final Line foundLine = lineRepository.getById(lineId);
//
//
//        if (!foundLine.getSections().get(0).getDownStation().equals()) {
//            throw new IllegalArgumentException("노선에 등록된 역(하행종점역)만 제거 가능합니다.");
//        }
//
////        foundLine.getStations(); // todo 길이 체크
////        // todo distance 체크
//    }
}
