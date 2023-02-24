package util;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import org.springframework.http.HttpHeaders;

import java.util.List;
import java.util.Map;

public class BaseAPIUtil {
    private static final Client client = ClientBuilder.newClient();
    private static WebTarget webTarget;
    private static Response response;

    public static Response sendGetRequest(String url, String jwtToken, MultivaluedMap<String, String> params) {
        webTarget = client.target(url);

        for(Map.Entry<String, List<String>> entry : params.entrySet()) {
            if (entry.getValue().size() == 1) {
                webTarget = webTarget.queryParam(entry.getKey(), entry.getValue().get(0));
            } else {
                webTarget = webTarget.queryParam(entry.getKey(), entry.getValue());
            }
        }

        response = webTarget
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .get();

        return response;
    }

    public static Response sendPostRequest(String url, String jwtToken, Object object) {
        webTarget = client.target(url);

        response = webTarget
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .post(Entity.entity(object, MediaType.APPLICATION_JSON));

        return response;
    }
}
