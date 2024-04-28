package com.yujigu.summer.iwebot.controller;

import com.symxns.sym.core.result.Result;
import com.yujigu.summer.iwebot.gcw.GcwData;
import com.yujigu.summer.iwebot.service.GcwService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/gcw")
public class GcwController {

    @Autowired
    private GcwService gcwService;

    @RequestMapping("/search/{name}")
    public Result<List<GcwData>> gcw(@PathVariable("name") String name) {
        return Result.ok(gcwService.gcw(name));
    }

    @RequestMapping("/mp3")
    public Result<String> gcwMp3(@RequestParam("url") String url) {
        return Result.ok(gcwService.gcwMp3(url));
    }

}
