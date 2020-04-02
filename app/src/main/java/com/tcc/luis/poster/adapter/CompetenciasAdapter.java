package com.tcc.luis.poster.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tcc.luis.poster.R;
import com.tcc.luis.poster.model.Competencia;
import com.tcc.luis.poster.model.FormacaoAcademica;

import java.util.List;

public class CompetenciasAdapter extends RecyclerView.Adapter<CompetenciasAdapter.CompetenciasViewHolder> {

    private List<Competencia> competencias;
    private Activity activity;
    private boolean showAll;

    public CompetenciasAdapter(List<Competencia> competencias, Activity activity, boolean showAll){
        this.competencias = competencias;
        this.activity = activity;
        this.showAll = showAll;
    }

    @NonNull
    @Override
    public CompetenciasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_competencias_curriculo, parent, false);
        CompetenciasAdapter.CompetenciasViewHolder opv = new CompetenciasAdapter.CompetenciasViewHolder(v);
        return opv;
    }

    @Override
    public void onBindViewHolder(@NonNull CompetenciasViewHolder holder, int position) {
        holder.mCompetenciaTitle.setText(competencias.get(position).getCompetencia());
    }

    @Override
    public int getItemCount() {
        int count;
        if(competencias.size() <= 3 || (competencias.size() > 3 && showAll)){
            count = competencias.size();
        } else {
            count = 3;
        }
        return count;
    }

    public class CompetenciasViewHolder extends RecyclerView.ViewHolder{
        private TextView mCompetenciaTitle;


        public CompetenciasViewHolder(@NonNull View itemView) {
            super(itemView);

            mCompetenciaTitle = itemView.findViewById(R.id.competencia_card_title);
        }
    }
}
