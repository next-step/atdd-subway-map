package subway.lines;

import javax.persistence.*;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String color;

    private Long upStationId;

    private Long downStationId;

    private Integer distance;

    private Line() 
    {
        // not use
    }

    public Line(String name, String color, long upStationId, long downStationId, int distance)
    {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Long getId()
    {
        return this.id;
    }

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

    public void setName(String name)
    {
        this.name = name;
    }

    public void setColor(String color)
    {
        this.color = color;
    }

    public void setUpStationId(long upStationId)
    {
        this.upStationId = upStationId;
    }

    public void setDownStationId(long downStationId)
    {
        this.downStationId = downStationId;
    }

    public void setDistance(int distance)
    {
        this.distance = distance;
    }
}