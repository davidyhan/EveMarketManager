package EveApi;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "result")
public class Result {

    private RowSet rowset;

    public RowSet getRowset() {
        return rowset;
    }

    @XmlElement(name = "rowset")
    public void setRowset(RowSet rowset) {
        this.rowset = rowset;
    }

	@Override
	public String toString() {
		return rowset.toString();
	}

}
