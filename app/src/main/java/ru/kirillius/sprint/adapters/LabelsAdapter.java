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
import ru.kirillius.sprint.models.Labels;
import ru.kirillius.sprint.service.UserInformationInPhone;

public class LabelsAdapter extends RecyclerView.Adapter<LabelsAdapter.ViewHolder> {
    Context context;
    private ArrayList<Labels> listItem;
    UserInformationInPhone userInformationInPhone;
    OnSelectLabel listener;

    public LabelsAdapter(Context context, ArrayList<Labels> listItem) {
        this.context=context;
        this.listItem=listItem;
        this.listener = (OnSelectLabel)context;
        userInformationInPhone = new UserInformationInPhone(context);
    }

    @Override
    public LabelsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.simple_list_item, parent, false);

        LabelsAdapter.ViewHolder vh = new LabelsAdapter.ViewHolder(v, context);
        return vh;
    }

    @Override
    public void onBindViewHolder(final LabelsAdapter.ViewHolder holder, final int position) {

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

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvName;
        private Labels item;
        OnSelectLabel listener;

        public ViewHolder(View v, Context context) {
            super(v);
            tvName = v.findViewById(R.id.tvName);
            this.listener = (OnSelectLabel) context;
            v.setOnClickListener(this);
        }

        public void setItem(Labels item) {
            this.item = item;
        }

        @Override
        public void onClick(View v) {
            listener.onSelectLabel(item);
        }
    }
}
