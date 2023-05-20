import Calendar from "./Calendar.js";

class GUI {
    constructor() {
        this.c = new Calendar();
    }
    clearTables() {
        let answer = document.querySelector("main");
        answer.innerHTML = "";
    }
    dayOfWeek(day) {
        let nomes = ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"];
        return nomes[day];
    }
    fillMonth(date, monthName) {
        let table = document.createElement("table");
        let tr = table.insertRow(0);
        let caption = document.createElement("caption");
        caption.innerHTML = monthName;
        table.appendChild(caption);
        tr = table.insertRow(1);
        for (let i = 0; i < 7; i++) {
            let day = document.createElement("th");
            day.innerHTML = this.dayOfWeek(i);
            tr.appendChild(day);
        }
        table.appendChild(tr);
        for (let i = 0; i < date.length; i++) {
            if (i % 7 === 0) {
                tr = table.insertRow(-1);
            }
            if (date[i].day === 0) {
                tr.insertCell(-1);
            } else {
                let td = tr.insertCell(-1);
                if (date[i].message) {
                    td.className = "holiday";
                    td.title = date[i].message;
                }
                td.textContent = date[i].day;
            }
        }
        return table;
    }
    compute() {
        this.clearTables();
        let input = document.getElementById("year");
        let year = input.valueAsNumber;
        let months = this.c.computeYear(year);
        let answer = document.querySelector("main");
        for (let i = 0; i < months.length; i++) {
            let monthName = new Date(year, i, 1).toLocaleString("en", { month: "long" });
            let name = monthName.charAt(0).toUpperCase() + monthName.slice(1);
            answer.appendChild(this.fillMonth(months[i], name));
        }
        return false;
    }
    registerEvents() {
        let year = document.getElementById("year");
        let form = document.forms[0];
        form.onsubmit = this.compute.bind(this);
        year.focus();
    }
}
let gui = new GUI();
gui.registerEvents();
