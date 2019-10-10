package com.example.loginactivity.Classes;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.loginactivity.EventRelated.EventInfo;
import com.example.loginactivity.R;

import java.util.ArrayList;

public class EventAdapterDataHolderBrief extends RecyclerView.Adapter<EventAdapterDataHolderBrief.EvDataViewHolder>{

    private Context mCtx;
    private ArrayList<EventData> eventList;
    private RecyclerView recyclerView;

    public EventAdapterDataHolderBrief(Context mCtx, ArrayList<EventData> eventList, RecyclerView recyclerView){
        this.mCtx = mCtx;
        this.eventList = eventList;
        this.recyclerView = recyclerView;
    }

    public EvDataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.card_event_data_brief, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int itemPosition = recyclerView.getChildLayoutPosition(view);
                EventData item = eventList.get(itemPosition);
//                mCtx.startActivity(new Intent(mCtx, EventInfo.class).putExtra("evId", item.getEvId()));
            }
        });
        return new EvDataViewHolder(view);
    }

    public void onBindViewHolder(EvDataViewHolder holder, int position) {
        EventData event = eventList.get(position);

        holder.event_nameb.setText(event.getEventName());
        holder.event_timeb.setText(String.valueOf(event.getStartTime()));
        holder.event_locationb.setText(String.valueOf(event.getAddress()));
    }
    
    public int getItemCount() {
        return eventList.size();
    }

    class EvDataViewHolder extends RecyclerView.ViewHolder {

        TextView event_nameb, event_timeb, event_locationb;

        public EvDataViewHolder(View itemView) {
            super(itemView);

            event_nameb = itemView.findViewById(R.id.event_name_brief);
            event_timeb = itemView.findViewById(R.id.event_time_brief);
            event_locationb = itemView.findViewById(R.id.event_location_brief);
        }

    }

}

