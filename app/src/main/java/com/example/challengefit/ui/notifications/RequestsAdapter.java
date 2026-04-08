package com.example.challengefit.ui.notifications;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.challengefit.R;
import com.example.challengefit.modelos.Solicitud;
import com.google.android.material.button.MaterialButton;
import java.util.List;

public class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.RequestViewHolder> {

    private List<Solicitud> requestList;
    private OnRequestActionListener listener;

    public interface OnRequestActionListener {
        void onAccept(Solicitud solicitud);
        void onReject(Solicitud solicitud);
    }

    public RequestsAdapter(List<Solicitud> requestList, OnRequestActionListener listener) {
        this.requestList = requestList;
        this.listener = listener;
    }

    public void setRequests(List<Solicitud> requests) {
        this.requestList = requests;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_solicitud, parent, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        Solicitud solicitud = requestList.get(position);
        if (solicitud.getAlumno() != null) {
            holder.tvName.setText(solicitud.getAlumno().getNombre());
        }
        
        holder.btnAccept.setOnClickListener(v -> listener.onAccept(solicitud));
        holder.btnReject.setOnClickListener(v -> listener.onReject(solicitud));
    }

    @Override
    public int getItemCount() {
        return requestList != null ? requestList.size() : 0;
    }

    public static class RequestViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        MaterialButton btnAccept, btnReject;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvStudentName);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnReject = itemView.findViewById(R.id.btnReject);
        }
    }
}
