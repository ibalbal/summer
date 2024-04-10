package com.yujigu.summer.iwebot.yunxiao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Source implements Serializable {
    private String repo;
    private String branch;
    private String commitId;
    private String previousCommitId;
}
