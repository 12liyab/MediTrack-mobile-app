package com.meditrack.worker;

import android.content.Context;
import android.app.NotificationManager;
import android.app.NotificationChannel;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import com.meditrack.data.MedicationRepository;
import com.meditrack.data.model.Medication;

public class MedicationReminderWorker extends Worker {
    private static final String CHANNEL_ID = "medication_reminders";
    private final Context context;
    private final MedicationRepository repository;

    public MedicationReminderWorker(
        @NonNull Context context,
        @NonNull WorkerParameters params) {
        super(context, params);
        this.context = context;
        this.repository = new MedicationRepository(context);
        createNotificationChannel();
    }

    @NonNull
    @Override
    public Result doWork() {
        repository.getActiveMedications(new MedicationRepository.RepositoryCallback<java.util.List<Medication>>() {
            @Override
            public void onSuccess(java.util.List<Medication> medications) {
                for (Medication medication : medications) {
                    if (shouldShowReminder(medication)) {
                        showNotification(medication);
                    }
                }
            }

            @Override
            public void onError(Exception e) {
                return;
            }
        });

        return Result.success();
    }

    private boolean shouldShowReminder(Medication medication) {
        // Check if it's time to show the reminder based on medication schedule
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        return medication.getReminderTime().isBefore(now) || medication.getReminderTime().isEqual(now);
    }

    private void showNotification(Medication medication) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Medication Reminder")
            .setContentText("Time to take " + medication.getName() + " - " + medication.getDosage())
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true);

        NotificationManager notificationManager = 
            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify((int) medication.getId(), builder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "Medication Reminders",
                NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Reminders for taking medications");
            
            NotificationManager notificationManager = 
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
