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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import ru.kirillius.sprint.R;
import ru.kirillius.sprint.adapters.SprintsAdapter;
import ru.kirillius.sprint.decorators.SimpleDividerItemDecoration;
import ru.kirillius.sprint.interfaces.OnCompleteRequest;
import ru.kirillius.sprint.models.Sprint;
import ru.kirillius.sprint.models.TrelloUserInfo;
import ru.kirillius.sprint.service.RequestHelper;

public class CurrentSprintChoiceFragment extends Fragment {

    private RecyclerView.LayoutManager mLayoutManager;
    RecyclerView rvListSprints;
    ArrayList<Sprint> listItems;
    SprintsAdapter adapter;
    GsonBuilder builder = new GsonBuilder();
    Gson gson = builder.create();
    int countBoards = 0;

    public CurrentSprintChoiceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_current_sprint_choice, container, false);
        rvListSprints = (RecyclerView)view.findViewById(R.id.rvListSprints);
        listItems = new ArrayList<>();
        mLayoutManager = new LinearLayoutManager(getActivity());
        rvListSprints.setLayoutManager(mLayoutManager);
        rvListSprints.setHasFixedSize(true);
        rvListSprints.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));

        String method = "members/"+getActivity().getResources().getString(R.string.trello_user);
        new RequestHelper(getActivity()).executeGetRequest(method, null, new OnCompleteRequest() {
            @Override
            public void onComplete(String json) {
                //получили список досок юзера, возьмем их id и получим их имена
                final TrelloUserInfo userInfo = gson.fromJson(json, TrelloUserInfo.class);
                for(String boardId: userInfo.idBoards) {
                    final Sprint sprintItem = new Sprint();
                    sprintItem.id = boardId;
                    listItems.add(sprintItem);
                    new RequestHelper(getActivity()).executeGetRequest("boards/" + boardId, null, new OnCompleteRequest() {
                        @Override
                        public void onComplete(String json) {
                            Sprint parseSprint = gson.fromJson(json, Sprint.class);
                            sprintItem.name = parseSprint.name;
                            countBoards++;
                            if(countBoards>=userInfo.idBoards.length) {
                                adapter = new SprintsAdapter(getActivity(), listItems);
                                rvListSprints.setAdapter(adapter);
                            }
                        }
                    });
                }
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }
}
