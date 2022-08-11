/* global XPathResult */

function GUI() {
    function eraseCalendar() {
        let output = document.getElementById("output");
        output.innerHTML = "";
    }
    function fillMonth( {days, name, weeknames}) {
        let table = document.createElement("table");
        let tr = table.insertRow(0);
        let monthName = document.createElement("caption");
        monthName.innerHTML = name;
        table.appendChild(monthName);
        tr = table.insertRow(1);
        let weekdays = Object.entries(weeknames).sort((a, b) => a[1] - b[1]).map(a => a[0]);
        for (let i = 0; i < weekdays.length; i++) {
            let day = document.createElement("th");
            day.innerHTML = weekdays[i];
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
    function compute(evt) {
        evt.preventDefault();
        eraseCalendar();
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
            for (let i = 0; i < months.length; i++) {
                let month = months[i];
                let obj = {};
                obj.name = month.getAttribute("name");
                let days = [];
                let eDays = month.querySelectorAll("day");
                for (let j = 0; j < eDays.length; j++) {
                    days.push({day: parseInt(eDays[j].getAttribute("n")), holiday: eDays[j].getAttribute("holiday")});
                }
                obj.days = days;
                let entries = {};
                let eEntries = month.querySelectorAll("entry");
                for (let j = 0; j < eEntries.length; j++) {
                    let key = eEntries[j].querySelector("key").textContent;
                    let value = parseInt(eEntries[j].querySelector("value").textContent);
                    entries[key] = value;
                }
                obj.weeknames = entries;
                listOfMonths.push(obj);
            }
            return listOfMonths;
        };
        let options = [{type: 'application/json', convert: resolve => resolve.json(), process: processJSON}, {type: 'application/xml', convert: resolve => resolve.text(), process: processXML}];
        let formatOptions = json ? options[0] : options[1];
        let yearCreation = months => {
            let output = document.getElementById("output");
            for (let month of months) {
                output.appendChild(fillMonth(month));
            }
        };
        let url = month ? `webresources/calendar/${year}/${month}` : `webresources/calendar/${year}`;
        window.fetch(url, {headers: new Headers({'Accept': formatOptions.type})}).then(formatOptions.convert).then(formatOptions.process).then(yearCreation);
    }
    function registerEvents() {
        let form = document.forms[0];
        form.onsubmit = compute;
        form.month.focus();
    }
    return {registerEvents};
}
let gui = new GUI();
gui.registerEvents();
