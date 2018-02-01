package com.ehighsun.rxjavatest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn1,btn2,btn3,btn4,btn5;
    public static String TAG = "myRxJava";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);
        btn4 = (Button) findViewById(R.id.btn4);
        btn5 = (Button) findViewById(R.id.btn5);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn1:
                Observable.create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                        Log.d(TAG, "Observable emit 1" + "\n");
                        e.onNext(1);
                        Log.d(TAG, "Observable emit 2" + "\n");
                        e.onNext(2);
                        Log.d(TAG, "Observable emit 3" + "\n");
                        e.onNext(3);
                        e.onComplete();
                        Log.d(TAG, "Observable emit 4" + "\n" );
                        e.onNext(4);
                    }
                }).subscribe(new Observer<Integer>() {
                    private int i;
                    private Disposable mDisposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(Integer value) {
//                        i++;
//                        if (i == 2) {
//                            // 在RxJava 2.x 中，新增的Disposable可以做到切断的操作，让Observer观察者不再接收上游事件
//                            mDisposable.dispose();
//                        }
                        Log.e(TAG, "onError : value : " + value + "\n" );
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError : value : " + e.getMessage() + "\n" );
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "onComplete" + "\n" );
                    }
                });

                break;
            case R.id.btn2:
                Observable.just(1,2,3,4).subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.e(TAG, "Integer" + integer+"\n" );
                    }
                });
                break;
            case R.id.btn3:
                Observable.fromArray(1,2,3).subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.e(TAG, "Integer" + integer+"\n" );
                    }
                });
                break;
            case R.id.btn4:
                Flowable.interval(0,1000, TimeUnit.MILLISECONDS).subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.d(TAG,"Long:"+aLong);
                    }
                });
                break;
            case R.id.btn5:
                Flowable.create(new FlowableOnSubscribe<Object>() {
                    @Override
                    public void subscribe(FlowableEmitter<Object> e) throws Exception {
                        Log.e(TAG,"threadName:"+Thread.currentThread().getName()+";threadId:"+Thread.currentThread().getId());
                        e.onNext("");
                    }
                }, BackpressureStrategy.DROP)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        Log.e(TAG,"onSubscribe=>threadName:"+Thread.currentThread().getName()+";threadId:"+Thread.currentThread().getId());
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(Object o) {
                        Log.e(TAG,"onSubscribe=>threadName:"+Thread.currentThread().getName()+";threadId:"+Thread.currentThread().getId());
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

                break;
        }
    }
}
