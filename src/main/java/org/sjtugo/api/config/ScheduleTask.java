package org.sjtugo.api.config;

import org.apache.commons.lang.StringUtils;
import org.sjtugo.api.DAO.TrafficInfoRepository;
import org.sjtugo.api.entity.TrafficInfo;
import org.sjtugo.api.service.TrafficService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;


@Component
public class ScheduleTask implements SchedulingConfigurer {

    protected static Logger logger = LoggerFactory.getLogger(ScheduleTask.class);
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final String DEFAULT_CRON = "0/5 * * * * ?";
    private String cron = DEFAULT_CRON;

    @Autowired
    private TrafficInfoRepository trafficInfoRepository;

    @Autowired
    private TrafficService trafficService;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        Runnable task = () -> {
            System.out.println("动态修改定时任务cron参数，当前时间：" + sdf.format(new Date()));
            trafficService.timeSetMenus();
        };

        Trigger trigger = triggerContext -> {
            TrafficInfo trafficInfo = trafficInfoRepository.findById(1).orElse(null);
            if (null == trafficInfo || StringUtils.isEmpty(trafficInfo.getTaskCron())) {
                cron = DEFAULT_CRON;
            }
            CronTrigger trigger1 = new CronTrigger(cron);
            return trigger1.nextExecutionTime(triggerContext);
        };
        taskRegistrar.addTriggerTask(task, trigger);
    }
}
