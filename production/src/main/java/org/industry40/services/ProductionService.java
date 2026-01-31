package org.industry40.services;

import lombok.extern.slf4j.Slf4j;
import org.industry40.clients.MachineClient;
import org.industry40.config.MQConfig;
import org.industry40.data.JobRepository;
import org.industry40.enums.JobStatus;
import org.industry40.messages.JobFinishedMessage;
import org.industry40.models.ProductionJob;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Slf4j
@Service
public class ProductionService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private ThreadPoolTaskScheduler scheduler;

    @Autowired
    private MachineClient machineClient;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private final Map<Integer, ScheduledFuture<?>> activeSchedules = new ConcurrentHashMap<>();

    public void scheduleJob(ProductionJob job) {
        Instant startWithDelay = Instant.now().plusSeconds(30);
        Date startAt = Date.from(startWithDelay);

        log.info("Scheduling Job #{} with 30s TEST DELAY. Will start at {}", job.getPlanId(), startAt);

        ScheduledFuture<?> scheduledTask = scheduler.schedule(() -> startProduction(job), startAt);
        activeSchedules.put(job.getPlanId(), scheduledTask);
    }

    private void startProduction(ProductionJob job) {
        log.info("--- PRODUCTION STARTED: Job #{} ---", job.getPlanId());

        job.setStatus(JobStatus.IN_PROGRESS);
        jobRepository.save(job);

        job.getMachineIds().forEach(id -> {
            machineClient.allocateMachine(id, job.getEndTime());
        });

        Date endAt = Date.from(job.getEndTime().atZone(ZoneId.systemDefault()).toInstant());
        scheduler.schedule(() -> finishProduction(job), endAt);
    }

    private void finishProduction(ProductionJob job) {
        log.info("--- PRODUCTION COMPLETED: Job #{} ---", job.getPlanId());

        job.getMachineIds().forEach(id -> {
            machineClient.releaseMachine(id);
        });

        job.setStatus(JobStatus.COMPLETED);
        jobRepository.save(job);

        long totalJobs = jobRepository.countByOrderId(job.getOrderId());

        JobFinishedMessage msg = new JobFinishedMessage(
                job.getOrderId(),
                job.getUserId(),
                (int) totalJobs
        );

        rabbitTemplate.convertAndSend(
                MQConfig.NOTIFICATION_EXCHANGE,
                MQConfig.NOTIFICATION_ROUTING_KEY,
                msg
        );

        long completedJobsCount = jobRepository.findByOrderId(job.getOrderId()).stream()
                .filter(j -> j.getStatus() == JobStatus.COMPLETED)
                .count();

        if (completedJobsCount == totalJobs) {
            log.info("FULL ORDER COMPLETED: Order #{} has all {} jobs done. Syncing with Orders Service.",
                    job.getOrderId(), totalJobs);

            rabbitTemplate.convertAndSend(
                    MQConfig.ORDER_COMPLETED_SYNC_EXCHANGE,
                    MQConfig.ORDER_COMPLETED_SYNC_ROUTING_KEY,
                    msg
            );
        }
    }

    public void cancelJob(Integer planId) {
        ProductionJob job = jobRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (job.getStatus() == JobStatus.PENDING) {
            ScheduledFuture<?> scheduledTask = activeSchedules.get(planId);
            if (scheduledTask != null) {
                scheduledTask.cancel(false);
                activeSchedules.remove(planId);
            }

            job.setStatus(JobStatus.CANCELLED);
            jobRepository.save(job);
            log.info("Job #{} cancelled and removed from scheduler.", planId);
        } else {
            log.warn("Job #{} cannot be cancelled because it is in status: {}", planId, job.getStatus());
            throw new IllegalStateException("Job already in progress");
        }
    }
}
