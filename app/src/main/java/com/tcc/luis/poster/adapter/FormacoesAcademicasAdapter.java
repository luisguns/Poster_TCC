package com.tcc.luis.poster.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.tcc.luis.poster.R;
import com.tcc.luis.poster.model.FormacaoAcademica;

import java.util.List;

public class FormacoesAcademicasAdapter extends RecyclerView.Adapter<FormacoesAcademicasAdapter.FormacoesAcademicaViewHolder> {


    private final List<FormacaoAcademica> formacoes;
    private final Activity activity;
    private boolean showAll;


    public  FormacoesAcademicasAdapter(List<FormacaoAcademica> formacoes, Activity activity, boolean showAll){
        this.formacoes = formacoes;
        this.activity = activity;
        this.showAll = showAll;

    }

    @NonNull
    @Override
    public FormacoesAcademicaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_formacao_academica, parent, false);
        FormacoesAcademicasAdapter.FormacoesAcademicaViewHolder opv = new FormacoesAcademicasAdapter.FormacoesAcademicaViewHolder(v);
        return opv;
    }

    @Override
    public void onBindViewHolder(@NonNull FormacoesAcademicaViewHolder holder, int position) {
        holder.mTxtTitleCurso.setText(formacoes.get(position).getTituloFormacao() + " em " +  formacoes.get(position).getNomeCurso());
        holder.mTxtTitleInstituicao.setText(formacoes.get(position).getNomeInstituicao());
        holder.mTxtConclusao.setText("Data de conclusão: " + formacoes.get(position).getDataConclusao());
    }

    @Override
    public int getItemCount() {
        int count;
        if(formacoes.size() <= 3 || (formacoes.size() > 3 && showAll)){
            count = formacoes.size();
        } else {
            count = 3;
        }
        return count;
    }

    public class FormacoesAcademicaViewHolder extends RecyclerView.ViewHolder {

        private TextView mTxtTitleInstituicao;
        private TextView mTxtTitleCurso;
        private TextView mTxtConclusao;

        public FormacoesAcademicaViewHolder(@NonNull View itemView) {
            super(itemView);
            mTxtTitleInstituicao = itemView.findViewById(R.id.formacao_academica_card_nome_instituicao);
            mTxtTitleCurso = itemView.findViewById(R.id.formacao_academica_card_nome_curso);
            mTxtConclusao = itemView.findViewById(R.id.formacao_academica_card_conclusao);

        }
    }

}
