package subway.domain.line;

import subway.interfaces.line.dto.LineRequest;

public class LineCommand {
    public static class SectionAddCommand {
        private final Long lineId;
        private final Long upStationId;
        private final Long downStationId;
        private final Long distance;

        private SectionAddCommand(Long lineId, Long upStationId, Long downStationId, Long distance) {
            this.lineId = lineId;
            this.upStationId = upStationId;
            this.downStationId = downStationId;
            this.distance = distance;
        }

        public static SectionAddCommand of (Long lineId, LineRequest.Section request) {
            return new SectionAddCommand(lineId, request.getUpStationId(), request.getDownStationId(), request.getDistance());
        }

        public Long getLineId() {
            return lineId;
        }

        public Long getUpStationId() {
            return upStationId;
        }

        public Long getDownStationId() {
            return downStationId;
        }

        public Long getDistance() {
            return distance;
        }
    }
}
