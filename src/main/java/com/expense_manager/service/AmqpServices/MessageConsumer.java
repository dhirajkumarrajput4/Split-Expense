package com.expense_manager.service.AmqpServices;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.expense_manager.comman.Job;
import com.expense_manager.entities.Expense;
import com.expense_manager.repository.ExpensesRepoes.ExpenseRepo;
import com.expense_manager.service.ExpenseServices.ExpenseService;

@Service
public class MessageConsumer {

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private ExpenseRepo expenseRepo;


    private Map<String, JobFunction> jobFunctions = new HashMap<String, JobFunction>() {
        {
            put("ExpenseNotificationJob", new JobFunction() {
                @Override
                public void execute(Job job) {
                    Long expenseId = job.getLong("expenseId");
                    Expense expense = expenseRepo.findById(expenseId)
                            .orElseThrow(() -> new RuntimeException("No expense found with this id"));
                    expenseService.sendEmailNotificationToGroupMembers(expense);
                }
            });

            put("TestJob", new JobFunction() {
                @Override
                public void execute(Job job) {
                    String code = (String) job.get("schoolCode");
                    // School school = schoolRepo.findBySchoolCode(code)
                    //         .orElseThrow(() -> new IllegalArgumentException("Cannot find school with code: " + code));
                    // System.out.println("Test job executed for school: " + school);
                }
            });

            // Add other job types similarly
        }
    };

    @RabbitListener(queues = "myQueue")
    public <T> void receivedMessage(Job job) {
        JobFunction func = jobFunctions.get(job.getName());

        if (func == null) {
            System.err.println("Job not recognized, discarding: " + job);
            return;
        }

        try {
            System.out.println("Acquiring lock and processing job: " + job);
            func.execute(job);
            func.postExecute(job);
        } catch (Exception e) {
            System.err.println("Exception during job processing: " + e.getMessage());
            func.onFailure(job);
            throw new RuntimeException(e);
        }
    }

    public abstract class JobFunction {
        public String getLockKey(Job job) {
            return job.getLockKey();
        }

        public abstract void execute(Job job);

        public void postExecute(Job job) {
        }

        public void onFailure(Job job) {
        }
    }

}
