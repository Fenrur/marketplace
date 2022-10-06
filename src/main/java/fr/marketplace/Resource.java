package fr.marketplace;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class Resource {

    public final static Image LOADING_IMAGE = new Image("/fr/marketplace/controller/snoop-dogg-dance.gif");
    private final static StackWalker STACK_WALKER = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);
    private final static HttpClient HTTP_CLIENT = HttpClient.newHttpClient();

    public static URL getResource(String name) {
        Objects.requireNonNull(name);
        return Objects.requireNonNull(STACK_WALKER.getCallerClass().getResource(name));
    }

    public static InputStream getResourceAsStream(String name) {
        Objects.requireNonNull(name);
        return Objects.requireNonNull(STACK_WALKER.getCallerClass().getResourceAsStream(name));
    }

    public static Parent loadFXML(String name) {
        Objects.requireNonNull(name);
        try {
            final Class<?> callerClass = STACK_WALKER.getCallerClass();
            return FXMLLoader.load(Objects.requireNonNull(callerClass.getResource(name)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static FXMLLoader loaderFXML(String name) {
        Objects.requireNonNull(name);
        try {
            final Class<?> callerClass = STACK_WALKER.getCallerClass();
            return FXMLLoader.load(Objects.requireNonNull(callerClass.getResource(name)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Image loadSVG(String name) {
        Objects.requireNonNull(name);
        final Class<?> callerClass = STACK_WALKER.getCallerClass();
        BufferedImageTranscoder trans = new BufferedImageTranscoder();
        Image img;
        try (InputStream is = callerClass.getResourceAsStream(name)) {
            TranscoderInput transIn = new TranscoderInput(is);
            try {
                trans.transcode(transIn, null);
                img = SwingFXUtils.toFXImage(trans.getBufferedImage(), null);
            } catch (TranscoderException ex) {
                throw new RuntimeException(ex);
            }
        } catch (IOException io) {
            throw new RuntimeException(io);
        }
        return img;
    }

    public static CompletableFuture<Image> fetchImageAsync(URI imageUrl) {
        return HTTP_CLIENT.sendAsync(
                        HttpRequest.newBuilder()
                                .uri(imageUrl)
                                .build(),
                        HttpResponse.BodyHandlers.ofInputStream()
                )
                .thenApply(httpResponse -> {
                    if (httpResponse.statusCode() == 200) {

                        final var body = httpResponse.body();

                        return new Image(body);
                    }
                    throw new RuntimeException("Can't fetch image: " + imageUrl);

                });
    }
}
