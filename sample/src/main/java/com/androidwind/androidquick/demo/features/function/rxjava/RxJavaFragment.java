package com.androidwind.androidquick.demo.features.function.rxjava;

import android.arch.lifecycle.Lifecycle;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidwind.androidquick.demo.R;
import com.androidwind.androidquick.demo.base.BaseFragment;
import com.androidwind.androidquick.demo.features.function.ui.webview.WebViewActivity;
import com.androidwind.androidquick.demo.tool.AssetsUtil;
import com.androidwind.androidquick.demo.tool.GlideUtils;
import com.androidwind.androidquick.demo.tool.RxUtil;
import com.androidwind.androidquick.module.retrofit.exeception.ApiException;
import com.androidwind.androidquick.module.rxjava.BaseObserver;
import com.androidwind.androidquick.util.StringUtil;
import com.androidwind.androidquick.util.ToastUtil;
import com.androidwind.annotation.annotation.BindTag;
import com.androidwind.annotation.annotation.TagInfo;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle;
import com.trello.rxlifecycle2.LifecycleProvider;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Callable;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;


/**
 * @author ddnosh
 * @website http://blog.csdn.net/ddnosh
 */
@BindTag(type = TagInfo.Type.FRAGMENT, tags = {"rxjava"}, description = "RxJava多种实例")
public class RxJavaFragment extends BaseFragment {

    @BindView(R.id.tv_flowable_result)
    TextView mFlowableResult;
    @BindView(R.id.iv_example)
    ImageView mExample;

    private LifecycleProvider<Lifecycle.Event> lifecycleProvider;

    @Override
    protected void initViewsAndEvents(Bundle savedInstanceState) {
        lifecycleProvider = AndroidLifecycle.createLifecycleProvider(this);
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_rxjava;
    }

