class GUI {
    constructor() {

    }
    eraseCalendar() {
        let output = document.getElementById("output");
        output.innerHTML = "";
    }
    fillMonth({ days, name, weeknames }) {
        let table = document.createElement("table");
        let tr = table.insertRow(0);
        let monthName = document.createElement("caption");
        monthName.innerHTML = name;
        table.appendChild(monthName);
        tr = table.insertRow(1);
        for(let value of weeknames) {
            let day = document.createElement("th");
            day.innerHTML = value;
            tr.appendChild(day);
        }
        table.appendChild(tr);
        for (let i = 0; i < days.length; i++) {
            if (i % 7 === 0) {
                tr = table.insertRow(-1);
            }
            if (days[i].day === 0) {
                tr.insertCell(-1);
            } else {
                let td = tr.insertCell(-1);
                if (days[i].holiday) {
                    td.className = "holiday";
                    td.title = days[i].holiday;
                }
                td.textContent = days[i].day;
            }
        }
        return table;
    }
    compute(evt) {
        evt.preventDefault();
        this.eraseCalendar();
        let form = document.forms[0];
        let year = parseInt(form.year.value, 10);
        let month = parseInt(form.month.value, 10);
        let json = form.format[0].checked;
        let processJSON = data => data.months;
        let processXML = data => {
            let parser = new DOMParser();
            let eYear = parser.parseFromString(data, "application/xml");
            let months = eYear.querySelectorAll("month");
            let listOfMonths = [];
            for(let month of months) {
                let obj = {};
                obj.name = month.getAttribute("name");
                let days = [];
                let eDays = month.querySelectorAll("day");
                for (let day of eDays) {
                    days.push({ day: parseInt(day.getAttribute("n")), holiday: day.getAttribute("holiday") });
                }
                obj.days = days;
                let entries = [];
                let eEntries = month.querySelectorAll("weeknames");
                for (let value of eEntries) {
                    entries.push(value.textContent);
                }
                obj.weeknames = entries;
                listOfMonths.push(obj);
            }
            return listOfMonths;
        };
        let options = [{ type: 'application/json', convert: resolve => resolve.json(), process: processJSON }, { type: 'application/xml', convert: resolve => resolve.text(), process: processXML }];
        let formatOptions = json ? options[0] : options[1];
        let yearCreation = months => {
            let output = document.getElementById("output");
            for (let month of months) {
                output.appendChild(this.fillMonth(month));
            }
        };
        let url = month ? `webresources/calendar/${year}/${month}` : `webresources/calendar/${year}`;
        window.fetch(url, { headers: new Headers({ 'Accept': formatOptions.type }) }).then(formatOptions.convert).then(formatOptions.process).then(yearCreation);
    }
    registerEvents() {
        let form = document.forms[0];
        form.onsubmit = this.compute.bind(this);
        form.month.focus();
    }
}
let gui = new GUI();
gui.registerEvents();
