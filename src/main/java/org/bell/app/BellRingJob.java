package org.bell.app;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@DisallowConcurrentExecution
public class BellRingJob implements Job {


    public void execute(JobExecutionContext context)
            throws JobExecutionException {
        //JsonFileHelper.getSchoolDays();
    }
}

