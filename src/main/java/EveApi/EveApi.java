package EveApi;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("restriction")
@XmlRootElement(name = "eveapi")
public class EveApi {

    private Result result;

    public Result getResult() {
        return result;
    }

    @XmlElement(name = "result")
    public void setResult(Result result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return result.toString();
    }

}
