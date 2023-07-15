package subway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import subway.dto.SectionResponse;
import subway.service.SectionService;
import subway.util.Mapper;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class SectionController {
    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @GetMapping(path = "/lines/{id}/sections")
    public List<SectionResponse> showSections(@PathVariable Long id) {
        return sectionService.findAllSectionsByLineId(id)
                .stream()
                .map(Mapper::toResponse)
                .collect(Collectors.toList());
    }
}
