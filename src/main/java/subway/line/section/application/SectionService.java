package subway.line.section.application;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import subway.exception.CannotCreateSectionException;
import subway.exception.CannotDeleteSectionException;
import subway.exception.ErrorMessage;
import subway.exception.NotFoundException;
import subway.line.domain.Line;
import subway.line.domain.LineRepository;
import subway.line.section.domain.Section;
import subway.line.section.domain.SectionRepository;
import subway.line.section.dto.SectionRequest;
import subway.line.section.dto.SectionResponse;
import subway.station.domain.Station;
import subway.station.domain.StationRepository;
import subway.station.dto.StationResponse;

@Service
public class SectionService {
    private final SectionRepository sectionRepository;
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public SectionService(SectionRepository sectionRepository, LineRepository lineRepository,
        StationRepository stationRepository) {
        this.sectionRepository = sectionRepository;
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public SectionResponse createSection(Long lineId, SectionRequest sectionRequest) {
        Line line = lineRepository.findById(lineId)
            .orElseThrow(() -> new NotFoundException(ErrorMessage.LINE_NOT_FOUND));
        Station upStation = stationRepository.findById(sectionRequest.getUpStationId())
            .orElseThrow(() -> new NotFoundException(ErrorMessage.STATION_NOT_FOUND));
        Station downStation = stationRepository.findById(sectionRequest.getDownStationId())
            .orElseThrow(() -> new NotFoundException(ErrorMessage.STATION_NOT_FOUND));

        if (!Objects.equals(line.getDownStation(), upStation)) {
            throw new CannotCreateSectionException(
                ErrorMessage.UPSTATION_OF_NEW_SECTION_SHOULD_BE_DOWNSTATION_OF_THE_LINE);
        }
        if (line.hasStation(downStation)) {
            throw new CannotCreateSectionException(
                ErrorMessage.DOWNSTATION_OF_NEW_SECTION_SHOULD_NOT_BE_REGISTERED_THE_LINE);
        }

        Section section = sectionRepository.save(new Section(
            upStation,
            downStation,
            line, sectionRequest.getDistance()));

        line.addSection(section);
        line.changeDownStation(downStation);

        return createSectionResponse(section);
    }

    @Transactional
    public void deleteSection(Long lineId, Long stationId) {
        Line line = lineRepository.findById(lineId)
            .orElseThrow(() -> new NotFoundException(ErrorMessage.LINE_NOT_FOUND));
        Station station = stationRepository.findById(stationId)
            .orElseThrow(() -> new NotFoundException(ErrorMessage.STATION_NOT_FOUND));

        if (!Objects.equals(line.getDownStation(), station)) {
            throw new CannotDeleteSectionException(ErrorMessage.SHOULD_DELETE_ONLY_DOWNSTATION_OF_LINE);
        }

        if (line.getSections().size() != 1) {
            throw new CannotDeleteSectionException(ErrorMessage.CANNOT_DELETE_LINE_CONSIST_OF_ONE_SECTION);
        }

        Section deletedSection = sectionRepository.findByLineAndDownStation(line, station);

        line.changeDownStation(deletedSection.getUpStation());
        sectionRepository.delete(deletedSection);
    }

    private SectionResponse createSectionResponse(Section section) {
        return new SectionResponse(
            section.getId(),
            List.of(new StationResponse(section.getUpStation().getId(), section.getUpStation().getName()),
                new StationResponse(section.getDownStation().getId(), section.getDownStation().getName()))
        );
    }
}
