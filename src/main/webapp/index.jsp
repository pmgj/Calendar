<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="ex" uri="http://sites.google.com/site/paulomgj/" %>
        <%@ page contentType="text/html" pageEncoding="UTF-8" %>
            <!DOCTYPE html>
            <html>

            <head>
                <title>Calendar</title>
                <meta charset="UTF-8" />
                <link type="image/svg" rel="icon" href="icon.svg" />
                <link rel="stylesheet" type="text/css" href="index.css" />
            </head>

            <body>
                <form>
                    <fieldset>
                        <legend>Calendar</legend>
                        <p><label for="month">Month:</label> <input type="number" id="month" name="month" min="1"
                                max="12" value="${param.month}" /></p>
                        <p><label for="year">Year:</label> <input type="number" id="year" name="year"
                                required="required" value="${param.year}" /></p>
                        <p><input type="submit" value="Show" /></p>
                    </fieldset>
                </form>
                <div id="output">
                    <ex:Calendar year="${param.year}" month="${param.month}" />
                </div>
            </body>

            </html>