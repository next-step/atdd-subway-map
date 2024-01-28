package subway.common;

public class Fixture {
    public static class Line {
        final String name;
        final String color;
        final String upStationId;
        final String downStationId;
        final String distance;

        private Line(String name, String color, String upStationId, String downStationId, String distance) {
            this.name = name;
            this.color = color;
            this.upStationId = upStationId;
            this.downStationId = downStationId;
            this.distance = distance;
        }

        public static Line 신분당선() {
            return new Line("신분당선", "bg-red-600", "1", "2", "10");
        }
    }
}
