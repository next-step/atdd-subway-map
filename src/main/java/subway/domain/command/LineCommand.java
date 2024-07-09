package subway.domain.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

public class LineCommand {
    @ToString
    @Getter
    @AllArgsConstructor
    public static class CreateLine {
        private String name;
        private String color;
        private Long upStationId;
        private Long downStationId;
        private Long distance;
    }

    @ToString
    @Getter
    @AllArgsConstructor
    public static class UpdateLine {
        private Long id;
        private String name;
        private String color;
        private Long upStationId;
        private Long downStationId;
        private Long distance;
    }
}
