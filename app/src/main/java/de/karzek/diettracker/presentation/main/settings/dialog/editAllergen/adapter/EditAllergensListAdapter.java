package de.karzek.diettracker.presentation.main.settings.dialog.editAllergen.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

import de.karzek.diettracker.R;
import de.karzek.diettracker.presentation.main.settings.dialog.editAllergen.adapter.viewHolder.AllergenViewHolder;
import de.karzek.diettracker.presentation.model.AllergenDisplayModel;

/**
 * Created by MarjanaKarzek on 30.05.2018.
 *
 * @author Marjana Karzek
 * @version 1.0
 * @date 30.05.2018
 */
public class EditAllergensListAdapter extends RecyclerView.Adapter<AllergenViewHolder> {

    private AllergenViewHolder.OnItemCheckedChangeListener onItemCheckedChangeListener;

    private ArrayList<AllergenDisplayModel> list;
    private HashMap<Integer, Boolean> status;

    public EditAllergensListAdapter(AllergenViewHolder.OnItemCheckedChangeListener onItemCheckedChangeListener) {
        list = new ArrayList<>();
        this.onItemCheckedChangeListener = onItemCheckedChangeListener;
    }

    @NonNull
    @Override
    public AllergenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AllergenViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dialog_checkbox, parent, false), onItemCheckedChangeListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AllergenViewHolder holder, int position) {
        holder.bind(list.get(position), status.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setAllergens(ArrayList<AllergenDisplayModel> allergens, HashMap<Integer, Boolean> allergenStatus) {
        this.list = allergens;
        this.status = allergenStatus;
        notifyDataSetChanged();
    }
}
