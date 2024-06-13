---
layout: single
title:  "Quartz 테스트 - Window 시간 설정"
categories:
  - Quartz
tags:
  - [Quartz, Spring, SpringBoot]

toc: true
toc_sticky: true
---


## Quartz 테스트 코드와 매커니즘
- 아래 코드는 매주 월요일 오전 10시에 실행되도록 설정된 Quartz 테스트 코드이다.
- 매주 월요일 실행되는지 테스트하기 위해 Window의 시스템 시간을 변경하여 테스트하였다. 
- 테스트 중 특정 날짜에 실행되지 않는 현상이 발생하게 되어 원인을 찾아보았다.

<br>

- 원인 : `어플리케이션을 실행하는 시간`(Job과 Trigger가 등록된 시간) 보다 `Trigger 실행 시간이 이전`일 수 없다.
- 이해를 돕기 위해 예를 들어보자. 
  - Trigger의 실행 규칙을 `0 0 10 ? * MON` 이렇다. `매주 월요일 오전 10시`
  - 현재 시스템 시간을 `24/6/10 오전 10시10분`으로 설정했다고 해보자.   
  (실제 날짜는 24/6/4 이었는데, 여러번의 테스트로 인해 변경되어 있던 상황)
  - 이 상태에서 `어플리케이션`을 실행하면 `Job과 Trigger`는 `24/6/10 오전 10시10분`로 설정된다.
  - 그리고 테스트를 위해 시스템 날짜를 `24/6/10 오전 9시59분`으로 맞췄다. 그럼 1분 후 Quartz가 실행될 것이다.
  - 그런데 실행되지 않았다. `24/6/17 오전 9시59분` 으로 설정 하고 했더니 실행되었다. 
  - 이해가 되는가? 
  - `어플리케이션`이 실행되면서 `Job과 Trigger`는 `24/6/10 오전 10시10분`로 설정되었기 때문에 이전 날짜에는 Trigge가 작동하지 않는다.



```java
@Component        // spring bean 등록
@Profile("demo")  // 지정한 profile에서만 실행하도록 설정. 즉 demo가 active 된 경우에만 실행되는 class로 지정한 것.
public class SchedulerLocalMangement {
	private SchedulerFactory schedulerFactory;  // 스케줄러 생성 객체
  private Scheduler scheduler;                // 스케줄 작업 관리 객체
    
  @PostConstruct  // Spring 컨텍스트가 초기화 된 후에 호출한다.
  public void start() throws SchedulerException{
  
    schedulerFactory = new StdSchedulerFactory();   // StdSchedulerFactory 인스턴스 생성한다.
    scheduler = schedulerFactory.getScheduler();    // scheduler 인스턴스를 가져온다.
    /**
     * 스케줄러 시작
     * 스케줄러를 실행 준비 상태로 만든다.
     * 이후 등록 된(또는 등록 될) job과 trigger의 실행 시간을 기다린다.
     * 여기서 job과 trigger의 등록 시점은 중요하지 않다. start() 이전 이어도 되고 이후 이어도 된다.
     */
    scheduler.start();
      
    /**
     * Job 정의
     */
    JobDetail jobDetail = JobBuilder.newJob(SmsQuartzJob.class).withIdentity("SmsQuartzJob").build();
  
    /**
     * Trigger 등록
     * cron : 초 분 시 일 월 요일
     */
    // 
    Trigger trigger = TriggerBuilder.newTrigger().
        withSchedule(CronScheduleBuilder.cronSchedule("0 0 10 ? * MON")).build();
    
    /**
     * JobDetail과 Trigger를 스케줄러에 등록한다.
     * Job과 Trigger가 등록되는 시간 보다 trigger의 시작시간이 이후여야 한다.
     */ 
    scheduler.scheduleJob(jobDetail, trigger);
  }
}
``` 

  
<br><br>  


## Quartz Cron 격주 설정 예제
```java
@Component  // spring bean 등록
@Profile("demo")  // 지정한 profile에서만 실행
public class SchedulerLocalMangement {
	private SchedulerFactory schedulerFactory;
  private Scheduler scheduler;
    
  @PostConstruct
  public void start() throws SchedulerException{
    
    JobDetail receiptDelayLongSmsQuartzJob = JobBuilder.newJob(SmsQuartzJob.class)
      .withIdentity("smsQuartzJob")
      .build();
  
    /**
     * 시간 설정
     * - 월요일 오전 10시로 시작 시간 설정
     */ 
    Calendar startTime = Calendar.getInstance();
    startTime.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
    startTime.set(Calendar.HOUR_OF_DAY, 10);
    startTime.set(Calendar.MINUTE, 0);
    startTime.set(Calendar.SECOND, 0);
    startTime.set(Calendar.MILLISECOND, 0);

    // 설정한 시간이 현재시간 보다 이전일 경우 시작 시간을 다음주로 설정
    if (startTime.getTime().before(new Date())) {
        startTime.add(Calendar.WEEK_OF_YEAR, 1);
    }
    
    
    /**
     * Trigger 등록
     * 초 분 시 일 월 요일
     */ 
    Trigger triggerReceiptDelayLongSms = TriggerBuilder.newTrigger()
        .startAt(startTime.getTime())
        .withSchedule(CalendarIntervalScheduleBuilder.calendarIntervalSchedule()
            .withIntervalInWeeks(2)).build();
//				withSchedule(CronScheduleBuilder.cronSchedule("0 10 10 ? * 1#2, 1#4")).build();
    
    scheduler.scheduleJob(receiptDelayLongSmsQuartzJob, triggerReceiptDelayLongSms);
    
  }
}
``` 