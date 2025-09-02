package com.meditrack.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.meditrack.R;
import com.meditrack.data.model.Medication;
import java.time.format.DateTimeFormatter;

public class MedicationAdapter extends ListAdapter<Medication, MedicationAdapter.MedicationViewHolder> {
    private final MedicationClickListener listener;
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("hh:mm a");

    public MedicationAdapter(MedicationClickListener listener) {
        super(new MedicationDiffCallback());
        this.listener = listener;
    }

    @NonNull
    @Override
    public MedicationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_medication, parent, false);
        return new MedicationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicationViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    class MedicationViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameText;
        private final TextView dosageText;
        private final TextView timeText;
        private final SwitchMaterial enabledSwitch;
        private final MaterialButton editButton;
        private final MaterialButton deleteButton;

        MedicationViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.medication_name);
            dosageText = itemView.findViewById(R.id.medication_dosage);
            timeText = itemView.findViewById(R.id.medication_time);
            enabledSwitch = itemView.findViewById(R.id.medication_enabled_switch);
            editButton = itemView.findViewById(R.id.edit_button);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }

        void bind(Medication medication) {
            nameText.setText(medication.getName());
            dosageText.setText(medication.getDosage());
            timeText.setText(medication.getReminderTime().format(TIME_FORMATTER));
            enabledSwitch.setChecked(medication.isEnabled());

            enabledSwitch.setOnCheckedChangeListener((buttonView, isChecked) ->
                listener.onEnableChanged(medication, isChecked));

            editButton.setOnClickListener(v -> listener.onEditClick(medication));
            deleteButton.setOnClickListener(v -> listener.onDeleteClick(medication));
        }
    }

    public interface MedicationClickListener {
        void onEditClick(Medication medication);
        void onDeleteClick(Medication medication);
        void onEnableChanged(Medication medication, boolean isEnabled);
    }

    private static class MedicationDiffCallback extends DiffUtil.ItemCallback<Medication> {
        @Override
        public boolean areItemsTheSame(@NonNull Medication oldItem, @NonNull Medication newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Medication oldItem, @NonNull Medication newItem) {
            return oldItem.getName().equals(newItem.getName()) &&
                   oldItem.getDosage().equals(newItem.getDosage()) &&
                   oldItem.getReminderTime().equals(newItem.getReminderTime()) &&
                   oldItem.isEnabled() == newItem.isEnabled();
        }
    }
}
