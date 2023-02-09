package subway.dto.request;

import lombok.Getter;

public class LineRequest {
    @Getter
    public static class Create {
        private String name;
        private String color;
        private Long upStationId;
        private Long downStationId;
        private Long distance;
    }

    @Getter
    public static class Update {
        private String name;
        private String color;
    }
}
