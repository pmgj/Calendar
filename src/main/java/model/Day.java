package model;

import jakarta.xml.bind.annotation.XmlAttribute;

public class Day {

    private int day;
    private String holiday;

    @XmlAttribute(name = "n")
    public int getDay() {
        return day;
    }

    public void setDay(int dia) {
        this.day = dia;
    }

    @XmlAttribute
    public String getHoliday() {
        return holiday;
    }

    public void setHoliday(String mensagem) {
        this.holiday = mensagem;
    }

    public Day(int dia, String mensagem) {
        this.day = dia;
        this.holiday = mensagem;
    }

    public Day(int dia) {
        this.day = dia;
    }

    public Day() {
    }

    @Override
    public String toString() {
        return String.format("(%d, %s)", this.day, this.holiday);
    }
}
