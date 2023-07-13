package subway.linesection;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.exception.BadRequestException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LineSectionService {
    private final LineSectionRepository lineSectionRepository;

    public LineSectionService(LineSectionRepository lineSectionRepository) {
        this.lineSectionRepository = lineSectionRepository;
    }


    @Transactional
    public void createSection(Long lineId, Long upStationId, Long downStationId, Integer distance) {
        LineSection startSection = LineSection.ofFirst(lineId, upStationId, downStationId, distance);
        LineSection endSection = LineSection.of(lineId, upStationId, downStationId, distance);
        lineSectionRepository.saveAll(Arrays.asList(startSection, endSection));
    }

    @Transactional
    public void addSection(Long lineId, Long upStationId, Long downStationId, Integer distance) {
        LineSection endLineSection = getEndLineSectionById(lineId);
        LineSection newLineSection = LineSection.of(lineId, upStationId, downStationId, distance);
        validate(lineId, endLineSection, newLineSection);
        endLineSection.linkLineSection(newLineSection);
        lineSectionRepository.saveAll(Arrays.asList(endLineSection, newLineSection));
    }

    private void validate(Long lindId, LineSection endLineSection, LineSection newLineSection) {
        if (endLineSection.getCurrentStationId() != newLineSection.getPreviousStationId())
            throw new BadRequestException(String.format("line's endStationId > %d must be equal to lineSectionRequest's upStationId > %d", endLineSection.getCurrentStationId(), newLineSection.getPreviousStationId()));

        boolean hasStation = lineSectionRepository.findByLineId(lindId)
                .stream()
                .map(e -> e.getCurrentStationId())
                .anyMatch(e -> e.equals(newLineSection.getCurrentStationId()));

        if (hasStation)
            throw new BadRequestException(String.format("line's already has the station. stationId > %d", newLineSection.getCurrentStationId()));
    }

    private LineSection getEndLineSectionById(Long lineId) {
        return lineSectionRepository.findByLineIdAndNextStationIdIsNull(lineId).orElseThrow(() -> new RuntimeException(String.format("the line has no end line section. lineId > %d", lineId)));
    }

    public List<LineSection> findByLineId(Long lineId) {
        return lineSectionRepository.findByLineId(lineId);
    }
}
