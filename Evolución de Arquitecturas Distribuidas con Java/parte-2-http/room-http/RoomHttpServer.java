import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class RoomHttpServer {
    public static void main(String[] args) throws Exception {
        RoomRepository repository = new RoomRepository();
        HttpServer server = HttpServer.create(new InetSocketAddress(8081), 0);

        server.createContext("/rooms", new RoomHandler(repository));
        server.setExecutor(null);
        server.start();

        System.out.println("RoomHttpServer escuchando en http://localhost:8081/rooms");
    }

    static class RoomHandler implements HttpHandler {
        private final RoomRepository repository;

        public RoomHandler(RoomRepository repository) {
            this.repository = repository;
        }

        @Override
        public void handle(HttpExchange exchange) {
            try {
                String method = exchange.getRequestMethod();
                String path = exchange.getRequestURI().getPath();
                String id = queryParam(exchange.getRequestURI().getQuery(), "id");

                String response;
                if ("GET".equals(method) && "/rooms".equals(path) && id == null) {
                    response = repository.list();
                } else if ("GET".equals(method) && "/rooms".equals(path)) {
                    response = repository.consult(normalize(id));
                } else if ("POST".equals(method) && "/rooms/reserve".equals(path)) {
                    response = repository.reserve(normalize(id));
                } else if ("POST".equals(method) && "/rooms/release".equals(path)) {
                    response = repository.release(normalize(id));
                } else {
                    response = "ERROR_OPERACION_INVALIDA";
                }

                send(exchange, response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private String queryParam(String query, String key) {
            if (query == null) {
                return null;
            }
            String[] params = query.split("&");
            for (String param : params) {
                String[] parts = param.split("=", 2);
                if (parts.length == 2 && key.equals(parts[0])) {
                    return parts[1];
                }
            }
            return null;
        }

        private String normalize(String id) {
            return id == null ? "" : id.trim().toUpperCase();
        }

        private void send(HttpExchange exchange, String response) throws Exception {
            byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=utf-8");
            exchange.sendResponseHeaders(200, bytes.length);

            OutputStream os = exchange.getResponseBody();
            os.write(bytes);
            os.close();
        }
    }
}
