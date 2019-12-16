package com.wenbin.listener;

import com.dangdang.ddframe.job.executor.ShardingContexts;
import com.dangdang.ddframe.job.lite.api.listener.ElasticJobListener;


public class MyElasticJobListener implements ElasticJobListener {

    private long beginTime = 0;

    @Override
    public void beforeJobExecuted(ShardingContexts shardingContexts) {
        beginTime = System.currentTimeMillis();

        System.out.println("===>{} JOB BEGIN TIME: {} <=== " + shardingContexts.getJobName() + beginTime);
    }

    @Override
    public void afterJobExecuted(ShardingContexts shardingContexts) {
        long endTime = System.currentTimeMillis();
        System.out.println("===>{} JOB END TIME: {},TOTAL CAST: {} <===" + shardingContexts.getJobName() + endTime + (endTime - beginTime));
    }
}
