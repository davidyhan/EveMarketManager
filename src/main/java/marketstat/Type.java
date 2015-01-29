package marketstat;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("restriction")
@XmlRootElement(name = "type")
public class Type {
    
    private int itemId;
    private MSBuy buyAverage;
    private MSSell sellAverage;

    public MSBuy getBuyAverage() {
        return buyAverage;
    }

    @XmlElement(name = "buy")
    public void setBuyAverage(MSBuy buyAverage) {
        this.buyAverage = buyAverage;
    }

    public MSSell getSellAverage() {
        return sellAverage;
    }

    @XmlElement(name = "sell")
    public void setSellAverage(MSSell sellAverage) {
        this.sellAverage = sellAverage;
    }
    
    @XmlAttribute(name = "id")
    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }
    
    public String toString(){
        String toString = "Type: id: "+itemId+"\n"+"Buy: \n"+buyAverage.toString() + "\n" + "Sell: \n"+ sellAverage.toString();
        
        return toString;
    }
    
}
