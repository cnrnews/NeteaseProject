package com.netease.neteaseproject.presenter;

import com.netease.core.network.rx.databus.RegisterRxBus;
import com.netease.neteaseproject.bean.Girl;
import com.netease.neteaseproject.task.GirlTaskImpl;
import com.netease.neteaseproject.task.IGirlTask;
import com.netease.neteaseproject.view.IGirlView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class GirlPresenter<T extends IGirlView> { // T == IGirlView 或者 IGirlView 子类

    // View层的定义
    private WeakReference<T> mView;

    // Task层的定义
    private IGirlTask iGirlTask;

    public GirlPresenter(T mView) {
        this.mView = new WeakReference<>(mView);

        iGirlTask = new GirlTaskImpl();
        iGirlTask.loadGirlData(); // 加载数据
    }

    /**
     * @param arrayList
     * @RegisterRxBus 作用：RxBus 事件总线在加载完数据回调之前,会找到回调的目标进行回调
     */
    @RegisterRxBus
    public void showGirlDataAction(ArrayList<Girl> arrayList) {
        // 还需要做很多的事情逻辑
        // ...

        // 把结果给View
        mView.get().showGirlData(arrayList);
    }
}
