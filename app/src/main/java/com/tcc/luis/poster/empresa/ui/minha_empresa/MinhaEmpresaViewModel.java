package com.tcc.luis.poster.empresa.ui.minha_empresa;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MinhaEmpresaViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MinhaEmpresaViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}