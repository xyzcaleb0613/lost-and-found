package com.example.controller;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.Result;
import com.example.entity.Found;
import com.example.entity.Lost;
import com.example.service.FoundService;
import com.example.service.LostService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequestMapping("/echarts")
@RestController

public class EchartsController {
    @Resource
    private LostService lostService;
    @Resource
    private FoundService foundService;
    @GetMapping("/all")
    public Result all(){
        //查询出所有失物广场的数据
        List<Lost> losts = lostService.selectAll(new Lost());
        //查询出所有招领广场的数据
        List<Found> founds=foundService.selectAll(new Found());
        Map<String,Object> resultMap= new HashMap<>();
        List<Map<String,Object>> list=new ArrayList<>();
        //往list里面塞业务数据
        Map<String,Object> lostMap=new HashMap<>();
        lostMap.put("name","平台发布失物总量");
        lostMap.put("value",losts.size());
        list.add(lostMap);

        Map<String,Object> foundMap=new HashMap<>();
        foundMap.put("name","平台招领物品总量");
        foundMap.put("value",founds.size());
        list.add(foundMap);

        resultMap.put("text","平台所有物品数量的统计");
        resultMap.put("subtext","统计维度：失物广场和招领广场");
        resultMap.put("name","总数");
        resultMap.put("data",list);

        return Result.success(resultMap);

    }
    @GetMapping("/lost")
    public Result lost(){
        //查询出所有失物广场的数据
        List<Lost> losts = lostService.selectAll(new Lost());

        Map<String,Object> resultMap=new HashMap<>();
        List<Map<String,Object>> list=new ArrayList<>();

        //封装list
        Map<String, Long> lostMap=losts.stream().filter(x -> ObjectUtil.isNotEmpty(x.getStatus()))
                        .collect(Collectors.groupingBy(Lost::getStatus,Collectors.counting()));
        for(String key:lostMap.keySet()){
            Map<String,Object> map=new HashMap<>();
            map.put("name",key);
            map.put("value",lostMap.get(key));
            list.add(map);
        }

        resultMap.put("text","失物广场物品数量的占比数据");
        resultMap.put("subtext","统计维度：丢失中和已找回");
        resultMap.put("name","总数");
        resultMap.put("data",list);

        return Result.success(resultMap);

    }
    @GetMapping("/found")
    public Result found(){
        //查询出所有失物广场的数据
        List<Found> founds = foundService.selectAll(new Found());

        Map<String,Object> resultMap=new HashMap<>();
        List<Map<String,Object>> list=new ArrayList<>();

        //封装list
        Map<String, Long> foundMap=founds.stream().filter(x -> ObjectUtil.isNotEmpty(x.getStatus()))
                .collect(Collectors.groupingBy(Found::getStatus,Collectors.counting()));
        for(String key:foundMap.keySet()){
            Map<String,Object> map=new HashMap<>();
            map.put("name",key);
            map.put("value",foundMap.get(key));
            list.add(map);
        }
        resultMap.put("text","招领广场物品数量的占比数据");
        resultMap.put("subtext","统计维度：已找到失主和未找到失主");
        resultMap.put("name","总数");
        resultMap.put("data",list);
        return Result.success(resultMap);
    }
}
