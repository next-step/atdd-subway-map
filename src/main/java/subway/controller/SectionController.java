package subway.controller;

import org.hibernate.annotations.common.util.impl.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.SectionRequest;
import subway.dto.SectionResponse;
import subway.service.LineService;
import subway.service.SectionService;

import java.net.URI;


@RestController
@RequestMapping("/sections")
public class SectionController {
    private SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping()
    public ResponseEntity<SectionResponse> createLine(@RequestBody SectionRequest sectionRequest) throws Exception{
        SectionResponse line = sectionService.saveSection(sectionRequest);
        return ResponseEntity.created(URI.create("/sections/" + line.getId())).body(line);
    }
}
