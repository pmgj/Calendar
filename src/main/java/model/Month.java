package model;

import java.util.List;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Month {

    private List<Day> days;
    private String name;
    private List<String> weeknames;

    public Month() {
    }

    public Month(List<Day> days, String name, List<String> weeknames) {
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

    public List<String> getWeeknames() {
        return weeknames;
    }

    public void setWeeknames(List<String> weeknames) {
        this.weeknames = weeknames;
    }

    @Override
    public String toString() {
        return String.format("%s: %s", name, days);
    }
}
