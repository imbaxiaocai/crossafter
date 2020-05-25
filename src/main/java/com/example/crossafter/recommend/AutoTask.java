package com.example.crossafter.recommend;

import com.example.crossafter.recommend.service.RecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class AutoTask {
    @Autowired
    RecommendService recommendService;
    @Scheduled(cron = "40 01 13 * * ?")
    private void updateWR(){
        recommendService.updateWR();
    }
}
