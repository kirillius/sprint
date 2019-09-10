package ru.kirillius.sprint.activities.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import ru.kirillius.sprint.R;
import ru.kirillius.sprint.adapters.LabelsAdapter;
import ru.kirillius.sprint.decorators.SimpleDividerItemDecoration;
import ru.kirillius.sprint.interfaces.OnCompleteRequest;
import ru.kirillius.sprint.models.Labels;
import ru.kirillius.sprint.service.RequestHelper;
import ru.kirillius.sprint.service.UserInformationInPhone;

public class LabelsFragment extends Fragment {

    UserInformationInPhone userInformationInPhone;
    TextView tvEmpty;
    RecyclerView rvListLabels;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<Labels> listItems;
    LabelsAdapter adapter;
    GsonBuilder builder = new GsonBuilder();
    Gson gson = builder.create();
    RequestHelper requestHelper;
    Context context;

    public LabelsFragment() {
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
        View view = inflater.inflate(R.layout.fragment_labels, container, false);
        context = getActivity();
        userInformationInPhone = new UserInformationInPhone(getActivity());
        rvListLabels = view.findViewById(R.id.rvListLabels);
        tvEmpty = view.findViewById(R.id.tvEmpty);
        listItems = new ArrayList<>();
        mLayoutManager = new LinearLayoutManager(getActivity());
        rvListLabels.setLayoutManager(mLayoutManager);
        rvListLabels.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));

        requestHelper = new RequestHelper(context);
        /*listItems.add(new Labels(1, "Интерсол"));
        listItems.add(new Labels(2, "Е2"));
        listItems.add(new Labels(3, "Наука"));
        listItems.add(new Labels(4, "Личные проекты"));

        adapter = new LabelsAdapter(getActivity(), listItems);
        rvListLabels.setAdapter(adapter);
        tvEmpty.setVisibility(View.GONE);
        rvListLabels.setVisibility(View.VISIBLE);*/
        requestHelper.executeGetRequest("/api/labels", null , new OnCompleteRequest() {
            @Override
            public void onComplete(String json) {
                Type listType = new TypeToken<ArrayList<Labels>>(){}.getType();
                listItems = gson.fromJson(json, listType);
                adapter = new LabelsAdapter(getActivity(), listItems);
                rvListLabels.setAdapter(adapter);

                if(listItems.size()>0) {
                    tvEmpty.setVisibility(View.GONE);
                    rvListLabels.setVisibility(View.VISIBLE);
                }
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void addLabelToList(Labels label) {
        listItems.add(label);
        adapter.notifyDataSetChanged();
    }

    public void editLabelToList(Labels label) {
        int indexEditedLabel = listItems.indexOf(label);
        listItems.set(indexEditedLabel, label);
        adapter.notifyDataSetChanged();
    }
}
