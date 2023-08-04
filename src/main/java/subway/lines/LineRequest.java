package subway.lines;

public class LineRequest {
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private Integer distance;

    public String getName()
    {
        return this.name;
    }

    public String getColor()
    {
        return this.color;
    }

    public Long getUpStationId()
    {
        return this.upStationId;
    }

    public Long getDownStationId()
    {
        return this.downStationId;
    }

    public Integer getDistance()
    {
        return this.distance;
    }
}