    @OnClick({R.id.btn_rxjava_create, R.id.btn_rxjava_just, R.id.btn_rxjava_from, R.id.btn_rxjava_map,
            R.id.btn_rxjava_flatmap, R.id.btn_rxjava_thread, R.id.tv_rxjava_more, R.id.btn_rxjava_by_manual,
            R.id.btn_rxjava_flowable, R.id.btn_rxjava_concat, R.id.btn_rxjava_lifecycle1, R.id.btn_rxjava_lifecycle2,
            R.id.btn_rxjava_lifecycle3, R.id.btn_rxjava_example})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_rxjava_create:
                testCreate();
                break;
            case R.id.btn_rxjava_just:
                testJust();
                break;
            case R.id.btn_rxjava_from:
                testFrom();
                break;
            case R.id.btn_rxjava_map:
                testMap();
                break;
            case R.id.btn_rxjava_flatmap:
                testFlatMap();
                break;
            case R.id.btn_rxjava_thread:
                testThread();
                break;
            case R.id.btn_rxjava_by_manual:
                testManual();
                break;
            case R.id.btn_rxjava_flowable:
                testFlowable();
                break;
            case R.id.tv_rxjava_more:
                Bundle bundle = new Bundle();
                bundle.putString("url", "https://github.com/amitshekhariitbhu/RxJava2-Android-Samples");
                readyGo(WebViewActivity.class, bundle);
                break;
            case R.id.btn_rxjava_concat:
                testConcat();
                break;
            case R.id.btn_rxjava_lifecycle1:
                testLifeCycle1();
                break;
            case R.id.btn_rxjava_lifecycle2:
                testLifeCycle2();
                break;
            case R.id.btn_rxjava_lifecycle3:
                testLifeCycle3();
                break;
            case R.id.btn_rxjava_example:
                testExample();
                break;
        }
    }

    //--------Create
    private void testCreate() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) {
                e.onNext("1");
                e.onNext("");
                e.onNext("2");
                e.onComplete();
//                e.onError(new NullPointerException());
            }
        }).subscribe(new Observer<String>() {
            private Disposable disposable;//主动阻断本次和后面的所有订阅

            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
            }

            @Override
            public void onNext(String s) {
                System.out.println("RxJava:" + s);
                if (StringUtil.isEmpty(s)) {//s为空不符合条件, 阻断本次和后面的所有订阅
                    disposable.dispose();
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                ToastUtil.showToast("create done!");
            }
        });
    }

    //--------Create
    //--------Just
    private void testJust() {
        Observable.just("A", "B", "C", "D").subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String s) {
                System.out.println("RxJava:" + s);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                ToastUtil.showToast("just done!");
            }

        });
    }

    //--------Just
    //--------From
    Integer[] items = {0, 1, 2, 3, 4, 5};

    private void testFrom() {
        Observable.fromArray(items).subscribe(new Observer<Integer>() {

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                ToastUtil.showToast("from done!");
            }

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer integer) {
                System.out.println("RxJava:" + integer);
            }
        });
    }

    //--------From
    //--------Map
    private void testMap() {
//        String pathString = Environment.getExternalStorageDirectory() + File.separator + "Pictures" + File.separator + "test.png";
//        Observable.just(pathString)
//                .map(new Func1<String, File>() {
//                    @Override
//                    public File call(String pathString) {
//                        File filePath = new File(pathString);
//                        return filePath;
//                    }
//                })
//                .map(new Func1<File, Bitmap>() {
//                    @Override
//                    public Bitmap call(File file) {
//                        return BitmapFactory.decodeFile(file.getAbsolutePath());
//                    }
//                })
//                .observeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<Bitmap>() {
//                    @Override
//                    public void call(Bitmap bitmap) {
//                        ((ImageView)getActivity().findViewById(R.id.iv_testmap)).setImageBitmap(bitmap);
//                        Log.d("RxJava", "transfer done!");
//                    }
//                }, new Action1<Throwable>() {
//                    @Override
//                    public void call(Throwable throwable) {
//                        throwable.printStackTrace();
//                    }
//                });

        final String pathString = "ic_launcher.png";
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext(pathString);
            }
        }).map(new Function<String, Bitmap>() {
            @Override
            public Bitmap apply(String assetsString) throws Exception {
                return AssetsUtil.getImageFromAssetsFile(getContext(), assetsString);
            }
        })
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Bitmap>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Bitmap bitmap) {
                        ((ImageView) getActivity().findViewById(R.id.iv_testmap)).setImageBitmap(bitmap);
                        Log.d("RxJava", "image has loaded!");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });

