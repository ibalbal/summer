package com.yujigu.summer.iwebot.yunxiao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GlobalParam implements Serializable {
    private String key;
    private String value;
}
