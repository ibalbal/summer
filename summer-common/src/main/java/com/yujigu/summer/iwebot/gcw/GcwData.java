package com.yujigu.summer.iwebot.gcw;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GcwData implements Serializable {
    String title;
    String url;
}
