package se.liu.student.frejo105.beerapp.fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import se.liu.student.frejo105.beerapp.R;
import se.liu.student.frejo105.beerapp.utility.Utility;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends DialogFragment
implements DialogInterface.OnClickListener {

    View settingsView;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View w = getActivity().getLayoutInflater().inflate(R.layout.dialog_settings, null);

        setupView(w);
        settingsView = w;

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.settings)
                .setPositiveButton(R.string.save, this)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // On cancel, there's nothing for us to do manually
                    }
                })
                .setView(w);
        return builder.create();
    }

    private void setupView(View view) {
        SharedPreferences settings = getContext().getSharedPreferences("settings", Context.MODE_PRIVATE);

        Spinner s = (Spinner)view.findViewById(R.id.distance_unit_selector);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.distance_units, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
        s.setSelection(settings.getBoolean("isKm", true) ? 1 : 0);
        ((CheckBox)view.findViewById(R.id.include_tested_input)).setChecked(settings.getBoolean("includeTested", true));
        EditText input = ((EditText)view.findViewById(R.id.distance_input));
        input.setText(Integer.toString(settings.getInt("distance", 20)), TextView.BufferType.EDITABLE);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        boolean includeTested = ((CheckBox)settingsView.findViewById(R.id.include_tested_input)).isChecked();
        boolean isKm = ((Spinner)settingsView.findViewById(R.id.distance_unit_selector)).getSelectedItem().equals("km");
        String distanceS = ((EditText)settingsView.findViewById(R.id.distance_input)).getText().toString();
        int distance = Utility.isNullEmptyOrWhitespace(distanceS) ? -1 : Integer.parseInt(distanceS);

        SharedPreferences.Editor settings = getContext().getSharedPreferences("settings", Context.MODE_PRIVATE).edit();
        settings.putBoolean("isKm", isKm);
        settings.putBoolean("includeTested", includeTested);
        if (distance > 0) settings.putInt("distance", distance);
        settings.apply();
    }
}
