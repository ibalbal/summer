package com.yujigu.summer.iwebot;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MusicParams implements Serializable {
    private String type;
    private String content;
}
