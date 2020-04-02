package com.tcc.luis.poster.adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tcc.luis.poster.R;
import com.tcc.luis.poster.helpers.Constantes;
import com.tcc.luis.poster.interessado.ui.oportunidades.DetalhesOportunidadeActivity;
import com.tcc.luis.poster.model.OportunidadeDeEmprego;

import java.util.List;

public class OportunidadesAdapter extends RecyclerView.Adapter<OportunidadesAdapter.OportunidadeViewHolder>{

    private final List<OportunidadeDeEmprego> jobs;
    private final Activity activity;
    private boolean isOwner;

    public OportunidadesAdapter(List<OportunidadeDeEmprego> jobs, Activity activity, boolean isOwner){
        this.jobs = jobs;
        this.activity = activity;
        this.isOwner = isOwner;
    }

    @NonNull
    @Override
    public OportunidadeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_oportunidades_interessado, parent, false);
        OportunidadeViewHolder opv = new OportunidadeViewHolder(v);
        return opv;
    }

    @Override
    public void onBindViewHolder(@NonNull OportunidadeViewHolder holder, int position) {
        OportunidadeDeEmprego oportunidadeDeEmprego = jobs.get(position);
        if((!oportunidadeDeEmprego.getEmpresa().getImagemLogo().isEmpty())){
            Picasso.with(activity).load(Uri.parse(oportunidadeDeEmprego.getEmpresa().getImagemLogo())).into(holder.mImgOportunidade);
        }
        holder.mTxtTitle.setText(oportunidadeDeEmprego.getCargo());
        holder.mTxtCategoria.setText(jobs.get(position).getCategoria());
        holder.mTxtDesc.setText(oportunidadeDeEmprego.getDescricao());

            View.OnClickListener onCLick = setOnClick(position);
            holder.mCardJobs.setOnClickListener(onCLick);

    }

    private View.OnClickListener setOnClick(final int position) {
        if (this.isOwner){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };
        } else {
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, DetalhesOportunidadeActivity.class);
                    intent.putExtra(Constantes.OPORTUNIDADE_KEY,jobs.get(position).getKey());
                    activity.startActivity(intent);
                }
            };
        }
    }

    @Override
    public int getItemCount() {
        return jobs.size();
    }

    public class OportunidadeViewHolder extends RecyclerView.ViewHolder{
        private TextView mTxtTitle;
        private TextView mTxtDesc;
        private TextView mTxtCategoria;
        private CardView mCardJobs;
        private ImageView mImgOportunidade;

        public OportunidadeViewHolder(View itemView) {
            super(itemView);
            mTxtTitle = itemView.findViewById(R.id.card_oportunidade_title_vaga);
            mTxtDesc = itemView.findViewById(R.id.card_oportunidade_descricao);
            mTxtCategoria = itemView.findViewById(R.id.card_oportunidade_categoria);
            mImgOportunidade = itemView.findViewById(R.id.card_oportunidade_img_empresa);
            mCardJobs = itemView.findViewById(R.id.card_oportunidade_interessado);
        }
    }

}
