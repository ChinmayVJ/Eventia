package com.example.loginactivity.Classes;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import com.example.loginactivity.R;

import java.util.ArrayList;

public class EventAdapterDataHolder extends RecyclerView.Adapter<EventAdapterDataHolder.EvDataViewHolder>{

    private Context mCtx;
    private ArrayList<EventData> eventList;

    public EventAdapterDataHolder(Context mCtx, ArrayList<EventData> eventList){
        this.mCtx = mCtx;
        this.eventList = eventList;
    }

    public EvDataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.card_event_data, parent, false);
        return new EvDataViewHolder(view);
    }

    public void onBindViewHolder(EvDataViewHolder holder, int position) {
        EventData event = eventList.get(position);

        holder.event_handler_name.setText(event.getGroupName());
        holder.event_name.setText(event.getEventName());
        holder.event_time.setText(String.valueOf(event.getStartTime()));
        holder.event_location.setText(String.valueOf(event.getAddress()));
        holder.no_of_members.setText(String.valueOf(event.getNoOfMembers()));
    }
    
    public int getItemCount() {
        return eventList.size();
    }


    class EvDataViewHolder extends RecyclerView.ViewHolder {

        TextView event_handler_name, event_name, event_time, event_location, no_of_members;

        public EvDataViewHolder(View itemView) {
            super(itemView);

            event_handler_name = itemView.findViewById(R.id.event_handlers_name);
            event_name = itemView.findViewById(R.id.event_name);
            event_time = itemView.findViewById(R.id.event_time);
            event_location = itemView.findViewById(R.id.event_location);
            no_of_members = itemView.findViewById(R.id.no_of_mems);
        }
    }

}

