package com.elk.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author zwt
 * @version 1.0
 * @date 2020/4/24 17:42
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class user {
    private String name;
    private int age;

}
