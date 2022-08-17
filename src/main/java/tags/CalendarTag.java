package tags;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.JspWriter;
import jakarta.servlet.jsp.PageContext;
import jakarta.servlet.jsp.tagext.SimpleTagSupport;
import model.CalendarGenerator;
import model.Month;
import model.Year;

public class CalendarTag extends SimpleTagSupport {

    private int year;
    private int month;

    public void setYear(Integer i) {
        if (i != null) {
            this.year = i;
        }
    }

    public void setMonth(Integer i) {
        this.month = i;
    }

    @Override
    public void doTag() throws JspException, IOException {
        CalendarGenerator calendar = new CalendarGenerator();
        JspWriter out = getJspContext().getOut();
        PageContext pageContext = (PageContext) getJspContext();
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        Locale v = request.getLocale();
        Year listOfMonths;

        if (this.month == 0) {
            listOfMonths = calendar.computeYear(year, v);
        } else {
            listOfMonths = calendar.computeMonth(year, month - 1, v);
        }
        Writer sw = new StringWriter();
        XMLOutputFactory outputFactory = XMLOutputFactory.newFactory();
        try {
            XMLStreamWriter xsw = outputFactory.createXMLStreamWriter(sw);
            xsw.writeStartDocument();
            for (Month month : listOfMonths.getMonths()) {
                this.fillMonth(month, xsw);
            }
            xsw.writeEndDocument();
            xsw.close();
        } catch (XMLStreamException ex) {
            System.out.println(ex.getMessage());
        }
        out.println(sw.toString());
    }

    public void fillMonth(Month month, XMLStreamWriter xsw) throws XMLStreamException {
        var days = month.getDays();
        var name = month.getName();
        var weeknames = month.getWeeknames();
        xsw.writeStartElement("table");
        xsw.writeStartElement("caption");
        xsw.writeCharacters(name);
        xsw.writeEndElement();
        xsw.writeStartElement("tr");
        for (var value : weeknames) {
            xsw.writeStartElement("th");
            xsw.writeCharacters(value);
            xsw.writeEndElement();
        }
        xsw.writeEndElement();
        for (int i = 0; i < days.size(); i++) {
            if (i % 7 == 0) {
                if(i != 0) {
                    xsw.writeEndElement();    
                }
                xsw.writeStartElement("tr");
            }
            if (days.get(i).getDay() == 0) {
                xsw.writeStartElement("td");
                xsw.writeEndElement();
            } else {
                xsw.writeStartElement("td");
                if (days.get(i).getHoliday() != null) {
                    xsw.writeAttribute("class", "holiday");
                    xsw.writeAttribute("title", days.get(i).getHoliday());
                }
                xsw.writeCharacters(String.valueOf(days.get(i).getDay()));
                xsw.writeEndElement();
            }
        }

    }

}
