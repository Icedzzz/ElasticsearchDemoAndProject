package com.meng.controller;

import com.meng.service.BulkAndSearchService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author zwt
 * @version 1.0
 * @date 2020/4/26 10:52
 */
@RestController
public class EsTestController {
    @Autowired
    private BulkAndSearchService bulkService;

    @GetMapping("/parse/{keyword}")
    public Boolean BulkIntoEs(@PathVariable("keyword") String keyword) throws IOException {
        return bulkService.BulkGoods(keyword);
    }

    @GetMapping("/search/{keyword}/{pageNum}/{pageSize}")
    public List<Map<String, Object>> SearchGoods(
            @PathVariable("keyword") String keyword,
            @PathVariable("pageNum") int pageNum,
            @PathVariable("pageSize") int pageSize
    ) throws Exception {
        return bulkService.SearchGoods(keyword,pageNum,pageSize);
    }
}
