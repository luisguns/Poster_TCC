package com.tcc.luis.poster.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.tcc.luis.poster.R;
import com.tcc.luis.poster.empresa.ui.interessados.DetalhesInteressadoActivity;
import com.tcc.luis.poster.helpers.Constantes;
import com.tcc.luis.poster.interessado.ui.meu_perfil.MeuPerfilFragment;
import com.tcc.luis.poster.model.Interessado;

import java.util.List;

public class InteressadosAdapter extends RecyclerView.Adapter<InteressadosAdapter.InteressadosViewHolder> {

    private final Activity activity;
    private final List<Interessado> interessados;

    public InteressadosAdapter(List<Interessado> interessados, Activity activity){
        this.interessados = interessados;
        this.activity = activity;
    }

    @NonNull
    @Override
    public InteressadosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_interessados, parent, false);
        InteressadosViewHolder interessadosViewHolder = new InteressadosViewHolder(v);
        return interessadosViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull InteressadosViewHolder holder, final int position) {
        holder.mNomeInteressado.setText(interessados.get(position).getUsuario().getName());
        holder.mOcupacaoInteressado.setText(interessados.get(position).getUsuario().getOcupacao());
        if(interessados.get(position).getUsuario().getProfileImage() != null){
            if(!interessados.get(position).getUsuario().getProfileImage().isEmpty()){
                Picasso.with(activity).load(interessados.get(position).getUsuario().getProfileImage()).networkPolicy(NetworkPolicy.OFFLINE).resize(512,512).centerCrop().into(holder.mImgInteressado);
            }
        }

        holder.mCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent irDetailInteressado = new Intent(activity, DetalhesInteressadoActivity.class);
                irDetailInteressado.putExtra(Constantes.INTERESSADO_KEY,interessados.get(position).getIdInteressado());
                activity.startActivity(irDetailInteressado);
                activity.finish();
            }
        });

    }

    @Override
    public int getItemCount() {
        return interessados.size();
    }

    class InteressadosViewHolder extends RecyclerView.ViewHolder{

        private TextView mNomeInteressado;
        private TextView mOcupacaoInteressado;
        private ImageView mImgInteressado;
        private CardView mCard;

        public InteressadosViewHolder(@NonNull View itemView) {
            super(itemView);

            mNomeInteressado = itemView.findViewById(R.id.card_interessados_nome);
            mOcupacaoInteressado = itemView.findViewById(R.id.card_interessados_ocupacao);
            mImgInteressado = itemView.findViewById(R.id.card_interessados_img_perfil);
            mCard = itemView.findViewById(R.id.card_interessados);
        }
    }
}
