package com.netease.neteaseproject.dagger;

import com.netease.neteaseproject.MainActivity;
import com.netease.neteaseproject.presenter.GirlPresenter;

import dagger.Module;
import dagger.Provides;

// 对GirlPresenter 进行包裹
@Module
public class GirlPresenterModule {

    private MainActivity mView;

    public GirlPresenterModule(MainActivity mView) {
        this.mView = mView;
    }

    @Provides
    public GirlPresenter providerGirlPresenter() {
        return new GirlPresenter(mView);
    }
}
