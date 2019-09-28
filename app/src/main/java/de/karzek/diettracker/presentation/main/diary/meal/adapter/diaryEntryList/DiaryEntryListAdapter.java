package de.karzek.diettracker.presentation.main.diary.meal.adapter.diaryEntryList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import de.karzek.diettracker.R;
import de.karzek.diettracker.presentation.main.diary.meal.adapter.diaryEntryList.viewHolder.DiaryEntryViewHolder;
import de.karzek.diettracker.presentation.model.DiaryEntryDisplayModel;

/**
 * Created by MarjanaKarzek on 30.05.2018.
 *
 * @author Marjana Karzek
 * @version 1.0
 * @date 30.05.2018
 */
public class DiaryEntryListAdapter extends RecyclerView.Adapter<DiaryEntryViewHolder> {

    private DiaryEntryViewHolder.OnDiaryEntryItemClickedListener onItemClickListener;
    private DiaryEntryViewHolder.OnDeleteDiaryEntryItemListener onItemDeleteListener;
    private DiaryEntryViewHolder.OnEditDiaryEntryItemListener onItemEditListener;
    private DiaryEntryViewHolder.OnMoveDiaryEntryItemListener onItemMoveListener;

    private ArrayList<DiaryEntryDisplayModel> list;

    public DiaryEntryListAdapter(DiaryEntryViewHolder.OnDiaryEntryItemClickedListener onItemClickListener,
                                 DiaryEntryViewHolder.OnDeleteDiaryEntryItemListener onItemDeleteListener,
                                 DiaryEntryViewHolder.OnEditDiaryEntryItemListener onItemEditListener,
                                 DiaryEntryViewHolder.OnMoveDiaryEntryItemListener onItemMoveListener) {
        list = new ArrayList<>();

        this.onItemClickListener = onItemClickListener;
        this.onItemDeleteListener = onItemDeleteListener;
        this.onItemEditListener = onItemEditListener;
        this.onItemMoveListener = onItemMoveListener;
    }

    @NonNull
    @Override
    public DiaryEntryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DiaryEntryViewHolder(LayoutInflater.from(
                parent.getContext()).inflate(R.layout.item_diary_grocery, parent, false
        ), onItemClickListener, onItemDeleteListener, onItemMoveListener, onItemEditListener);
    }

    @Override
    public void onBindViewHolder(@NonNull DiaryEntryViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(ArrayList<DiaryEntryDisplayModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }
}
