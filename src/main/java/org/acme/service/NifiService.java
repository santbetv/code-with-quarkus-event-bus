package org.acme.service;

import javax.enterprise.context.ApplicationScoped;
import io.smallrye.mutiny.Uni;
import io.quarkus.vertx.ConsumeEvent;
import org.acme.Entities.CatImage;
import org.acme.client.TheCatApiClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.util.List;

@ApplicationScoped
public class NifiService {

    @RestClient
    TheCatApiClient catApiClient;

    @ConsumeEvent("nifi-service")
    public Uni<String> handleNifi(String data) {
        return Uni.createFrom().item(() -> {
            try {
                // Llamar al cliente REST para obtener las imágenes
                List<CatImage> catImages = catApiClient.getCatImages();

                // Procesar la respuesta
                if (!catImages.isEmpty()) {
                    CatImage catImage = catImages.get(0);
                    return "ID: " + catImage.getId() + ", URL: " + catImage.getUrl();
                } else {
                    return "No images found";
                }
            } catch (Exception e) {
                System.err.println("Error fetching from TheCatAPI: " + e.getMessage());
                return "Nifi failed";
            }
        }).onFailure().retry().atMost(3);
    }
}


