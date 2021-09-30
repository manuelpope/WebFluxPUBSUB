package com.producerAPI.producer.controller.v1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemCappedDTO {



    @NotEmpty(message = "could not be blank field name")
    private String description;
    @NotNull(message = "could not be blank field name")
    private Double price;
}
