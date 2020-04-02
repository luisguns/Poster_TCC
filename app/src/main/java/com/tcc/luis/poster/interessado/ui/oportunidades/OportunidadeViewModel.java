package com.tcc.luis.poster.interessado.ui.oportunidades;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class OportunidadeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public OportunidadeViewModel() {

    }

    public LiveData<String> getText() {
        return mText;
    }
}