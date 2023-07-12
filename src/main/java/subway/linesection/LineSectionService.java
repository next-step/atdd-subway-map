package subway.linesection;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

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
        endLineSection.lineNextLineSection(newLineSection);
        lineSectionRepository.saveAll(Arrays.asList(endLineSection, newLineSection));
    }

    private LineSection getEndLineSectionById(Long lineId) {
        return lineSectionRepository.findByLineIdAndNextStationIdIsNull(lineId).orElseThrow(() -> new RuntimeException(String.format("the line has no end line section. lineId > %d", lineId)));
    }

    public List<LineSection> findByLineId(Long lineId) {
        return lineSectionRepository.findByLineId(lineId);
    }
}
