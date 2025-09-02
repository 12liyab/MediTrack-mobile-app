package com.meditrack.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.textfield.TextInputEditText;
import com.meditrack.R;
import com.meditrack.data.model.Medication;
import com.meditrack.ui.viewmodel.MedicationViewModel;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class AddMedicationActivity extends AppCompatActivity {
    private TextInputEditText nameInput;
    private TextInputEditText dosageInput;
    private RadioGroup frequencyRadioGroup;
    private TimePicker timePicker;
    private Button saveButton;
    private MedicationViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medication);

        viewModel = new ViewModelProvider(this).get(MedicationViewModel.class);
        initializeViews();
        setupListeners();
    }

    private void initializeViews() {
        nameInput = findViewById(R.id.medication_name_input);
        dosageInput = findViewById(R.id.dosage_input);
        frequencyRadioGroup = findViewById(R.id.frequency_radio_group);
        timePicker = findViewById(R.id.reminder_time_picker);
        saveButton = findViewById(R.id.save_medication_button);
    }

    private void setupListeners() {
        saveButton.setOnClickListener(v -> saveMedication());
    }

    private void saveMedication() {
        String name = nameInput.getText().toString().trim();
        String dosage = dosageInput.getText().toString().trim();
        String frequency = getSelectedFrequency();
        LocalDateTime reminderTime = getSelectedTime();

        if (validateInput(name, dosage)) {
            Medication medication = new Medication(name, dosage, frequency, reminderTime, null);
            viewModel.addMedication(medication);
            finish();
        }
    }

    private String getSelectedFrequency() {
        int selectedId = frequencyRadioGroup.getCheckedRadioButtonId();
        if (selectedId == R.id.frequency_daily) {
            return "daily";
        } else if (selectedId == R.id.frequency_weekly) {
            return "weekly";
        } else {
            return "custom";
        }
    }

    private LocalDateTime getSelectedTime() {
        LocalTime time = LocalTime.of(timePicker.getHour(), timePicker.getMinute());
        return LocalDateTime.now().with(time);
    }

    private boolean validateInput(String name, String dosage) {
        if (name.isEmpty()) {
            showError("Medication name is required");
            return false;
        }

        if (dosage.isEmpty()) {
            showError("Dosage is required");
            return false;
        }

        return true;
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
