package com.meditrack.ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.meditrack.R;
import com.meditrack.auth.AuthManager;
import com.meditrack.data.model.Medication;
import com.meditrack.service.MedicationReminderService;
import com.meditrack.ui.adapter.MedicationAdapter;
import com.meditrack.ui.viewmodel.MedicationViewModel;

public class MainActivity extends AppCompatActivity implements MedicationAdapter.MedicationClickListener {
    private MedicationViewModel viewModel;
    private LocalAuthManager authManager;
    private MedicationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize ViewModel and AuthManager
        viewModel = new ViewModelProvider(this).get(MedicationViewModel.class);
        authManager = LocalAuthManager.getInstance(this);

        // Check authentication
        if (authManager.getCurrentUserId() == null) {
            startActivity(new Intent(this, AuthActivity.class));
            finish();
            return;
        }

        // Start foreground service
        startService(new Intent(this, MedicationReminderService.class));

        // Setup UI
        setupRecyclerView();
        setupAddButton();
        observeMedications();
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.medications_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MedicationAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    private void setupAddButton() {
        FloatingActionButton fab = findViewById(R.id.fab_add_medication);
        fab.setOnClickListener(view -> {
            startActivity(new Intent(this, AddMedicationActivity.class));
        });
    }

    private void observeMedications() {
        viewModel.getMedications().observe(this, medications -> {
            adapter.submitList(medications);
        });
    }

    @Override
    public void onEditClick(Medication medication) {
        Intent intent = new Intent(this, AddMedicationActivity.class);
        intent.putExtra("medication_id", medication.getId());
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(Medication medication) {
        viewModel.deleteMedication(medication);
    }

    @Override
    public void onEnableChanged(Medication medication, boolean isEnabled) {
        medication.setEnabled(isEnabled);
        viewModel.updateMedication(medication);
    }
}
