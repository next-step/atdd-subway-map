package nextstep.subway.exception;

public class NonMatchLastStationException extends IllegalArgumentException {
    public NonMatchLastStationException(Message message) {
        super("non match last station with " + message.fieldName);
    }

    public enum Message {
        UP_STATION_ID("upStationId"),
        DOWN_STATION_ID("downStationId");

        private final String fieldName;

        Message(String fieldName) {
            this.fieldName = fieldName;
        }
    }
}
