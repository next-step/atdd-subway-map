package subway.domain.contract;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class LineCommand {
    @Getter
    @AllArgsConstructor
    public static class CreateLine {
        private String name;
        private String color;
        private Long upStationId;
        private Long downStationId;
        private Long distance;
    }
}
