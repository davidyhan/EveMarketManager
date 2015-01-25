package dhan.eve.marketmanager.evecentral.marketstat;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("restriction")
@XmlRootElement(name = "marketstat")
public class MarketStat {

    private List<Type> types;
    private int id;

    public int getId() {
        return id;
    }

    @XmlAttribute(name = "id")
    public void setId(int id) {
        this.id = id;
    }

    public List<Type> getTypes() {
        return types;
    }

    @XmlElement(name = "type")
    public void setTypes(List<Type> types) {
        this.types = types;
    }
    
    public String toString(){
        String toString = "";
        
        for(Type t : types){
            toString += t.toString();
        }
        
        return toString;
    }
}

