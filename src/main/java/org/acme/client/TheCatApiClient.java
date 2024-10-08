package org.acme.client;

import org.acme.Entities.CatImage;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;


@RegisterRestClient(configKey="thecatapi-api") // Asegúrate que coincida con application.properties
public interface TheCatApiClient {

    @GET // Indica que es una solicitud GET
    @Path("/v1/images/search") // La ruta base
    @Produces(MediaType.APPLICATION_JSON) // Indica el tipo de respuesta
    List<CatImage> getCatImages(); // Asegúrate de que el método devuelva una lista de CatImage
}
