package nextstep.subway.line.ui;

import java.net.URI;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nextstep.subway.line.application.LineSectionService;
import nextstep.subway.line.domain.dto.SectionRequest;

@RequestMapping(path = "/lines/{lineId}/sections", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class LineSectionController {
    private static final String URL_PATTERN = "/lines/%d/sections/%d";

    private final LineSectionService lineSectionService;

    public LineSectionController(LineSectionService lineSectionService) {
        this.lineSectionService = lineSectionService;
    }

    @PostMapping
    public ResponseEntity<Void> addSection(@PathVariable("lineId") final Long lineId,
                                           @RequestBody SectionRequest request) {
        Long createSectionId = lineSectionService.addSection(lineId, request);
        String location = String.format(URL_PATTERN, lineId, createSectionId);
        return ResponseEntity.created(URI.create(location)).build();
    }
}