//        final String resName = "ic_launcher.png";
//        Observable.just(resName)
//                .map(new Function<String, Bitmap>() {
//                    @Override
//                    public Bitmap apply(String s) throws Exception {
//                        return AssetsUtil.getImageFromAssetsFile(getContext(), resName);
//                    }
//                })
//                .observeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<Bitmap>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(Bitmap bitmap) {
//                        ((ImageView) getActivity().findViewById(R.id.iv_testmap)).setImageBitmap(bitmap);
//                        Log.d("RxJava", "image has loaded!");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        e.printStackTrace();
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
    }

    //--------Map
    //--------FlatMap
    private void testFlatMap() {
        final String originalPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        Observable.just(originalPath)
                .flatMap(new Function<String, ObservableSource<Bitmap>>() {
                    @Override
                    public ObservableSource<Bitmap> apply(String s) throws Exception {
                        String newPath = originalPath + File.separator + "Pictures" + File.separator + "test.png";
                        Bitmap bitmap = BitmapFactory.decodeFile(newPath);
                        return Observable.just(bitmap);
                    }
                })
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Bitmap>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Bitmap bitmap) {
                        ((ImageView) getActivity().findViewById(R.id.iv_testflat)).setImageBitmap(bitmap);
                        Log.d("RxJava", "image has loaded!");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    //--------FlatMap
    //--------Thread
    private void testThread() {
        //write way 1
//        Observable.just("task result").
        //write way 2
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                if (!emitter.isDisposed()) {
                    Log.d("RxJava", "create " + Thread.currentThread().getName());
                    emitter.onNext("task result");
                }
            }
        })
                .observeOn(AndroidSchedulers.mainThread())//let the next call run in main thread
                .flatMap(new Function<String, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(String s) throws Exception {
                        Log.d("RxJava", "flatMap1 " + Thread.currentThread().getName());
                        return Observable.just(s);
                    }
                })
                .observeOn(Schedulers.io())//let the next call run in io thread
                .flatMap(new Function<String, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(String s) throws Exception {
                        Log.d("RxJava", "flatMap2 " + Thread.currentThread().getName());
                        return Observable.just(s);
                    }
                })
                .subscribeOn(Schedulers.io())//let all the observables which haven't assign to special thread in the front run in io thread
                .observeOn(AndroidSchedulers.mainThread())//let the next call run in main thread
                .subscribe(new BaseObserver<String>() {
                    @Override
                    public void onError(ApiException exception) {
                        Log.e("tag", "error" + exception.getMessage());
                    }

                    @Override
                    public void onSuccess(String s) {
                        Log.e("tag", "onSuccess");
                    }
                });
    }

    //--------Thread
    //--------Manual
    private Subject<String> lifecycle = PublishSubject.create();

    private void testManual() {
        lifecycle.fromCallable(new Callable<String>() {

            @Override
            public String call() throws Exception {
                return "here is triggered by manual";
            }
        }).compose(RxUtil.applySchedulers())
                .compose(lifecycleProvider.bindUntilEvent(Lifecycle.Event.ON_DESTROY))
                .subscribe(new Observer<String>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        System.out.println(s);
                        ToastUtil.showToast(s);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
    //--------Compose
    //--------Flowable

    /**
     * 注意：只有在有背压需求的时候才需要使用Flowable, 否则使用Observable, 不然会影响性能
     */
    private void testFlowable() {//flowable比observable多了一个缓冲池大小的限制
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> e) throws Exception {
                System.out.println("send----> 1");
                e.onNext(1);
                System.out.println("send----> 2");
                e.onNext(2);
                System.out.println("send----> 3");
                e.onNext(3);
                System.out.println("send----> Done");
                e.onComplete();
            }
        }, BackpressureStrategy.BUFFER) //add param: BackpressureStrategy
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        System.out.println("receive----> " + integer);
                        mFlowableResult.setText("receive----> " + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("receive----> Done");
                        mFlowableResult.setText("receive----> Done");
                    }
                });
    }

    //--------Flowable
    //--------Concat
    private Observable<String> getObservable() {
        return Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                try {
                    Thread.sleep(1000); // 假设此处是耗时操作
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return "3";
            }
        });
    }

    /**
     * 将多个数据源连接起来一起处理
     */
    private void testConcat() {
        Observable<String> o1 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) {
                e.onNext("1");
                e.onComplete();//必须加这个, 否则o2出现不了
            }
        });
        Observable<String> o2 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) {
                e.onNext("2");
                e.onComplete();
            }
        });
        Observable.concat(o1, o2, getObservable())
                .compose(RxUtil.applySchedulers())
                .compose(lifecycleProvider.bindUntilEvent(Lifecycle.Event.ON_DESTROY))
                .subscribeWith(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        if ("1".equals(s)) {
                            ToastUtil.showToast("concat: 1");
                        } else if ("2".equals(s)) {
                            ToastUtil.showToast("concat: 2");
                        } else if ("3".equals(s)) {
                            ToastUtil.showToast("concat: 3");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        ToastUtil.showToast("concat done!");
                    }
                });
    }

    //--------Concat
    //--------Lifecycle CompositeDisposable
    private final CompositeDisposable disposables = new CompositeDisposable();

    private void testLifeCycle1() {
        Observable<Boolean> observable = Observable.create(emitter -> {
            try {
                Thread.sleep(2000); // 假设此处是耗时操作
            } catch (Exception e) {
                e.printStackTrace();
                if (!emitter.isDisposed()) {
                    emitter.onError(new RuntimeException());//onError和onSuccess不能同时调用
                }
            }
            if (!emitter.isDisposed()) {
                emitter.onNext(true);
            }
        });
        DisposableObserver<Boolean> observer = new DisposableObserver<Boolean>() {
            @Override
            public void onNext(Boolean s) {
                ToastUtil.showToast("CompositeDisposable with DisposableObserver");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        observable
                .compose(RxUtil.applySchedulers())
                .subscribe(observer);
        disposables.add(observer);
    }

    private void testLifeCycle2() {
        Observable<Boolean> observable = Observable.create(emitter -> {
            try {
                Thread.sleep(2000); // 假设此处是耗时操作
            } catch (Exception e) {
                e.printStackTrace();
                if (!emitter.isDisposed()) {
                    emitter.onError(new RuntimeException());//onError和onSuccess不能同时调用
                }
            }
            if (!emitter.isDisposed()) {
                emitter.onNext(true);
            }
        });
        Consumer<Boolean> observer = new Consumer<Boolean>() {
            @Override
            public void accept(Boolean path) throws Exception {
                ToastUtil.showToast("CompositeDisposable with Consumer");
            }
        };
        disposables.add(observable.compose(RxUtil.applySchedulers()).subscribe(observer));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposables.clear();
    }

    //--------Lifecycle CompositeDisposable
    //--------Lifecycle RxLifecycle
    private void testLifeCycle3() {
        Observable.create((ObservableOnSubscribe<Boolean>) emitter -> {
                    try {
                        Thread.sleep(2000); // 假设此处是耗时操作
                    } catch (Exception e) {
                        e.printStackTrace();
                        emitter.onError(new RuntimeException());
                    }
                    emitter.onNext(true);
                }
        )
                .compose(RxUtil.applySchedulers())
                .compose(lifecycleProvider.bindUntilEvent(Lifecycle.Event.ON_DESTROY))
                .subscribe(new BaseObserver<Boolean>() {
                    @Override
                    public void onError(ApiException exception) {
                        ToastUtil.showToast("LifeCycle Error");
                    }

                    @Override
                    public void onSuccess(Boolean b) {
                        ToastUtil.showToast("LifeCycle");
                    }
                });
    }

    //--------Lifecycle RxLifecycle
    //--------Example
    private void testExample() {
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<File>() {
            @Override
            public void subscribe(ObservableEmitter<File> emitter) throws Exception {
                emitter.onNext(Glide.with(getContext())
                        .load("https://hbimg.huabanimg.com/85966739547072c95d2ecd2ff60248e1d09be657c005-Dd5NrU_fw658")
                        .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .get());
            }
        }).map(new Function<File, String>() {
            @Override
            public String apply(File file) throws Exception {
                return savePicture(getContext(), file, new SimpleDateFormat("yyyyMMddHHmmss",
                        Locale.getDefault()).format(new Date())
                        + ".jpg");
            }
        });
        Consumer<String> observer = new Consumer<String>() {
            @Override
            public void accept(String path) throws Exception {
                ToastUtil.showToast(StringUtil.isEmpty(path) ? "保存失败" : "保存成功");
                GlideUtils.loadImageView(path, mExample);
            }
        };
        disposables.add(observable.compose(RxUtil.applySchedulers()).subscribe(observer));
    }
    private String savePicture(Context context, File sourceFile, String fileName) {
        File save = new File(Environment.getExternalStorageDirectory() + File.separator + "AndroidQuick" +
                File.separator + "Picture");
        if (save != null && !save.exists()) {
            save.mkdirs();
        }
        File destFile = new File(save, fileName);
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            fileInputStream = new FileInputStream(sourceFile);
            fileOutputStream = new FileOutputStream(destFile);
            byte[] buffer = new byte[1024];
            while (fileInputStream.read(buffer) > 0) {
                fileOutputStream.write(buffer);
            }
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(destFile))); //通知相册更新
            return destFile.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        } finally {
            try {
                fileInputStream.close();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //--------Example
}
