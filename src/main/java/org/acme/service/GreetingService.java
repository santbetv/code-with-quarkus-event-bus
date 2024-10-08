package org.acme.service;

import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GreetingService {

    // Método síncrono: se ejecuta en el event loop (no debe bloquear)
    @ConsumeEvent("greeting")
    public String consume(String name) {
        return name.toUpperCase(); // Responderá el mensaje transformado a mayúsculas
    }

    // Método asíncrono con Mutiny: devuelve un Uni<String>
    @ConsumeEvent("greeting-async")
    public Uni<String> consumeAsync(String name) {
        return Uni.createFrom().item(() -> name.toUpperCase()); // Retorna respuesta asíncrona
    }
}
