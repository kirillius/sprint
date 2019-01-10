package ru.kirillius.sprint.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ru.kirillius.sprint.R;
import ru.kirillius.sprint.models.TrelloCard;
import ru.kirillius.sprint.service.UserInformationInPhone;

public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.ViewHolder> {

    Context context;
    private ArrayList<TrelloCard> listItem;
    UserInformationInPhone userInformationInPhone;

    public CardsAdapter(Context context, ArrayList<TrelloCard> listItem) {
        this.context=context;
        this.listItem=listItem;
        userInformationInPhone = new UserInformationInPhone(context);
    }

    @Override
    public CardsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.simple_list_item, parent, false);

        CardsAdapter.ViewHolder vh = new CardsAdapter.ViewHolder(v, context);
        return vh;
    }

    @Override
    public void onBindViewHolder(final CardsAdapter.ViewHolder holder, final int position) {

        holder.setItem(listItem.get(position));
        holder.tvName.setText(listItem.get(position).name);

        /*if(userInformationInPhone.getCurrentSprint().equals(listItem.get(position).id))
            holder.ivCheck.setVisibility(View.VISIBLE);*/
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        //ImageView ivCheck;
        private TrelloCard item;
        //OnSelectSprint listener;

        public ViewHolder(View v, Context context) {
            super(v);
            tvName = v.findViewById(R.id.tvName);
            //ivCheck = v.findViewById(R.id.ivCurrentSprint);
            //this.listener = (OnSelectSprint)context;
            //v.setOnClickListener(this);
        }

        public void setItem(TrelloCard item) {
            this.item = item;
        }
    }
}
