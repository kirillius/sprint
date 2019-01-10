package ru.kirillius.sprint.activities.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import ru.kirillius.sprint.R;
import ru.kirillius.sprint.adapters.CardsAdapter;
import ru.kirillius.sprint.decorators.SimpleDividerItemDecoration;
import ru.kirillius.sprint.interfaces.OnCompleteRequest;
import ru.kirillius.sprint.models.TrelloCard;
import ru.kirillius.sprint.service.RequestHelper;
import ru.kirillius.sprint.service.UserInformationInPhone;


public class TodayTasksFragment extends Fragment {

    UserInformationInPhone userInformationInPhone;
    TextView tvNotChoiceSprint;
    RecyclerView rvListTasks;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<TrelloCard> listItems;
    CardsAdapter adapter;
    GsonBuilder builder = new GsonBuilder();
    Gson gson = builder.create();

    public TodayTasksFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_today_tasks, container, false);
        userInformationInPhone = new UserInformationInPhone(getActivity());
        rvListTasks = view.findViewById(R.id.rvListTasks);
        tvNotChoiceSprint = view.findViewById(R.id.tvNotChoiceSprint);
        listItems = new ArrayList<>();
        mLayoutManager = new LinearLayoutManager(getActivity());
        rvListTasks.setLayoutManager(mLayoutManager);
        //rvListTasks.setHasFixedSize(true);
        rvListTasks.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));

        if(userInformationInPhone.getCurrentSprint()!=null) {
            String method = "boards/" + userInformationInPhone.getCurrentSprint() + "/cards";
            new RequestHelper(getActivity()).executeGetRequest(method, null, new OnCompleteRequest() {
                @Override
                public void onComplete(String json) {
                    Type listType = new TypeToken<ArrayList<TrelloCard>>(){}.getType();
                    listItems = gson.fromJson(json, listType);
                    adapter = new CardsAdapter(getActivity(), listItems);
                    rvListTasks.setAdapter(adapter);
                    tvNotChoiceSprint.setVisibility(View.GONE);

                    /*ConstraintLayout.LayoutParams params = new
                            ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                    // Set the height by params
                    params.height=70*listItems.size();
                    // set height of RecyclerView
                    rvListTasks.setLayoutParams(params);*/
                    rvListTasks.setVisibility(View.VISIBLE);
                }
            });
        }
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
