package EveApi;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("restriction")
@XmlRootElement(name = "row")
public class CharOrder {

    private String orderId;
    private String charId;
    private String stationId;
    private Integer volEntered;
    private Integer volRemaining;
    private Integer minVolume;
    private Integer orderState;
    private Integer typeId;
    private Double price;

    public String getOrderId() {
        return orderId;
    }

    @XmlAttribute(name = "orderID")
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCharId() {
        return charId;
    }

    @XmlAttribute(name = "charID")
    public void setCharId(String charId) {
        this.charId = charId;
    }

    public String getStationId() {
        return stationId;
    }

    @XmlAttribute(name = "stationId")
    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public Integer getVolEntered() {
        return volEntered;
    }

    @XmlAttribute(name = "volEntered")
    public void setVolEntered(Integer volEntered) {
        this.volEntered = volEntered;
    }

    public Integer getVolRemaining() {
        return volRemaining;
    }

    @XmlAttribute(name = "volRemaining")
    public void setVolRemaining(Integer volRemaining) {
        this.volRemaining = volRemaining;
    }

    public Integer getMinVolume() {
        return minVolume;
    }

    @XmlAttribute(name = "minVolume")
    public void setMinVolume(Integer minVolume) {
        this.minVolume = minVolume;
    }

    public Integer getOrderState() {
        return orderState;
    }

    @XmlAttribute(name = "orderState")
    public void setOrderState(Integer orderState) {
        this.orderState = orderState;
    }

    public Integer getTypeId() {
        return typeId;
    }

    @XmlAttribute(name = "typeID")
    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Double getPrice() {
        return price;
    }

    @XmlAttribute(name = "price")
    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "CharOrder [orderId=" + orderId + ", charId=" + charId + ", stationId=" + stationId + ", volEntered=" + volEntered + ", volRemaining="
                        + volRemaining + ", minVolume=" + minVolume + ", orderState=" + orderState + ", typeId=" + typeId + ", price=" + price + "]";
    }

}
