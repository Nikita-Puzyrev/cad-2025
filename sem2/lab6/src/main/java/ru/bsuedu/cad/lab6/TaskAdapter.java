package ru.bsuedu.cad.lab6;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

final class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.Row> {
    private final List<Task> tasks;

    TaskAdapter(List<Task> tasks) {
        this.tasks = tasks;
    }

    @NonNull
    @Override
    public Row onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Row(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Row row, int position) {
        Task task = tasks.get(position);
        row.title.setText(task.title);
        row.description.setText(task.description);
        row.description.setVisibility(task.description == null || task.description.isEmpty() ? View.GONE : View.VISIBLE);
        row.state.setText(task.completed ? R.string.completed : R.string.pending);
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    static final class Row extends RecyclerView.ViewHolder {
        final TextView title;
        final TextView description;
        final TextView state;

        Row(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            description = view.findViewById(R.id.description);
            state = view.findViewById(R.id.state);
        }
    }
}
