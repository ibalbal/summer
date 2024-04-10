package com.yujigu.summer.iwebot.yunxiao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event  implements Serializable {
    private String event;
    private String action;
    private Task task;
    private List<Source> sources;
    private List<GlobalParam> globalParams;

}