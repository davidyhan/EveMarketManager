package dhan.eve.marketmanager.evecentral.marketstat;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("restriction")
@XmlRootElement(name = "buy")
public class MSBuy {

    private int volume;

    private double averagePrice;

    private double maxPrice;

    private double minPrice;

    private double standardDeviation;

    private double medianPrice;

    private double percentile;

    public int getVolume() {
        return volume;
    }

    @XmlElement(name = "volume")
    public void setVolume(int volume) {
        this.volume = volume;
    }

    @XmlElement(name = "avg")
    public double getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(double averagePrice) {
        this.averagePrice = averagePrice;
    }

    public double getMaxPrice() {
        return maxPrice;
    }

    @XmlElement(name = "max")
    public void setMaxPrice(double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public double getMinPrice() {
        return minPrice;
    }

    @XmlElement(name = "min")
    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    public double getStandardDeviation() {
        return standardDeviation;
    }

    @XmlElement(name = "stddev")
    public void setStandardDeviation(double standardDeviation) {
        this.standardDeviation = standardDeviation;
    }

    public double getMedianPrice() {
        return medianPrice;
    }

    @XmlElement(name = "median")
    public void setMedianPrice(double medianPrice) {
        this.medianPrice = medianPrice;
    }

    public double getPercentile() {
        return percentile;
    }

    @XmlElement(name = "percentile")
    public void setPercentile(double percentile) {
        this.percentile = percentile;
    }

    public String toString() {
        //@formatter:off
        String toString = "Volume: " + volume + "\n" +
                          "Average Price: " + averagePrice + "\n" +
                          "Max Price: " + maxPrice + "\n" +
                          "Min Price: " + minPrice + "\n" +
                          "Median Price: " + medianPrice + "\n" +
                          "Percentile: " + percentile + "\n";
        //@formatter:on
        return toString;
    }

}
