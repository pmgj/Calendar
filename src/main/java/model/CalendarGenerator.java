package model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import jakarta.xml.bind.JAXBException;

public class CalendarGenerator {

    private final List<Holiday> movableHolidays = new ArrayList<>();
    private final List<Holiday> fixedHolidays;

    public CalendarGenerator() {
        fixedHolidays = this.createFixedHolidays();
    }

    private LocalDate easter(int year) {
        double a = year % 19;
        double b = Math.floor(year / 100);
        double c = year % 100;
        double d = Math.floor(b / 4);
        double e = b % 4;
        double f = Math.floor((b + 8) / 25);
        double g = Math.floor((b - f + 1) / 3);
        double h = (19 * a + b - d - g + 15) % 30;
        double i = Math.floor(c / 4);
        double k = c % 4;
        double l = (32 + 2 * e + 2 * i - h - k) % 7;
        double m = Math.floor((a + 11 * h + 22 * l) / 451);
        double p = Math.floor((h + l - 7 * m + 114) / 31);
        double q = (h + l - 7 * m + 114) % 31;
        LocalDate date = LocalDate.of(year, (int) p, (int) q + 1);
        while (date.getDayOfWeek() != DayOfWeek.SUNDAY) {
            date.plusDays(1);
        }
        return date;
    }

    private List<Holiday> createFixedHolidays() {
        List<Holiday> holidays = new ArrayList<>();
        holidays.add(new Holiday(1, 1, "Confraternização Universal"));
        holidays.add(new Holiday(6, 3, "Revolução Pernambucana"));
        holidays.add(new Holiday(21, 4, "Tiradentes"));
        holidays.add(new Holiday(1, 5, "Dia do Trabalho"));
        holidays.add(new Holiday(24, 6, "São João"));
        holidays.add(new Holiday(16, 7, "Nossa Senhora do Carmo"));
        holidays.add(new Holiday(7, 9, "Independência do Brasil"));
        holidays.add(new Holiday(12, 10, "Nossa Senhora Aparecida"));
        holidays.add(new Holiday(2, 11, "Finados"));
        holidays.add(new Holiday(15, 11, "Proclamação da República"));
        holidays.add(new Holiday(8, 12, "Nossa Senhora da Conceição"));
        holidays.add(new Holiday(25, 12, "Natal"));
        holidays.add(new Holiday(31, 12, "Ano Novo"));
        return holidays;
    }

    private void createMovableHolidays(int year) {
        movableHolidays.clear();
        LocalDate easter = easter(year);
        LocalDate holyFriday = easter.minusDays(2);
        movableHolidays.add(new Holiday(holyFriday.getDayOfMonth(), holyFriday.getMonthValue(),
                "Sexta-feira da Paixão"));
        LocalDate carnival = easter.minusDays(47);
        movableHolidays
                .add(new Holiday(carnival.getDayOfMonth(), carnival.getMonthValue(), "Carnaval"));
        LocalDate corpusChristi = easter.plusDays(60);
        movableHolidays.add(new Holiday(corpusChristi.getDayOfMonth(), corpusChristi.getMonthValue(),
                "Corpus Christi"));
    }

    private int numberOfDays(LocalDate date) {
        int[] numDays = new int[] { 31, date.isLeapYear() ? 29 : 28, 31, 30, 31, 30,
                31, 31, 30, 31, 30, 31 };
        return numDays[date.getMonthValue() - 1];
    }

    private Holiday getHoliday(int day, int month) {
        Predicate<Holiday> isHoliday = (element) -> element.getDay() == day && element.getMonth() == month;
        Stream<Holiday> sf = fixedHolidays.stream().filter(isHoliday);
        Stream<Holiday> sm = movableHolidays.stream().filter(isHoliday);
        Holiday f = sf.findFirst().orElse(null);
        if (f != null) {
            return f;
        }
        f = sm.findFirst().orElse(null);
        if (f != null) {
            return f;
        }
        return null;
    }

    private Month computeMonth2(int year, int month, Locale locale) {
        LocalDate date = LocalDate.of(year, month + 1, 1);
        date.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        List<Day> days = new ArrayList<>();
        for (int i = 0; i < 42; i++) {
            days.add(new Day(0));
        }
        for (int i = 0, j = date.getDayOfWeek().getValue() - 1; i < numberOfDays(date); i++, j++) {
            Holiday holiday = getHoliday(i + 1, date.getMonthValue());
            days.set(j, new Day(i + 1, holiday != null ? holiday.getMessage() : null));
        }
        String monthName = date.getMonth().getDisplayName(TextStyle.FULL, locale);
        List<String> daysOfWeekNames = Stream.of(DayOfWeek.values()).map(d -> d.getDisplayName(TextStyle.SHORT, locale))
                .collect(Collectors.toList());
        return new Month(days, monthName, daysOfWeekNames);
    }

    public Year computeMonth(int ano, int mes, Locale locale) {
        List<Month> calendar = new ArrayList<>();
        calendar.add(computeMonth2(ano, mes, locale));
        return new Year(calendar);
    }

    public Year computeYear(int year, Locale locale) {
        List<Month> calendar = new ArrayList<>();
        createMovableHolidays(year);
        for (int i = 0; i < 12; i++) {
            calendar.add(computeMonth2(year, i, locale));
        }
        return new Year(calendar);
    }

    public static void main(String[] args) throws JAXBException {
        CalendarGenerator c = new CalendarGenerator();
        c.createMovableHolidays(2022);
        System.out.println(c.movableHolidays);
        // Year ano = c.computeMonth(2022, 2, new Locale("pt"));

        // Year ano = c.computeYear(2022, new Locale("pt"));
        // ano.forEach(mes -> System.out.println(mes));
        // System.out.println(JsonbBuilder.create().toJson(ano));

        // JAXBContext jaxbContext = JAXBContext.newInstance(Year.class);
        // Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        // jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        // jaxbMarshaller.marshal(ano, System.out);
    }
}
