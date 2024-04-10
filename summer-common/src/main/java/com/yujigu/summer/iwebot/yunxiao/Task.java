package com.yujigu.summer.iwebot.yunxiao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task implements Serializable {
    private Integer pipelineId;
    private String pipelineName;
    private String stageName;
    private String taskName;
    private Integer buildNumber;
    private String statusCode;
    private String statusName;
    private String pipelineUrl;
    private String message;
}
