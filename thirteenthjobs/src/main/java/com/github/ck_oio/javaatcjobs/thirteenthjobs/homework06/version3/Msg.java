package com.github.ck_oio.javaatcjobs.thirteenthjobs.homework06.version3;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Msg<T> {
    private String topicId;
    private T t;
}
