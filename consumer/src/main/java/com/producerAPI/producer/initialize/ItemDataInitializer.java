package com.producerAPI.producer.initialize;


import com.producerAPI.producer.document.ItemCapped;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Component
@Profile("!test")
@Slf4j
public class ItemDataInitializer implements CommandLineRunner {

    public static final String BASE_URL = "http://localhost:8080";
    public static final String STREAM_ITEMS = "/v1/stream/items";

    WebClient webClient = WebClient.create(BASE_URL);

    private static void doSomething(ItemCapped item) {

        log.info(item.toString() + "do something with this message");
    }

    @Override
    public void run(String... args) throws Exception {

        listenToHTTPNonBlocking();
    }

    public void listenToHTTPNonBlocking() {
        ConnectableFlux<ItemCapped> stringFlux = getAllItemsUsingExchange().publish();
        stringFlux.connect();
        stringFlux.subscribe(
                ItemDataInitializer::doSomething,
                error -> log.error(error.getMessage()),
                () -> log.info("finish messages")
        );


    }

    private Flux<ItemCapped> getAllItemsUsingExchange() {
        return webClient.get().uri(STREAM_ITEMS)
                .exchange()
                .filter(s -> !s.statusCode().isError())
                .flatMapMany(clientResponse -> clientResponse.bodyToFlux(ItemCapped.class))
                .retryBackoff(3, Duration.ofSeconds(5))
                .log();
    }
}





