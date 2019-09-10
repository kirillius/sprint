package ru.kirillius.sprint.service;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import ru.kirillius.sprint.R;
import ru.kirillius.sprint.adapters.LabelsAdapter;
import ru.kirillius.sprint.interfaces.OnCompleteAction;
import ru.kirillius.sprint.interfaces.OnCompleteRequest;
import ru.kirillius.sprint.interfaces.OnSaveLabel;
import ru.kirillius.sprint.interfaces.OnSaveTask;
import ru.kirillius.sprint.interfaces.OnSaveTimesTask;
import ru.kirillius.sprint.models.Labels;
import ru.kirillius.sprint.models.Tasks;

/**
 * Created by Lavrentev on 25.12.2017.
 */

public class NotificationsHelper {
    public static void DialogLabel(Context context, LayoutInflater inflater, String title, final Labels label, final OnSaveLabel listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        View viewDialog = inflater.inflate(R.layout.dialog_labels, null);
        TextView tvTitleDialog;
        final AppCompatEditText name;
        Button btnOk, btnCancel;

        tvTitleDialog = viewDialog.findViewById(R.id.tvTitleDialog);
        name = viewDialog.findViewById(R.id.name);
        btnOk = viewDialog.findViewById(R.id.btnOk);
        btnCancel = viewDialog.findViewById(R.id.btnCancel);

        if (title != null)
            tvTitleDialog.setText(CommonHelper.FilterNullValues(title));

        if(label!=null) {
            name.setText(label.name);
        }

        builder.setView(viewDialog);
        final AlertDialog dialog = builder.create();
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    Labels savedLabel = new Labels(5, name.getText().toString());

                    if(label!=null) {
                        savedLabel.id = label.id;
                        savedLabel.name = name.getText().toString();
                    }

                    listener.onSaveLabel(savedLabel);
                    dialog.dismiss();
                } else
                    dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onNotSaveLabel();
                    dialog.dismiss();
                } else
                    dialog.dismiss();
            }
        });

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                if (listener != null)
                    listener.onNotSaveLabel();
            }
        });

        if(!((Activity) context).isFinishing())
        {
            dialog.show();
        }
    }

    public static void DialogTask(final Context context, LayoutInflater inflater, String title, final Tasks task, final OnSaveTask listener) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        final View viewDialog = inflater.inflate(R.layout.dialog_tasks, null);
        TextView tvTitleDialog;
        final AppCompatEditText name, description, deadline;
        final Button btnOk, btnCancel;
        final Spinner spLabel = viewDialog.findViewById(R.id.label);

        tvTitleDialog = viewDialog.findViewById(R.id.tvTitleDialog);
        btnOk = viewDialog.findViewById(R.id.btnOk);
        btnCancel = viewDialog.findViewById(R.id.btnCancel);
        name = viewDialog.findViewById(R.id.name);
        description = viewDialog.findViewById(R.id.description);
        deadline = viewDialog.findViewById(R.id.deadline);

        if (title != null)
            tvTitleDialog.setText(CommonHelper.FilterNullValues(title));

        if(task!=null) {
            name.setText(task.name);
            description.setText(task.description);
            deadline.setText(task.deadline);
        }

        RequestHelper requestHelper = new RequestHelper(context);
        requestHelper.executeGetRequest("/api/labels", null , new OnCompleteRequest() {
            @Override
            public void onComplete(String json) {
                GsonBuilder builderGson = new GsonBuilder();
                Gson gson = builderGson.create();
                Type listType = new TypeToken<ArrayList<Labels>>(){}.getType();
                ArrayList<Labels> listItems = gson.fromJson(json, listType);

                String[] labels = new String[listItems.size()];
                int i=0;
                for(Labels label: listItems) {
                    labels[i] = label.name;
                    i++;
                }

                ArrayAdapter<String> adapter = new  ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, labels);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spLabel.setAdapter(adapter);

                spLabel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               View itemSelected, int selectedItemPosition, long selectedId) {
                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

                builder.setView(viewDialog);
                final AlertDialog dialog = builder.create();
                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (listener != null) {
                            Tasks savedTask = new Tasks();
                            if(task!=null) {
                                savedTask = task;
                            }
                            savedTask.name = name.getText().toString();
                            savedTask.description = description.getText().toString();
                            savedTask.deadline = deadline.getText().toString();
                            savedTask.labelId = 1;
                            listener.onSaveTask(savedTask);
                            dialog.dismiss();
                        } else
                            dialog.dismiss();
                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (listener != null) {
                            listener.onNotSaveTask();
                            dialog.dismiss();
                        } else
                            dialog.dismiss();
                    }
                });

                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        if (listener != null)
                            listener.onNotSaveTask();
                    }
                });

                if(!((Activity) context).isFinishing())
                {
                    dialog.show();
                }
            }
        });
    }

    public static void DialogTimes(Context context, LayoutInflater inflater, String title, final Tasks task, final boolean start, final OnSaveTimesTask listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        View viewDialog = inflater.inflate(R.layout.dialog_times_task, null);
        TextView tvTitleDialog, tvNameTask, tvDescriptionTask;
        Button btnStartStopTask, btnCancel;

        tvTitleDialog = viewDialog.findViewById(R.id.tvTitleDialog);
        tvNameTask = viewDialog.findViewById(R.id.tvNameTask);
        tvDescriptionTask = viewDialog.findViewById(R.id.tvDescriptionTask);
        btnStartStopTask = viewDialog.findViewById(R.id.btnStartStopTask);
        btnCancel = viewDialog.findViewById(R.id.btnCancel);

        if (title != null)
            tvTitleDialog.setText(CommonHelper.FilterNullValues(title));

        tvNameTask.setText(task.name);
        tvDescriptionTask.setText(task.description);
        btnStartStopTask.setText(start ? "В работу" : "Остановить работу");

        builder.setView(viewDialog);
        final AlertDialog dialog = builder.create();
        btnStartStopTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onSaveTimesTask(task, start);
                    dialog.dismiss();
                } else
                    dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if (listener != null) {
                    dialog.dismiss();
                } else*/
                    dialog.dismiss();
            }
        });

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                //if (listener != null)
                    //listener.onNotSaveLabel();

                dialog.dismiss();
            }
        });

        if(!((Activity) context).isFinishing())
        {
            dialog.show();
        }
    }
}
