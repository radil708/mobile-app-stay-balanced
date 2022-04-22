package edu.staybalanced.staybalanced;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class AddExercise extends Fragment {
    AutoCompleteTextView name;
    EditText desc, reps, sets, secs;

    public AddExercise() {
        // Required empty public constructor
        super(R.layout.fragment_add_exercise);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_exercise, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        name = view.findViewById(R.id.add_name);
        desc = view.findViewById(R.id.add_desc);
        reps = view.findViewById(R.id.add_reps);
        sets = view.findViewById(R.id.add_sets);
        secs = view.findViewById(R.id.add_secs);

        // replaced list with array of names retrieved from database
        // String[] exampleNameList = new String[] { "Plank", "Wall Sits" };

        DatabaseHelper databaseHelper = new DatabaseHelper(view.getContext());
        ArrayList<String> exampleNames = databaseHelper.getAllExerciseNames();
        ArrayAdapter<String> nameAutocomplete = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_list_item_1, exampleNames);
        name.setAdapter(nameAutocomplete);

        Button add = view.findViewById(R.id.add_button);
        add.setOnClickListener(addButton -> {
            // TODO: Confirm if keeping Toasts
            Exercises new_exercise;
            try {
                new_exercise = new Exercises(-1,
                        name.getText().toString(),
                        desc.getText().toString(),
                        Integer.parseInt(sets.getText().toString()),
                        Integer.parseInt(reps.getText().toString()),
                        Integer.parseInt(secs.getText().toString()),
                        // temporarily setting default image
                        // TODO: add icon selection feature if we have time
                        R.drawable.eicon_b_curl
                );
                Toast.makeText(view.getContext(), "New exercise " + name.getText().toString() + " created", Toast.LENGTH_SHORT).show();
            }
            catch (Exception e) {
                Toast.makeText(view.getContext(), "Error adding exercise", Toast.LENGTH_SHORT).show();
                new_exercise = new Exercises(-1, "error", "error while adding exercise", 0, 0, 0, 0);
            }
            boolean success = databaseHelper.addExercise(new_exercise);

            Toast.makeText(view.getContext(), "Success = " + success, Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        // When the Fragment is in view, automatically focus on the name field and bring up the
        // keyboard to allow seamless user interaction.
        // Source: https://stackoverflow.com/a/39144104/13084818
        name.requestFocus();
        name.postDelayed(() -> {
            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(getContext().INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(name, InputMethodManager.SHOW_IMPLICIT);
        }, 150);
    }
}
