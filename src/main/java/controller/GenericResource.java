package controller;

import java.util.List;
import java.util.Locale;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Request;
import jakarta.ws.rs.core.Variant;
import model.CalendarGenerator;
import model.Year;

@Path("calendar")
public class GenericResource {

    private static final CalendarGenerator calendar = new CalendarGenerator();

    @GET
    @Path("{year}")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public Year getCalendar(@PathParam("year") int year, @Context Request request) {
        Variant.VariantListBuilder vb = Variant.VariantListBuilder.newInstance();
        vb.mediaTypes(MediaType.APPLICATION_XML_TYPE, MediaType.APPLICATION_JSON_TYPE).languages(Locale.of("pt"),
                Locale.of("en"), Locale.of("es"), Locale.of("fr"));
        List<Variant> variants = vb.build();
        Variant v = request.selectVariant(variants);
        return calendar.computeYear(year, v.getLanguage());
    }

    @GET
    @Path("{year}/{month}")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public Year getCalendar(@PathParam("year") int year, @PathParam("month") int month, @Context Request request) {
        Variant.VariantListBuilder vb = Variant.VariantListBuilder.newInstance();
        vb.mediaTypes(MediaType.APPLICATION_XML_TYPE, MediaType.APPLICATION_JSON_TYPE).languages(Locale.of("pt"),
                Locale.of("en"), Locale.of("es"), Locale.of("fr"));
        List<Variant> variants = vb.build();
        Variant v = request.selectVariant(variants);
        return calendar.computeMonth(year, month - 1, v.getLanguage());
    }
}
