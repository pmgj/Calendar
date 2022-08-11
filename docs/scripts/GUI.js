import {Calendar} from "./Calendar.js";

function GUI() {
    let c = new Calendar();
    function limparTabelas() {
        let resposta = document.getElementById("answer");
        resposta.innerHTML = "";
    }
    function nomeDoDiaDaSemana(dia) {
        let nomes = ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"];
        return nomes[dia];
    }
    function preencherMes(data, monthName) {
        let tabela = document.createElement("table");
        let tr = tabela.insertRow(0);
        let nomeMes = document.createElement("caption");
        nomeMes.innerHTML = monthName;
        tabela.appendChild(nomeMes);
        tr = tabela.insertRow(1);
        for (let i = 0; i < 7; i++) {
            let dia = document.createElement("th");
            dia.innerHTML = nomeDoDiaDaSemana(i);
            tr.appendChild(dia);
        }
        tabela.appendChild(tr);
        for (let i = 0; i < data.length; i++) {
            if (i % 7 === 0) {
                tr = tabela.insertRow(-1);
            }
            if (data[i].dia === 0) {
                tr.insertCell(-1);
            } else {
                let td = tr.insertCell(-1);
                if (data[i].mensagem) {
                    td.className = "feriado";
                    td.title = data[i].mensagem;
                }
                td.textContent = data[i].dia;
            }
        }
        return tabela;
    }
    function calcular() {
        limparTabelas();
        let input = document.getElementById("year");
        let ano = parseInt(input.value, 10);
        let year = c.calcular(ano);
        let resposta = document.getElementById("answer");
        for (let i = 0; i < year.length; i++) {
            let monthName = new Date(ano, i, 1).toLocaleString("en", {month: "long"});
            let name = monthName.charAt(0).toUpperCase() + monthName.slice(1);
            resposta.appendChild(preencherMes(year[i], name));
        }
        return false;
    }
    function registerEvents() {
        let year = document.getElementById("year");
        let form = document.forms[0];
        form.onsubmit = calcular;
        year.focus();
    }
    return {registerEvents};
}
let gui = new GUI();
gui.registerEvents();
