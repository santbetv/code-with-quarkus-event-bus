package org.acme;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.eventbus.EventBus;
import io.vertx.mutiny.core.eventbus.Message;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/greet")
public class GreetingResource {

    @Inject
    EventBus bus; // Inyectamos el EventBus

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/hello")
    public String hello() {
        return "Hello from RESTEasy Reactive holaaa";
    }

    // Enviar un evento y recibir una respuesta síncrona
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/{name}")
    public Uni<String> greeting(@PathParam("name") String name) {
        // Enviar un evento al address "greeting" y transformar la respuesta
        return bus.<String>request("greeting", name).onItem().transform(Message::body); // Obtener el cuerpo del mensaje
    }

    // Enviar un evento asíncrono y recibir una respuesta
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/async/{name}")
    public Uni<String> greetingAsync(@PathParam("name") String name) {
        return bus.<String>request("greeting-async", name).onItem().transform(Message::body); // Procesar la respuesta del evento asíncrono
    }

    @GET
    @Path("/fire-forget/{name}")
    public void fireForget(@PathParam("name") String name) {
        System.out.println("greeting SANTIAGO");
        bus.publish("greeting", name); // Publicar sin esperar respuesta
    }
}
