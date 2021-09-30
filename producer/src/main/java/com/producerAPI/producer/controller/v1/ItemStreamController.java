package com.producerAPI.producer.controller.v1;


import com.producerAPI.producer.controller.v1.dto.ItemCappedDTO;
import com.producerAPI.producer.document.ItemCapped;
import com.producerAPI.producer.repository.ItemReactiveCappedRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

import static com.producerAPI.producer.constants.ItemConstants.ITEM_POST_END_POINT_V1;
import static com.producerAPI.producer.constants.ItemConstants.ITEM_STREAM_END_POINT_V1;

@RestController
@Slf4j
public class ItemStreamController {

    @Autowired
    ItemReactiveCappedRepository itemReactiveCappedRepository;

    @GetMapping(value = ITEM_STREAM_END_POINT_V1,produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<ItemCapped> getItemsStream() {

        return itemReactiveCappedRepository.findItemsBy();
    }

    @PostMapping(value = ITEM_POST_END_POINT_V1, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> persistEvent(@Valid @RequestBody ItemCappedDTO request) {
        try {
            ItemCapped itemCapped = new ItemCapped(null, request.getDescription(), request.getPrice());
            log.info("item from request: "+itemCapped);
            return new ResponseEntity<>(itemReactiveCappedRepository.save(itemCapped), HttpStatus.CREATED);

        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();

            response.put("error", e.getMessage());

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

}
