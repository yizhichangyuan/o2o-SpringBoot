package com.imooc.o2o.config.quartz;

import com.imooc.o2o.service.ProductSellDailyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * @PackageName:com.imooc.o2o.config.quartz
 * @NAME:QuartzConfig
 * @Description:
 * @author: yizhichangyuan
 * @date:2021/2/21 14:08
 */
@Configuration //写入到IOC容器中
public class QuartzConfig {
    @Autowired
    private ProductSellDailyService productSellDailyService;
    @Autowired
    private MethodInvokingJobDetailFactoryBean jobDetailFactory;
    @Autowired
    private CronTriggerFactoryBean triggerFactory;


    /**
     * 创建任务JobDetail，通过工厂类创建就不用实现Job接口了
     * @return
     */
    @Bean("jobDetailFactory")
    public MethodInvokingJobDetailFactoryBean createJobDetail(){
        // new出JobDetailFactory对象，此工厂是制作一个jobDetail，通过反射获取定时任务的类
        MethodInvokingJobDetailFactoryBean jobDetailFactoryBean = new MethodInvokingJobDetailFactoryBean();
        // 设置jobDetail的名字
        jobDetailFactoryBean.setName("product_sell_daily_job");
        // 设置jobDetail的组名
        jobDetailFactoryBean.setGroup("job_product_sell_daily_group");
        // 对于相同的JobDetail，当指定多个Trigger时，很可能第一个job完成之前，第二个job已经开始
        // 对于指定concurrent为false，多个job不会并发运行，第二个job将不会在第一个job完成之前开始
        jobDetailFactoryBean.setConcurrent(false);
        // 指定运行任务的类
        jobDetailFactoryBean.setTargetObject(productSellDailyService);
        // 指定运行的方法
        jobDetailFactoryBean.setTargetMethod("dailyCalculate");
        return jobDetailFactoryBean;
    }

    /**
     * 创建cornTrigger触发器工厂
     */
    @Bean("productSellDailyTriggerFactory")
    public CronTriggerFactoryBean createProductSellDailyTrigger(){
        // 创建triggerFactory实例，用来创建trigger
        CronTriggerFactoryBean triggerFactory = new CronTriggerFactoryBean();
        // 设置triggerFactory的名字
        triggerFactory.setName("product_sell_daily_trigger");
        // 设置组名
        triggerFactory.setGroup("jon_product_sell_daily_group");
        // 绑定jobDetail
        triggerFactory.setJobDetail(jobDetailFactory.getObject());
        // 设定cron表达式，每天凌晨执行一次
        triggerFactory.setCronExpression("0 0 0 * * ?");
        return triggerFactory;
    }

    /**
     * 创建调度工厂
     * @return
     */
    @Bean("schedulerFactory")
    public SchedulerFactoryBean createSchedulerFactory(){
        SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
        schedulerFactory.setTriggers(triggerFactory.getObject());
        return schedulerFactory;
     }

}
