package com.tcc.luis.poster.empresa.ui.minha_empresa;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.tcc.luis.poster.R;
import com.tcc.luis.poster.empresa.CadastrarOportunidadesActivity;
import com.tcc.luis.poster.empresa.ui.minha_empresa.adapter.TabsAdapter;
import com.tcc.luis.poster.empresa.ui.minha_empresa.ui.AboutCompanyTabFragment;
import com.tcc.luis.poster.empresa.ui.minha_empresa.ui.CompanyJobsTabFragment;
import com.tcc.luis.poster.empresa.ui.minha_empresa.ui.EditEmpresaActivity;
import com.tcc.luis.poster.helpers.Constantes;
import com.tcc.luis.poster.helpers.CountHelper;
import com.tcc.luis.poster.helpers.ImageHelper;
import com.tcc.luis.poster.helpers.ProgressHelper;
import com.tcc.luis.poster.model.Empresa;

import java.io.File;
import java.io.IOException;

import id.zelory.compressor.Compressor;
import id.zelory.compressor.FileUtil;

import static android.app.Activity.RESULT_OK;

public class MinhaEmpresaFragment extends Fragment {
    private static final int IMAGE_GALLERY_REQUEST = 1;
    private static final String TAG = "MinhaEMpresaFragment";
    private View v;
    private CompanyJobsTabFragment companyJobsTabFragment;
    private AboutCompanyTabFragment aboutCompanyTabFragment;

    private TextView mTxtSlogan;
    private TextView mTxtNomeEmpresa;

    private FloatingActionButton mFabEditCompany;
    private FloatingActionButton mFabRegisterJob;

    private String actualSelectedTab;
    private ProgressBar mProgressImgProfile;

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;

    private ImageView mImgEditImageProfile;
    private ImageView mImgImageProfile;


    private Uri mImagemUri;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_minha_empresa, container, false);

        mFabRegisterJob = v.findViewById(R.id.company_jobs_tab_btn_register_job);
        mFabEditCompany = v.findViewById(R.id.company_jobs_tab_btn_edit_company);
        mTxtNomeEmpresa = v.findViewById(R.id.my_company_fragment_name_company);
        mTxtSlogan = v.findViewById(R.id.my_company_fragment_slogan_company);
        mImgEditImageProfile = v.findViewById(R.id.my_company_img_edit_profileimg);
        mImgImageProfile = v.findViewById(R.id.my_company_fragment_image_company_profile);
        mProgressImgProfile = v.findViewById(R.id.my_company_progress_img);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();


        mFabEditCompany.setVisibility(View.GONE);

        mFabRegisterJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent irCadastrar = new Intent(getActivity(), CadastrarOportunidadesActivity.class);
                startActivity(irCadastrar);
            }
        });

        mFabEditCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), EditEmpresaActivity.class));
            }
        });

        mImgEditImageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImageClick();
            }
        });

        companyJobsTabFragment = new CompanyJobsTabFragment();
        aboutCompanyTabFragment = new AboutCompanyTabFragment();
        actualSelectedTab = "Vagas";

        findCompany();


        return v;
    }

    private void findCompany() {
        ProgressHelper.show(mProgressImgProfile,null,true);
        firebaseFirestore.collection(Constantes.TABELA_DATABASE_EMPRESA)
                .document(firebaseAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            Empresa empresa = task.getResult().toObject(Empresa.class);
                            loadUiWithCompany(empresa);
                        }
                    }
                });
    }

    private void loadUiWithCompany(Empresa empresa) {
        if(empresa.getImagemLogo() != null){
            if(!empresa.getImagemLogo().isEmpty()){
                mImgImageProfile.setBackgroundColor(getResources().getColor(R.color.transparent));
                ImageHelper.carregaImagem(mImgImageProfile,empresa.getImagemLogo(), getActivity(),null);
            }
        }
        mTxtNomeEmpresa.setText(empresa.getNome());
        ProgressHelper.show(mProgressImgProfile,null,false);

    }


    public void selectImageClick() {
        ProgressHelper.show(mProgressImgProfile,mImgImageProfile,true);

        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(
                    Intent.createChooser(
                            intent,
                            getString(R.string.select_picture_title)),
                    IMAGE_GALLERY_REQUEST);
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    IMAGE_GALLERY_REQUEST);
        }
    }


    @Override
    public void onStart() {
        super.onStart();

        Resources resources = getResources();
        TabsAdapter adapter = new TabsAdapter(getChildFragmentManager());
        adapter.add(companyJobsTabFragment, "Vagas");
        adapter.add(aboutCompanyTabFragment, "Sobre");

        ViewPager viewPager = (ViewPager) v.findViewById(R.id.my_company_fragment_view_page);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) v.findViewById(R.id.my_company_fragment_tabs);
        tabLayout.setupWithViewPager(viewPager);
        setHasOptionsMenu(true);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                actualSelectedTab = tab.getText().toString();
                showFab(actualSelectedTab);
                Log.e(TAG, "onTabSelected: ");
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_GALLERY_REQUEST && resultCode == RESULT_OK) {
            try {
                File fileImagem = FileUtil.from(getActivity(), data.getData());
                fileImagem = Compressor.getDefault(getActivity()).compressToFile(fileImagem);
                mImagemUri = Uri.fromFile(fileImagem);
                updateImage(mImagemUri);
            } catch (IOException e) {
                e.printStackTrace();

            }
        } else {
            ProgressHelper.show(mProgressImgProfile,mImgImageProfile,false);
        }
    }


    private void updateImage(Uri data1) {
        String paste = "image/company_logo/";
        StorageReference reference = FirebaseStorage.getInstance().getReference();
        StorageReference child = reference.child(paste + String.valueOf(System.currentTimeMillis()));


        StorageTask<UploadTask.TaskSnapshot> taskSnapshotStorageTask = child.putFile(data1)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl();
                        downloadUrl.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                updateProfile(uri.toString());


                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        ProgressHelper.show(mProgressImgProfile,mImgImageProfile,false);
                                        Toast.makeText(getActivity(), "Ocorreu um erro durante o upload de sua imagem, Tente mais tarde", Toast.LENGTH_SHORT).show();
                                    }
                                });


//                        updateProfile(s);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });

    }

    private void updateProfile(final String uri) {
        FirebaseFirestore instance = FirebaseFirestore.getInstance();
        instance.collection(Constantes.TABELA_DATABASE_EMPRESA).document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .update("imagemLogo", uri)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            ImageHelper.carregaImagem(mImgImageProfile, mImagemUri.toString(), getActivity(),null);
                            ProgressHelper.show(mProgressImgProfile,mImgImageProfile,false);


                        } else {
                            Toast.makeText(getActivity(), "Ocorreu um erro ao atualizar sua imagem, tente novamente", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void showFab(String actualSelectedTab) {
        switch (actualSelectedTab) {
            case "Vagas":
                mFabEditCompany.setVisibility(View.GONE);
                mFabRegisterJob.setVisibility(View.VISIBLE);
                break;
            case "Sobre":
                mFabEditCompany.setVisibility(View.VISIBLE);
                mFabRegisterJob.setVisibility(View.GONE);
                break;
        }
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

    ;
}