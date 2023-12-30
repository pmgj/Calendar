import Calendar from "./Calendar.js";

class GUI {
    constructor() {
        this.c = new Calendar();
        this.year = null;
    }
    clearTables() {
        let answer = document.querySelector("main");
        answer.innerHTML = "";
    }
    dayOfWeek(day) {
        let monthName = new Date(2023, 0, day + 1).toLocaleString(navigator.language, { weekday: "short" });
        return (monthName.charAt(0).toUpperCase() + monthName.slice(1)).slice(0, 3);
    }
    fillMonth(date, monthName) {
        let table = document.createElement("table");
        table.className = "table table-bordered caption-top w-25 me-4";
        let caption = document.createElement("caption");
        caption.className = "text-center fw-bold fs-3";
        caption.innerHTML = monthName;
        table.appendChild(caption);
        let thead = table.createTHead();
        thead.className = "table-dark";
        let tr = thead.insertRow(0);
        for (let i = 0; i < 7; i++) {
            let day = document.createElement("th");
            day.className = "text-center";
            day.innerHTML = this.dayOfWeek(i);
            tr.appendChild(day);
        }
        let tbody = table.createTBody();
        for (let i = 0, td; i < date.length; i++) {
            let sunday = false;
            if (i % 7 === 0) {
                tr = tbody.insertRow();
                sunday = true;
            }
            if (date[i].day === 0) {
                td = tr.insertCell();
            } else {
                td = tr.insertCell();
                if(sunday) td.className = "fw-bold bg-danger";
                if (date[i].message) {
                    td.className = "fw-bold bg-danger";
                    td.title = date[i].message;
                }
                td.textContent = date[i].day;
            }
        }
        return table;
    }
    compute() {
        this.clearTables();
        let year = this.year.valueAsNumber;
        let months = this.c.computeYear(year);
        let answer = document.querySelector("main");
        for (let i = 0; i < months.length; i++) {
            let monthName = new Date(year, i, 1).toLocaleString(navigator.language, { month: "long" });
            let name = monthName.charAt(0).toUpperCase() + monthName.slice(1);
            answer.appendChild(this.fillMonth(months[i], name));
        }
        return false;
    }
    registerEvents() {
        this.year = document.getElementById("year");
        this.year.onchange = this.compute.bind(this);
        this.year.focus();
        this.year.value = new Date().getFullYear();
        this.compute();
    }
}
let gui = new GUI();
gui.registerEvents();