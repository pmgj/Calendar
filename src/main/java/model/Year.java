package model;

import java.util.List;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Year {
    private List<Month> months;

    public Year(List<Month> months) {
        this.months = months;
    }
    
    public Year() {
    }    

    @XmlElement(name="month")
    public List<Month> getMonths() {
        return months;
    }

    public void setMonths(List<Month> months) {
        this.months = months;
    }
}
