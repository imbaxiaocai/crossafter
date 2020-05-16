package com.example.crossafter.dailytask;

import com.example.crossafter.Retailer.bean.RetailerInventory;
import com.example.crossafter.Retailer.dao.RetailerInventoryMapper;
import com.example.crossafter.goods.bean.Good;
import com.example.crossafter.goods.dao.GoodMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Configuration
@EnableScheduling
public class DailyScheduleTask {
    @Autowired
    private RetailerInventoryMapper retailerInventoryMapper;
    @Autowired
    private GoodMapper goodMapper;
    //清理过期库存至历史表
    @Scheduled(cron = "0 0 1 * * ?")
    private void clearInventory(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        String now = sdf.format(date);
        List<RetailerInventory> retailerInventories = retailerInventoryMapper.getExpired(now);
        try {
            for(int i=0;i<retailerInventories.size();i++){
                Good good = new Good();
                good.setGid(retailerInventories.get(i).getGid());
                //库存返还
                int amount = goodMapper.getAmount(good.getGid());
                good.setAmount(amount+retailerInventories.get(i).getAmount());
                retailerInventoryMapper.addToHis(retailerInventories.get(i));
                retailerInventoryMapper.delExpiredInventory(retailerInventories.get(i));

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}
