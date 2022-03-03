package com.netease.core.network.rx.databus;

// 负责数据总线

import android.util.Log;

import com.netease.core.R;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class RxBus {

    // 分发事件标记
    private final static String START_RUN = "doProcessInvoke start emitter run";

    private Set<Object> subscribers;

    /**
     * 注册
     */
    public synchronized void register(Object subscriber) {
        subscribers.add(subscriber);
    }

    /**
     * 移除
     */
    public synchronized void unRegister(Object subscriber) {
        subscribers.remove(subscriber);
    }

    private static volatile RxBus instance;

    private RxBus() {
        // 给容器初始化
        subscribers = new CopyOnWriteArraySet<>(); // 稳定的 安全的
    }

    public static RxBus getInstance() {
        if (instance == null) {
            synchronized (RxBus.class) {
                if (instance == null) {
                    instance = new RxBus();
                }
            }
        }
        return instance;
    }

    // TODO 对外暴露API
    public <T, R> void doProcessInvoke(Function function) { // function 提供给外界 网络耗时操作的 (异步)
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext(START_RUN);
                emitter.onComplete();
            }
        })
        .map(function)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer() {
            @Override
            public void accept(Object data) throws Exception {

                // data == GirlTaskImpl{return data}；

                if (data != null) {
                    sendDataActoin(data);
                }
            }
        });
    }

    // TODO 对外暴露API 2
    public <T, R> void doProcessInvoke(Observable<String> observable) {
        observable
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<String>() {
            @Override
            public void accept(String data) throws Exception {
                if (data != null) {
                    sendDataActoin(data);
                }
            }
        });
    }

        // 发生 并 负责扫描 被注册的目标
    public void sendDataActoin(Object data) {
        // 扫描注册进来的对象，所以需要遍历subscribers容器
        for (Object subscriber : subscribers) { // size=1
            checkSubscriberAnnotationMethod(subscriber, data);
        }
    }

    // 专门总线发射的
    private void checkSubscriberAnnotationMethod(Object subscriberTarget, Object data) {
        Method[] declaredMethods = subscriberTarget.getClass().getDeclaredMethods();
        for (Method method : declaredMethods) {
            method.setAccessible(true); // 让虚拟机不要检测private

            //1.找到含有注解的方法
            RegisterRxBus registerRxBus = method.getAnnotation(RegisterRxBus.class);
            if (registerRxBus != null) {
                // 找到目标了...

                Class<?>[] parameterTypes = method.getParameterTypes();
                String parameterType = parameterTypes[0].getName();

                Log.d("TAG","type  >>> "+data.getClass().getName());
                Log.d("TAG","parameterType  >>> "+parameterType);

                // 判断目标方法：判断接收的数据类型是否匹配
                // data.getClass().getName()：java.util.ArrayList
                // parameterType：java.util.ArrayList
                if (data.getClass().getName().equals(parameterType)) {
                    try {

                        // 执行 GirlPresenter 类中包含 RegisterRxBus 注解的方法
                        method.invoke(subscriberTarget, new Object[]{data});
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
