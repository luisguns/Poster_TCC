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
import com.tcc.luis.poster.model.ExperienciaProfissional;

import java.util.List;

public class ExperienciaProfissionalAdapter extends RecyclerView.Adapter<ExperienciaProfissionalAdapter.ExperienciasProfissionaisViewHolder> {


    private final List<ExperienciaProfissional> experiencias;
    private final Activity activity;
    private final boolean showAll;

    public ExperienciaProfissionalAdapter(List<ExperienciaProfissional> experiencias, Activity activity, boolean showAll){
        this.experiencias = experiencias;
        this.activity = activity;
        this.showAll = showAll;
    }


    @NonNull
    @Override
    public ExperienciasProfissionaisViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_experiencia_profissional, parent, false);
        ExperienciaProfissionalAdapter.ExperienciasProfissionaisViewHolder opv = new ExperienciaProfissionalAdapter.ExperienciasProfissionaisViewHolder(v);
        return opv;
    }

    @Override
    public void onBindViewHolder(@NonNull ExperienciasProfissionaisViewHolder holder, int position) {
        holder.mTxtCargo.setText(experiencias.get(position).getCargo());
        holder.mTxtEmpresa.setText(experiencias.get(position).getEmpresa());
        holder.mTxtDesc.setText(experiencias.get(position).getDescricao());
        holder.mTxtData.setText("De " + experiencias.get(position).getDataInicio() + " A " + experiencias.get(position).getDataFim());
    }

    @Override
    public int getItemCount() {
        int count;
        if(experiencias.size() <= 3 || (experiencias.size() > 3 && showAll)){
            count = experiencias.size();
        } else {
            count = 3;
        }
        return count;
    }

    public class ExperienciasProfissionaisViewHolder extends RecyclerView.ViewHolder{

        private TextView mTxtCargo;
        private TextView mTxtEmpresa;
        private TextView mTxtDesc;
        private TextView mTxtData;

        public ExperienciasProfissionaisViewHolder(@NonNull View itemView) {
            super(itemView);

            mTxtCargo = itemView.findViewById(R.id.card_experiencia_cargo);
            mTxtEmpresa = itemView.findViewById(R.id.card_experiencia_empresa);
            mTxtDesc = itemView.findViewById(R.id.card_experiencia_desc);
            mTxtData = itemView.findViewById(R.id.card_experiencia_data_inicio);
        }
    }
}
