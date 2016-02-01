package quicklook;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "evec_api")
public class EveCentralApi {

    private QuickLook quick;

    public QuickLook getQuick() {
        return quick;
    }

    @XmlElement(name = "quicklook")
    public void setQuick(QuickLook quickLook) {
        this.quick = quickLook;
    }

    public String toString() {
        return quick.toString();
    }

}
