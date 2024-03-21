package com.example.exampleproject.base;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProvider;
public abstract class BaseMvvmActivity<V extends ViewDataBinding, VM extends BaseViewModel> extends AppCompatActivity {

    private ProgressBar mLoadingView;
    protected V mBinding;
    protected VM mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initViewModel();
        initData();
        initObservable();
        initListener();
    }

    protected VM getActivityScopeViewModel(Class<VM> cls) {
        mViewModel = new ViewModelProvider(this).get(cls);
        return mViewModel;
    }


    protected void initObservable() {
//        if(mViewModel==null)return;
        mViewModel.getLoading().observe(this, aBoolean -> {
            if (aBoolean) {
                showLoading();
            } else {
                dismissLoading();
            }
        });
    }

    ;


    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        if (res != null && res.getConfiguration().fontScale != 1.0f) {
            Configuration configuration = res.getConfiguration();
            configuration.fontScale = 1.0f;
            res.updateConfiguration(configuration, res.getDisplayMetrics());
        }
        return res;
    }

    protected void initListener() {
    }

    protected void initData() {

    }


    @SuppressLint("SourceLockedOrientationActivity")
    protected void initView() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mBinding = DataBindingUtil.setContentView(this, getLayoutId());
    }

    protected abstract void initViewModel();

    public abstract int getLayoutId();



    public void showLoading() {
        try {
            if (mLoadingView == null) {
                mLoadingView = new ProgressBar(this);
            }
            if(mLoadingView.getVisibility() != View.VISIBLE) {
                mLoadingView.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {

        }

    }

    public void dismissLoading() {
        try {
            if (mLoadingView != null && mLoadingView.getVisibility() == View.VISIBLE) {
                mLoadingView.setVisibility(View.GONE);
            }
        } catch (Exception e) {

        }

    }

}
