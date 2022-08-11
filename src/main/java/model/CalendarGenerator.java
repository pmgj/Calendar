package model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;
import jakarta.json.bind.JsonbBuilder;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;

public class CalendarGenerator {

    private final List<Holiday> movableHolidays = new ArrayList<>();
    private final List<Holiday> fixedHolidays;

    public CalendarGenerator() {
        fixedHolidays = this.createFixedHolidays();
    }

    private Calendar easter(int year) {
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
        Calendar cal = new GregorianCalendar();
        cal.set(year, (int) p - 1, (int) q + 1);
        for (int x = cal.get(Calendar.DAY_OF_MONTH); cal.get(Calendar.DAY_OF_WEEK) != 1; x++) {
            cal.set(Calendar.DAY_OF_MONTH, x);
        }
        return cal;
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
        Calendar easter = easter(year);
        Calendar carnival = new GregorianCalendar();
        carnival.setTime(easter.getTime());
        carnival.add(Calendar.DAY_OF_MONTH, -47);
        Calendar corpusChristi = new GregorianCalendar();
        corpusChristi.setTime(easter.getTime());
        corpusChristi.add(Calendar.DAY_OF_MONTH, +60);
        easter.add(Calendar.DAY_OF_MONTH, -2);
        movableHolidays.clear();
        movableHolidays
                .add(new Holiday(carnival.get(Calendar.DAY_OF_MONTH), carnival.get(Calendar.MONTH) + 1, "Carnaval"));
        movableHolidays.add(new Holiday(easter.get(Calendar.DAY_OF_MONTH), easter.get(Calendar.MONTH) + 1,
                "Sexta-feira da Paixão"));
        movableHolidays.add(new Holiday(corpusChristi.get(Calendar.DAY_OF_MONTH), corpusChristi.get(Calendar.MONTH) + 1,
                "Corpus Christi"));
    }

    private int numberOfDays(Calendar date) {
        int ano = date.get(Calendar.YEAR);
        int[] numDays = new int[] { 31, (ano % 4 == 0 && (ano % 400 == 0 || ano % 100 != 0)) ? 29 : 28, 31, 30, 31, 30,
                31, 31, 30, 31, 30, 31 };
        return numDays[date.get(Calendar.MONTH)];
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
        Calendar date = new GregorianCalendar(year, month, 1);
        List<Day> days = new ArrayList<>();
        for (int i = 0; i < 42; i++) {
            days.add(new Day(0));
        }
        for (int i = 0, j = date.get(Calendar.DAY_OF_WEEK) - 1; i < numberOfDays(date); i++, j++) {
            Holiday holiday = getHoliday(i + 1, date.get(Calendar.MONTH) + 1);
            days.set(j, new Day(i + 1, holiday != null ? holiday.getMessage() : null));
        }
        String monthName = date.getDisplayName(Calendar.MONTH, Calendar.LONG_FORMAT, locale);
        Map<String, Integer> weekNames = date.getDisplayNames(Calendar.DAY_OF_WEEK, Calendar.SHORT_FORMAT, locale);
        return new Month(days, monthName, weekNames);
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
        // Year ano = c.computeMonth(2021, 4, new Locale("pt"));

        Year ano = c.computeYear(2021, new Locale("pt"));
        // ano.forEach(mes -> System.out.println(mes));
        System.out.println(JsonbBuilder.create().toJson(ano));

        JAXBContext jaxbContext = JAXBContext.newInstance(Year.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        jaxbMarshaller.marshal(ano, System.out);
    }
}
