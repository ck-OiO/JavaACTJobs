package com.github.ck_oio.javaatcjobs.thirteenthjobs.homework06.version2;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmqMsg<T> {
    private Map<String, String> headers;
    private T body;
}
