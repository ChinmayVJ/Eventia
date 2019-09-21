package com.example.loginactivity.Classes;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginactivity.R;

public class DataViewHolder extends RecyclerView.ViewHolder{

    View dataView;

    public DataViewHolder(@NonNull View itemView) {
        super(itemView);
        dataView = itemView;
    }

    public View getDataView(){
        return dataView;
    }

    public void setEventHandlerName(String eventHandlerName){
        TextView event_handler_name = dataView.findViewById(R.id.event_handler_name);
        event_handler_name.setText(eventHandlerName);
    }

    public void setEventName(String eventName){
        TextView event_name = dataView.findViewById(R.id.event_name);
        event_name.setText(eventName);
    }

    public void setEventTime(String eventTime){
        TextView event_time = dataView.findViewById(R.id.event_time);
        event_time.setText(eventTime);
    }

    public void setEventLocation(String eventLocation){
        TextView event_location = dataView.findViewById(R.id.event_location);
        event_location.setText(eventLocation);
    }

    public void setNoOfMembers(String noOfMembers){
        TextView no_of_members = dataView.findViewById(R.id.no_of_mems);
        no_of_members.setText(noOfMembers);
    }
}

