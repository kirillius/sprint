package ru.kirillius.sprint.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ru.kirillius.sprint.R;
import ru.kirillius.sprint.interfaces.OnSelectLabel;
import ru.kirillius.sprint.interfaces.OnSelectTask;
import ru.kirillius.sprint.models.Tasks;
import ru.kirillius.sprint.service.UserInformationInPhone;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.ViewHolder> {
    Context context;
    private ArrayList<Tasks> listItem;
    UserInformationInPhone userInformationInPhone;
    OnSelectTask listener;

    public TasksAdapter(Context context, ArrayList<Tasks> listItem) {
        this.context=context;
        this.listItem=listItem;
        this.listener = (OnSelectTask) context;
        userInformationInPhone = new UserInformationInPhone(context);
    }

    @Override
    public TasksAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.simple_list_item, parent, false);

        TasksAdapter.ViewHolder vh = new TasksAdapter.ViewHolder(v, context);
        return vh;
    }

    @Override
    public void onBindViewHolder(final TasksAdapter.ViewHolder holder, final int position) {

        holder.setItem(listItem.get(position));
        holder.tvName.setText(listItem.get(position).name);
    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView tvName, tvDescription, tvDeadline;
        private Tasks item;
        OnSelectTask listener;

        public ViewHolder(View v, Context context) {
            super(v);
            tvName = v.findViewById(R.id.tvName);
            //tvDescription = v.findViewById(R.id.tvDescription);
            //tvDeadline = v.findViewById(R.id.tvDeadline);
            this.listener = (OnSelectTask) context;
            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
        }

        public void setItem(Tasks item) {
            this.item = item;
        }

        @Override
        public void onClick(View v) {
            listener.onSelectTask(item);
        }

        @Override
        public boolean onLongClick(View v) {
            listener.onLongSelectTask(item);
            return false;
        }
    }
}
