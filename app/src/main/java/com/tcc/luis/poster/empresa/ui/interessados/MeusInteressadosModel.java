package com.tcc.luis.poster.empresa.ui.interessados;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MeusInteressadosModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MeusInteressadosModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}