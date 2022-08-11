import {Feriado} from "./Feriado.js";
import {Day} from "./Day.js";

function Calendar() {
    let feriadosMoveis = [];
    let feriadosFixos = [new Feriado(1, 1, "Confraternização Universal"), new Feriado(21, 4, "Tiradentes"), new Feriado(1, 5, "Dia do Trabalho"), new Feriado(24, 6, "São João"), new Feriado(16, 7, "Nossa Senhora do Carmo"), new Feriado(7, 9, "Independência do Brasil"), new Feriado(12, 10, "Nossa Senhora Aparecida"), new Feriado(28, 10, "Dia do Servidor Público"), new Feriado(2, 11, "Finados"), new Feriado(15, 11, "Proclamação da República"), new Feriado(8, 12, "Nossa Senhora da Conceição"), new Feriado(25, 12, "Natal"), new Feriado(31, 12, "Ano Novo")];
    function domingoPascoa(ano) {
        let a = ano % 19;
        let b = Math.floor(ano / 100);
        let c = ano % 100;
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
        let data = new Date(ano, p - 1, q + 1);
        for (let x = data.getDate(); data.getDay() !== 0; x++) {
            data.setDate(x);
        }
        return data;
    }
    function gerarFeriadosMoveis(ano) {
        let pascoa = domingoPascoa(ano);
        let carnaval = new Date(pascoa);
        carnaval.setDate(pascoa.getDate() - 47);
        let corpusChristi = new Date(pascoa);
        corpusChristi.setDate(pascoa.getDate() + 60);
        pascoa.setDate(pascoa.getDate() - 2);
        feriadosMoveis = [];
        feriadosMoveis.push(new Feriado(carnaval.getDate(), carnaval.getMonth() + 1, "Carnaval"));
        feriadosMoveis.push(new Feriado(pascoa.getDate(), pascoa.getMonth() + 1, "Sexta-feira da Paixão"));
        feriadosMoveis.push(new Feriado(corpusChristi.getDate(), corpusChristi.getMonth() + 1, "Corpus Christi"));
    }
    function ehFeriado(dia, mes) {
        let isHoliday = (element) => element.dia === dia && element.mes === mes;
        return feriadosFixos.find(isHoliday) || feriadosMoveis.find(isHoliday);
    }
    function dias(data) {
        let ano = data.getFullYear();
        let numDias = [31, (ano % 4 === 0 && (ano % 400 === 0 || ano % 100 !== 0)) ? 29 : 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
        return numDias[data.getMonth()];
    }
    function preencherMes(data) {
        let month = new Array(42).fill(new Day(0));
        for (let i = 0, j = data.getDay(); i < dias(data); i++, j++) {
            let feriado = ehFeriado(i + 1, data.getMonth() + 1);
//            month[j] = new Day(i + 1, feriado?.mensagem);
            month[j] = new Day(i + 1, feriado ? feriado.mensagem : null);
        }
        return month;
    }
    function calcular(ano) {
        let calendar = [];
        gerarFeriadosMoveis(ano);
        let data = new Date(ano, 0, 1);
        for (let i = 0; i < 12; i++) {
            data.setMonth(i);
            calendar.push(preencherMes(data));
        }
        return calendar;
    }
    return {calcular};
}

export {Calendar};
