package model;

import java.util.List;
import java.util.Map;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Month {

    private List<Day> days;
    private String name;
    private Map<String, Integer> weeknames;

    public Month() {
    }

    public Month(List<Day> days, String name, Map<String, Integer> weeknames) {
        this.days = days;
        this.name = name;
        this.weeknames = weeknames;
    }

    @XmlElement(name = "day")
    public List<Day> getDays() {
        return days;
    }

    public void setDays(List<Day> days) {
        this.days = days;
    }

    @XmlAttribute
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Integer> getWeeknames() {
        return weeknames;
    }

    public void setWeeknames(Map<String, Integer> weeknames) {
        this.weeknames = weeknames;
    }

    @Override
    public String toString() {
        return String.format("%s: %s", name, days);
    }
}
