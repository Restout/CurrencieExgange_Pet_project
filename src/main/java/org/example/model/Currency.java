package org.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Currency implements Serializable {
    @JsonProperty
    int id;
    @JsonProperty
    String code;
    @JsonProperty
    String fullName;
    @JsonProperty
    String sign;


}
