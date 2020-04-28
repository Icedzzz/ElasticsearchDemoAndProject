package com.meng.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author zwt
 * @version 1.0
 * @date 2020/4/25 23:20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class goods {
    private String img;
    private String name;
    private String price;

}
