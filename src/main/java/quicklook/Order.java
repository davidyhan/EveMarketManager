package quicklook;

import java.util.Comparator;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("restriction")
@XmlRootElement
public class Order implements Comparable<Order>{

    Integer id;

    Integer regionId;
    Integer stationId;
    String stationName;
    Integer range;
    Double price;
    Double security;
    Integer volumeRemaining;
    Integer MinimumVolume;
    String expiration;
    String reportedTime;

    public Integer getRegion() {
        return regionId;
    }

    @XmlElement
    public void setRegion(Integer region) {
        this.regionId = region;
    }

    public Integer getStation() {
        return stationId;
    }

    @XmlElement
    public void setStation(Integer stationId) {
        this.stationId = stationId;
    }

    public String getStationName() {
        return stationName;
    }

    @XmlElement(name = "station_name")
    public void setStationName(String station_name) {
        this.stationName = station_name;
    }

    public Double getPrice() {
        return price;
    }

    @XmlElement
    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getRange() {
        return range;
    }

    @XmlElement
    public void setRange(Integer range) {
        this.range = range;
    }

    public Integer getVolumeRemaining() {
        return volumeRemaining;
    }

    @XmlElement(name = "vol_remain")
    public void setVolumeRemaining(Integer volumeRemaining) {
        this.volumeRemaining = volumeRemaining;
    }

    public Integer getMinimumVolume() {
        return MinimumVolume;
    }

    @XmlElement(name = "min_volume")
    public void setMinimumVolume(Integer minimumVolume) {
        MinimumVolume = minimumVolume;
    }

    public String getExpiration() {
        return expiration;
    }

    @XmlElement(name = "expires")
    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public String getReportedTime() {
        return reportedTime;
    }

    @XmlElement(name = "expires")
    public void setReportedTime(String reportedTime) {
        this.reportedTime = reportedTime;
    }

    public Double getSecurity() {
        return security;
    }

    @XmlElement(name = "security")
    public void setSecurity(Double security) {
        this.security = security;
    }

    public Integer getId() {
        return id;
    }

    @XmlAttribute(name = "id")
    public void setId(Integer id) {
        this.id = id;
    }

    public String toString() {
        //@formatter:off
        String toString = "Order Id: " + id + "\n" + 
                          "Region Id: " + regionId + "\n" + 
                          "Station Id: " + stationId + "\n" + 
                          "Station Name: " + stationName+ "\n" + 
                          "Range: " + range + "\n" + 
                          "Price: " + price + "\n" + 
                          "Securtiy: " + security + "\n" + 
                          "Volume Remaining: " + volumeRemaining+ "\n" + 
                          "Expiration: " + expiration + "\n";
        return toString;
        //@formatter:on
    }

    public int compareTo(Order o) {
        return this.getPrice().compareTo(o.getPrice());
    }
}
