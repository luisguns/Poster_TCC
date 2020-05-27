package com.tcc.luis.poster.adapter;

import android.app.Activity;
import android.content.Context;
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
import com.tcc.luis.poster.model.Usuario;

import java.util.List;
import java.util.zip.Inflater;

public class CurriculosAdapter extends RecyclerView.Adapter<CurriculosAdapter.CurriculosViewHolder>{

    private List<Usuario> usuarioList;
    private Activity activity;


    public CurriculosAdapter(List<Usuario> usuarioList, Activity activity) {
        this.usuarioList = usuarioList;
        this.activity = activity;
    }


    @NonNull
    @Override
    public CurriculosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_curriculos, parent, false);
        CurriculosViewHolder opv = new CurriculosAdapter.CurriculosViewHolder(v);
        return opv;
    }

    @Override
    public void onBindViewHolder(@NonNull CurriculosViewHolder h, final int position) {
        h.mTxtNome.setText(usuarioList.get(position).getName() + usuarioList.get(position).getLastName());
        h.mTxtOcupacao.setText(usuarioList.get(position).getOcupacao());
        h.mTxtDescricao.setText(usuarioList.get(position).getSobre());

        if(usuarioList.get(position).getProfileImage() != null){
            if(!usuarioList.get(position).getProfileImage().isEmpty()){
                Picasso.with(activity).load(usuarioList.get(position)
                        .getProfileImage())
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .resize(512,512)
                        .centerCrop()
                        .into(h.mImgProfile);
            }
        }

        h.mCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent irDetailInteressado = new Intent(activity, DetalhesInteressadoActivity.class);
                irDetailInteressado.putExtra(Constantes.INTERESSADO_KEY,usuarioList.get(position).getUidUser());
                activity.startActivity(irDetailInteressado);
            }
        });
    }

    @Override
    public int getItemCount() {
        return usuarioList.size();
    }

    public class CurriculosViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImgProfile;
        private TextView mTxtNome;
        private TextView mTxtOcupacao;
        private TextView mTxtDescricao;
        private CardView mCard;

        public CurriculosViewHolder(@NonNull View iv) {
            super(iv);

            mImgProfile = iv.findViewById(R.id.card_curriculos_img);
            mTxtNome = iv.findViewById(R.id.card_curriculos_nome);
            mTxtOcupacao = iv.findViewById(R.id.card_curriculos_ocupacao);
            mTxtDescricao = iv.findViewById(R.id.card_curriculos_descricao);
            mCard = iv.findViewById(R.id.card_curriculos);
        }
    }
}
