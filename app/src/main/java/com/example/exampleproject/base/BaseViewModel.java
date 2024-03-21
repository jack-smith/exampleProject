package com.example.exampleproject.base;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BaseViewModel extends ViewModel {

    public MutableLiveData<Boolean> getLoading() {
        return mLoading;
    }

    protected MutableLiveData<Boolean> mLoading = new MutableLiveData<>();
}
