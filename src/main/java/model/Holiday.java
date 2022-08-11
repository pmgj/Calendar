package model;

public class Holiday {

    private int day;
    private int month;
    private String message;

    public Holiday(int day, int month, String message) {
        this.day = day;
        this.month = month;
        this.message = message;
    }

    @Override
    public String toString() {
        return this.day + "/" + this.month + " (" + this.message + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Holiday) {
            Holiday x = (Holiday) o;
            return (this.day == x.day && this.month == x.month);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + this.day;
        hash = 37 * hash + this.month;
        return hash;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
