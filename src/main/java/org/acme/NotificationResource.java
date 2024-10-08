package org.acme;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.eventbus.EventBus;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;


@Path("/notify")
public class NotificationResource {

    @Inject
    EventBus bus;  // Inyectamos el EventBus para enviar notificaciones

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/trigger/{data}")
    public Uni<String> triggerNotifications(@PathParam("data") String data) {
        // Notificar a Nifi con manejo de fallos
        Uni<String> notifyNifi = bus.<String>request("nifi-service", data)
                .onItem().transform(resp -> "Nifi: " + resp.body())
                .onFailure().recoverWithItem("Nifi: Service Unavailableeee");

        // Notificar a Calculations con manejo de fallos
        Uni<String> notifyCalculations = bus.<String>request("calculations-service", data)
                .onItem().transform(resp -> "Calculations: " + resp.body())
                .onFailure().recoverWithItem("Calculations: Service Unavailable");

        // Notificar a Matching con manejo de fallos
        Uni<String> notifyMatching = bus.<String>request("matching-service", data)
                .onItem().transform(resp -> "Matching: " + resp.body())
                .onFailure().recoverWithItem("Matching: Service Unavailable");

        // Combinar las tres respuestas
        return Uni.combine().all().unis(notifyNifi, notifyCalculations, notifyMatching)
                .combinedWith(responses -> {
                    // Procesar todas las respuestas y combinarlas en una sola salida
                    String nifiResp = (String) responses.get(0);
                    String calcResp = (String) responses.get(1);
                    String matchingResp = (String) responses.get(2);

                    return "All responses received: \n" + nifiResp + "\n" + calcResp + "\n" + matchingResp;
                });
    }

//    @GET
//    @Produces(MediaType.TEXT_PLAIN)
//    @Path("/trigger/{data}")
//    public Uni<String> triggerNotifications(@PathParam("data") String data) {
//        // Hacer tres notificaciones de forma paralela a Nifi, Calculations y Matching
//        Uni<String> notifyNifi = bus.<String>request("nifi-service", data).onItem().transform(resp -> "Nifi: " + resp.body());
//        Uni<String> notifyCalculations = bus.<String>request("calculations-service", data).onItem().transform(resp -> "Calculations: " + resp.body());
//        Uni<String> notifyMatching = bus.<String>request("matching-service", data).onItem().transform(resp -> "Matching: " + resp.body());
//
//        // Combinar las tres respuestas
//        return Uni.combine().all().unis(notifyNifi, notifyCalculations, notifyMatching)
//                .combinedWith(responses -> {
//                    // Procesar todas las respuestas y combinarlas en una sola salida
//                    String nifiResp = (String) responses.get(0);
//                    String calcResp = (String) responses.get(1);
//                    String matchingResp = (String) responses.get(2);
//
//                    return "All responses received: \n" + nifiResp + "\n" + calcResp + "\n" + matchingResp;
//                });
//    }
}
