package nextstep.subway.linestation.ui;

import nextstep.subway.linestation.application.ListStationService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lines/{lineId}/stations")
public class LineStationController {

    private final ListStationService listStationService;

    public LineStationController(ListStationService listStationService) {
        this.listStationService = listStationService;
    }
}
