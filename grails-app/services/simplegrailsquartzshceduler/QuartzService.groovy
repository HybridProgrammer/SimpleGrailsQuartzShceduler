
import grails.transaction.Transactional
import org.quartz.JobBuilder
import org.quartz.JobDetail
import org.quartz.JobKey
import org.quartz.Scheduler
import org.quartz.SchedulerFactory
import org.quartz.Trigger
import org.quartz.TriggerBuilder
import org.quartz.TriggerKey
import simplegrailsquartzshceduler.Frequency

import static org.quartz.CronScheduleBuilder.cronSchedule

@Transactional
class QuartzService {
    final static SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();
    final static Scheduler sched = schedFact.getScheduler();

    QuartzService() {
        if(!sched.isStarted()) {
            sched.start();
        }
    }

    def schedule(Class<?> jobClass, Frequency frequency){
        JobDetail job = JobBuilder.newJob(jobClass)
                .withIdentity(frequency.tag, "frequency")
                .build()

        job.getJobDataMap().put("frequency", frequency)

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(frequency.tag, "frequency")
                .withPriority(6)
                .forJob(JobKey.jobKey(frequency.tag, "frequency"))
                .withSchedule(cronSchedule(frequency.cron))
        // Start right away .startAt(new LocalDate().toDateTimeAtStartOfDay().toDate()) // Start at midnight today
                .build()

        sched.scheduleJob(job, trigger)
    }

    def unSchedule(String tag) {
        sched.unscheduleJob(new TriggerKey(tag, "frequency"))
    }

    def reSchedule(Frequency frequency) {
        if(sched.checkExists(new JobKey(frequency.tag, "frequency"))) {
            unSchedule(frequency.tag)
        }

        schedule(frequency)
    }


}
