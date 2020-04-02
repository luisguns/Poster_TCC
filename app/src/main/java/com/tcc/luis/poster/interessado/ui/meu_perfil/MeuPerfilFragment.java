package com.tcc.luis.poster.interessado.ui.meu_perfil;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.tcc.luis.poster.R;
import com.tcc.luis.poster.adapter.CompetenciasAdapter;
import com.tcc.luis.poster.adapter.ExperienciaProfissionalAdapter;
import com.tcc.luis.poster.adapter.FormacoesAcademicasAdapter;
import com.tcc.luis.poster.helpers.Constantes;
import com.tcc.luis.poster.helpers.CountHelper;
import com.tcc.luis.poster.helpers.ImageHelper;
import com.tcc.luis.poster.helpers.ProgressHelper;
import com.tcc.luis.poster.interessado.ui.meu_perfil.ui.EditaPerfilActivity;
import com.tcc.luis.poster.interessado.ui.meu_perfil.ui.competencias.CompetenciasActivity;
import com.tcc.luis.poster.interessado.ui.meu_perfil.ui.experiencia.ExperienciaProfissionalActivity;
import com.tcc.luis.poster.interessado.ui.meu_perfil.ui.formacao.FormacoesAcademicaActivity;
import com.tcc.luis.poster.model.Competencia;
import com.tcc.luis.poster.model.Curriculo;
import com.tcc.luis.poster.model.ExperienciaProfissional;
import com.tcc.luis.poster.model.FormacaoAcademica;
import com.tcc.luis.poster.model.Usuario;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MeuPerfilFragment extends Fragment {

    private ImageView mImgEditAcademic;
    private ImageView mImgEditCompetencia;
    private ImageView mImgEditExperiencia;
    private ImageView mImgEditPerfil;

    private ConstraintLayout mContainerViews;
    private ProgressBar mProgressGeral;

    private TextView mTxtName;
    private TextView mTxtSobre;
    private TextView mTxtOcupacao;
    private TextView mTxtEmail;
    private ImageView mImgPerfil;

    private String actualUser;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private Curriculo curriculo;
    private ProgressBar mProgressImgPerfil;

    private RecyclerView mRvCompetencias;
    private RecyclerView mRvFormacoes;
    private RecyclerView mRvExperiencias;
    private List<View> ownerViews;
    private Activity activity;
    private TextView mTxtVerMaisFormacao;
    private TextView mTxtVerMaisCompetencia;
    private TextView mTxtVerMaisExperiencia;

    public MeuPerfilFragment(){
        ownerViews = new ArrayList<>();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_meu_perfil, container, false);
        setHasOptionsMenu(true);
        initViews(v);
        aplyListens();
        Bundle bd = getArguments();
        if (bd != null) {
            String interessadoKey = bd.getString(Constantes.INTERESSADO_KEY);
            if(interessadoKey != null){
                actualUser = interessadoKey;
                dimissAllViews(ownerViews);
            } else {
                actualUser = firebaseAuth.getCurrentUser().getUid();
            }

        } else {
            actualUser = firebaseAuth.getCurrentUser().getUid();
        }
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        initViews();
        new Thread(new Runnable() {
            @Override
            public void run() {
                findUser();
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                findCurriculo();
            }
        }).start();
    }

    private void loadUserUi(Usuario usuario) {
        String name = "";
        if (usuario.getName() != null) {
            name = name + usuario.getName();
        }

        if (usuario.getLastName() != null) {
            name = name + " " + usuario.getLastName();
        }

        if (usuario.getSobre() != null) {
            mTxtSobre.setText(usuario.getSobre());
        }

        if (usuario.getOcupacao() != null && !usuario.getOcupacao().isEmpty()) {
            mTxtOcupacao.setText(usuario.getOcupacao());
        }
        mTxtName.setText(name);

        if (!usuario.getProfileImage().isEmpty()) {
            loadImage(usuario.getProfileImage(), mImgPerfil);
        }

        if (usuario.getEmail() != null) {
            mTxtEmail.setText("Email: " + usuario.getEmail());
        }
    }

    private void dimissAllViews(List<View> views){
        for (View view: views
             ) {
            view.setVisibility(View.GONE);
        }
    }


    private void loadImage(String url, final ImageView img) {
        Picasso.with(activity).load(url).networkPolicy(NetworkPolicy.OFFLINE).resize(80, 80)
                .resize(512, 512).centerCrop().into(img, new Callback() {
            @Override
            public void onSuccess() {
                Bitmap imageBitmap = ((BitmapDrawable) img.getDrawable()).getBitmap();
                RoundedBitmapDrawable imageDrawable = ImageHelper.makeRoundDrawer(imageBitmap, getResources());
                img.setImageDrawable(imageDrawable);
                ProgressHelper.show(mProgressImgPerfil, null, false);
            }

            @Override
            public void onError() {
                img.setVisibility(View.GONE);
            }
        });
        ;
    }


    private void findUser() {
        ProgressHelper.show(mProgressImgPerfil, null, true);
        firebaseFirestore.collection(Constantes.TABELA_DATABASE_USUARIO)
                .document(actualUser)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            Usuario usuario = task.getResult().toObject(Usuario.class);
                            loadUserUi(usuario);
                        }
                    }
                });
    }

    private void findCurriculo() {
        ProgressHelper.show(mProgressGeral, mContainerViews, true);
        firebaseFirestore.collection(Constantes.TABELA_DATABASE_CURRICULO).document(actualUser)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    curriculo = task.getResult().toObject(Curriculo.class);
                    if (curriculo != null) {
                        loadViewsWIthCurriculo();
                    }

                }
            }
        });
    }

    private void loadViewsWIthCurriculo() {
        List<FormacaoAcademica> formacoesAcademicas = curriculo.getFormacoesAcademicas();
        List<Competencia> competencias = curriculo.getCompetencias();
        List<ExperienciaProfissional> experiencias = curriculo.getExperienciasProfissionais();
        if (formacoesAcademicas != null) {
            loadRecyclerViewFormacoes(formacoesAcademicas);
        } else {
            mRvFormacoes.setVisibility(View.GONE);
        }
        if (competencias != null) {
            loadRecyclerViewCompetencias(competencias);
        } else {
            mRvCompetencias.setVisibility(View.GONE);
        }
        if (experiencias != null) {
            loadRecyclerViewExperiencia(experiencias);
        } else {
            mRvExperiencias.setVisibility(View.GONE);
        }

        ProgressHelper.show(mProgressGeral, mContainerViews, false);

    }

    private void loadRecyclerViewExperiencia(List<ExperienciaProfissional> experiencias) {
        if (experiencias.size() > 3) {
            mTxtVerMaisExperiencia.setVisibility(View.VISIBLE);

        }

        ExperienciaProfissionalAdapter adapter = new ExperienciaProfissionalAdapter(experiencias, getActivity(), false);
        mRvExperiencias.setAdapter(adapter);
        mRvExperiencias.setNestedScrollingEnabled(false);


    }

    private void loadRecyclerViewCompetencias(List<Competencia> competencias) {
        if (competencias.size() > 3) {
            mTxtVerMaisCompetencia.setVisibility(View.VISIBLE);

        }
        CompetenciasAdapter adapter = new CompetenciasAdapter(competencias, getActivity(), false);
        mRvCompetencias.setAdapter(adapter);
        mRvCompetencias.setNestedScrollingEnabled(false);
    }

    private void loadRecyclerViewFormacoes(List<FormacaoAcademica> formacoesAcademicas) {
        if (formacoesAcademicas.size() > 3) {
            mTxtVerMaisFormacao.setVisibility(View.VISIBLE);
        }
        FormacoesAcademicasAdapter adapter = new FormacoesAcademicasAdapter(formacoesAcademicas, getActivity(), false);
        mRvFormacoes.setAdapter(adapter);
        mRvFormacoes.setNestedScrollingEnabled(false);
    }


    private void aplyListens() {
        mImgEditAcademic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), FormacoesAcademicaActivity.class));

            }
        });

        mImgEditCompetencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), CompetenciasActivity.class));

            }
        });

        mImgEditExperiencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), ExperienciaProfissionalActivity.class));

            }
        });

        mImgEditPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), EditaPerfilActivity.class));

            }
        });
    }

    private void initViews(View v) {
        mImgEditAcademic = v.findViewById(R.id.meu_perfil_interessado_img_edit_formacao);
        mImgEditCompetencia = v.findViewById(R.id.meu_perfil_interessado_img_edit_competencias);
        mImgEditExperiencia = v.findViewById(R.id.meu_perfil_interessado_img_edit_experiencia);
        mImgEditPerfil = v.findViewById(R.id.meu_perfil_interessado_img_edit_perfil);

        mTxtVerMaisFormacao = v.findViewById(R.id.meu_perfil_formacao_ver_mais_label);
        mTxtVerMaisCompetencia = v.findViewById(R.id.meu_perfil_competencia_ver_mais);
        mTxtVerMaisExperiencia = v.findViewById(R.id.meu_perfil_experiencia_ver_mais);

        ownerViews.addAll(Arrays.asList(mImgEditAcademic,mImgEditCompetencia, mImgEditExperiencia,mImgEditPerfil));

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        activity = getActivity();
        mProgressImgPerfil = v.findViewById(R.id.meu_perfil_progress_image);

        mRvFormacoes = v.findViewById(R.id.meu_perfil_rv_formacoes_academicas);
        mRvCompetencias = v.findViewById(R.id.meu_perfil_rv_competencias);
        mRvExperiencias = v.findViewById(R.id.meu_perfil_container_rv_experiencias);

        mTxtName = v.findViewById(R.id.meu_perfil_nome);
        mTxtOcupacao = v.findViewById(R.id.meu_perfil_usuario_atual_ocupação);
        mTxtEmail = v.findViewById(R.id.meu_perfil_contato_email);
        mTxtSobre = v.findViewById(R.id.meu_perfil_sobre);
        mImgPerfil = v.findViewById(R.id.meu_perfil_imagem_perfil);

        mContainerViews = v.findViewById(R.id.meu_perfil_layout_views);
        mProgressGeral = v.findViewById(R.id.meu_perfil_progress_geral);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        LinearLayoutManager llm2 = new LinearLayoutManager(getContext());
        LinearLayoutManager llm3 = new LinearLayoutManager(getContext());
        mRvFormacoes.setLayoutManager(llm);
        mRvCompetencias.setLayoutManager(llm2);
        mRvExperiencias.setLayoutManager(llm3);


    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.option_menu_company, menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_exit_company:
                CountHelper.logout(getActivity());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}