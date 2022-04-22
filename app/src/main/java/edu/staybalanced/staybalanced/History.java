package edu.staybalanced.staybalanced;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class History extends Fragment {
    //TODO: add code for making graph
    RecyclerView recyclerView;
    ArrayList<ExerciseItem> exercises;
    ExerciseAdapter exerciseAdapter;
    MenuItem menuItem;
    SearchView searchView;

    public History() {
        // Required empty public constructor
        super(R.layout.fragment_history);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Indicate the Fragment wants to contribute to the Host Activity's options menu
        setHasOptionsMenu(true);

        // initialize recyclerview
        recyclerView = view.findViewById(R.id.recycler_view);

        exercises = new ArrayList<>();

        // get database exercise items
        DatabaseHelper databaseHelper = new DatabaseHelper(view.getContext());
        exercises = databaseHelper.getAllExerciseItems();

        //adding default exercises if db is empty
        if (exercises.isEmpty()) {
            databaseHelper.addExercise(new Exercises(-1, "Wall Squat", "Squat with back to the wall", 1, 1, 1 , R.drawable.eicon_squat));
            databaseHelper.addExercise(new Exercises(-1, "Plank", "Holding body straight off the ground", 1, 1,1, R.drawable.eicon_plank));
            databaseHelper.addExercise(new Exercises(-1, "Bicep Curl Hold", "Hold dumbbell in position", 1, 1, 1, R.drawable.eicon_b_curl));
            exercises = databaseHelper.getAllExerciseItems();
        }

        exerciseAdapter = new ExerciseAdapter(exercises,view.getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(exerciseAdapter);

        // TODO: Search bar interaction not optimal.  Opening and closing is clunky.
        FloatingActionButton searchButton = view.findViewById(R.id.hist_fab);
        searchButton.setOnClickListener(fab -> {
            if (menuItem.isActionViewExpanded()) {
                menuItem.collapseActionView();
            }
            menuItem.expandActionView();
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        // inflate the menu layout
        inflater.inflate(R.menu.menu_search, menu);

        super.onCreateOptionsMenu(menu, inflater);

        // set the menu item
        menuItem = menu.findItem(R.id.action_search);
        // get the action view from the menu item and set as a search view
        searchView = (SearchView) menuItem.getActionView();

        // set the search filter
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                exerciseAdapter.getFilter().filter(newText);
                // false if the SearchView should perform the default action of showing any suggestions if available
                // KEEP FALSE
                return false;
            }
        });
    }
}