package de.karzek.diettracker.presentation.main.cookbook.dialog.filterOptionsDialog.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;

import de.karzek.diettracker.R;
import de.karzek.diettracker.presentation.main.cookbook.dialog.filterOptionsDialog.adapter.viewHolder.RecipeFilterOptionViewHolder;

/**
 * Created by MarjanaKarzek on 30.05.2018.
 *
 * @author Marjana Karzek
 * @version 1.0
 * @date 30.05.2018
 */
public class RecipeFilterOptionsListAdapter extends RecyclerView.Adapter<RecipeFilterOptionViewHolder> {

    private RecipeFilterOptionViewHolder.OnItemCheckedChangeListener onItemCheckedChangeListener;

    private ArrayList<String> options;
    private HashMap<String, Boolean> status;

    public RecipeFilterOptionsListAdapter(RecipeFilterOptionViewHolder.OnItemCheckedChangeListener onItemCheckedChangeListener) {
        options = new ArrayList<>();
        status = new HashMap<>();
        this.onItemCheckedChangeListener = onItemCheckedChangeListener;
    }

    @NonNull
    @Override
    public RecipeFilterOptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecipeFilterOptionViewHolder(LayoutInflater.from(
                parent.getContext()).inflate(R.layout.item_dialog_checkbox, parent, false
        ), onItemCheckedChangeListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeFilterOptionViewHolder holder, int position) {
        holder.bind(options.get(position), status.get(options.get(position)));
    }

    @Override
    public int getItemCount() {
        return options.size();
    }

    public void setList(ArrayList<String> options, HashMap<String, Boolean> optionStatus) {
        this.options = options;
        this.status = optionStatus;
        notifyDataSetChanged();
    }
}
