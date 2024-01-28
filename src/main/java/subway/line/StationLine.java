package subway.line;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class StationLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(length = 20, nullable = false)
    private String name;
    @Column(length = 20, nullable = false)
    private String color;
    @Column(nullable = false)
    private int upStationId;
    @Column(nullable = false)
    private int downStationId;
    @Column(nullable = false)
    private int distance;

    // 일단 하드 코딩으로 upStation과 downStation을 넣어놓고 커밋한 후, 엔티티로 변경하자.
    // 질문거리: 현업에서 실제로 Controller의 응답 형식을 ResponseEntity의 상태코드로 지정해주는 것인지?? (201, 205 등을 구체적으로 나누는 것인지, 혹은 200으로 그냥 통합하는지..? 난 항상 200만 사용했었는데)


    public StationLine(String name, String color, int upStationId, int downStationId, int distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public StationLine() {
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public int getUpStationId() {
        return upStationId;
    }

    public int getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "StationLine{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", upStationId=" + upStationId +
                ", downStationId=" + downStationId +
                ", distance=" + distance +
                '}';
    }

    public void updateStationLine(StationLineRequest stationLineRequest) {
        this.name = stationLineRequest.getName();
        this.color = stationLineRequest.getColor();
    }
}
