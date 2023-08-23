package org.example.model;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Currency {
    int id;
    String code;
    String fullName;
    String sign;


}
