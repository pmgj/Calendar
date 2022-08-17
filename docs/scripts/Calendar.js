import Holiday from "./Holiday.js";
import Day from "./Day.js";

export default class Calendar {
    constructor() {
        this.movableHolidays = [];
        this.fixedHolidays = [new Holiday(1, 1, "Confraternização Universal"), new Holiday(21, 4, "Tiradentes"), new Holiday(1, 5, "Dia do Trabalho"), new Holiday(24, 6, "São João"), new Holiday(16, 7, "Nossa Senhora do Carmo"), new Holiday(7, 9, "Independência do Brasil"), new Holiday(12, 10, "Nossa Senhora Aparecida"), new Holiday(28, 10, "Dia do Servidor Público"), new Holiday(2, 11, "Finados"), new Holiday(15, 11, "Proclamação da República"), new Holiday(8, 12, "Nossa Senhora da Conceição"), new Holiday(25, 12, "Natal"), new Holiday(31, 12, "Ano Novo")];
    }
    easter(year) {
        let a = year % 19;
        let b = Math.floor(year / 100);
        let c = year % 100;
        let d = Math.floor(b / 4);
        let e = b % 4;
        let f = Math.floor((b + 8) / 25);
        let g = Math.floor((b - f + 1) / 3);
        let h = (19 * a + b - d - g + 15) % 30;
        let i = Math.floor(c / 4);
        let k = c % 4;
        let l = (32 + 2 * e + 2 * i - h - k) % 7;
        let m = Math.floor((a + 11 * h + 22 * l) / 451);
        let p = Math.floor((h + l - 7 * m + 114) / 31);
        let q = (h + l - 7 * m + 114) % 31;
        let date = new Date(year, p - 1, q + 1);
        for (let x = date.getDate(); date.getDay() !== 0; x++) {
            date.setDate(x);
        }
        return date;
    }
    createMovableHolidays(year) {
        let easterDay = this.easter(year);
        let carnival = new Date(easterDay);
        carnival.setDate(easterDay.getDate() - 47);
        let corpusChristi = new Date(easterDay);
        corpusChristi.setDate(easterDay.getDate() + 60);
        easterDay.setDate(easterDay.getDate() - 2);
        this.movableHolidays = [];
        this.movableHolidays.push(new Holiday(carnival.getDate(), carnival.getMonth() + 1, "Carnaval"));
        this.movableHolidays.push(new Holiday(easterDay.getDate(), easterDay.getMonth() + 1, "Sexta-feira da Paixão"));
        this.movableHolidays.push(new Holiday(corpusChristi.getDate(), corpusChristi.getMonth() + 1, "Corpus Christi"));
    }
    isHoliday(day, month) {
        let isHoliday = (element) => element.day === day && element.month === month;
        return this.fixedHolidays.find(isHoliday) || this.movableHolidays.find(isHoliday);
    }
    numberOfDays(date) {
        let year = date.getFullYear();
        let numDays = [31, (year % 4 === 0 && (year % 400 === 0 || year % 100 !== 0)) ? 29 : 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
        return numDays[date.getMonth()];
    }
    computeMonth(date) {
        let month = new Array(42).fill(new Day(0));
        for (let i = 0, j = date.getDay(); i < this.numberOfDays(date); i++, j++) {
            let holiday = this.isHoliday(i + 1, date.getMonth() + 1);
            month[j] = new Day(i + 1, holiday?.message);
        }
        return month;
    }
    computeYear(year) {
        let calendar = [];
        this.createMovableHolidays(year);
        let data = new Date(year, 0, 1);
        for (let i = 0; i < 12; i++) {
            data.setMonth(i);
            calendar.push(this.computeMonth(data));
        }
        return calendar;
    }
}
