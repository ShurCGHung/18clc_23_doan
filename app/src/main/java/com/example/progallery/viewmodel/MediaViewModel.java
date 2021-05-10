package com.example.progallery.viewmodel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.progallery.model.models.Media;
import com.example.progallery.model.services.MediaFetchService;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MediaViewModel extends ViewModel {
    private MutableLiveData<List<Media>> allMedias;

    public MediaViewModel() {
        this.allMedias = new MutableLiveData<>();
    }

    public void callService(Context context) {
        MediaFetchService service = MediaFetchService.getInstance();
        service.getMediaList(context)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getMediasObserverRx());
    }

    public void callServiceForAlbum(Context context, String albumName) {
        MediaFetchService service = MediaFetchService.getInstance();
        service.getMediaListForAlbum(context, albumName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getMediasObserverRx());
    }

    public void callServiceForFavoriteAlbum(Context context) {
        MediaFetchService service = MediaFetchService.getInstance();
        service.getMediaListForFavoriteAlbum(context)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getMediasObserverRx());
    }

    public MutableLiveData<List<Media>> getMediasObserver() {
        return allMedias;
    }

    private Observer<List<Media>> getMediasObserverRx() {
        return new Observer<List<Media>>() {

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                // Do something
            }

            @Override
            public void onNext(@NonNull List<Media> mediaList) {
                allMedias.postValue(mediaList);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                allMedias.postValue(null);
            }

            @Override
            public void onComplete() {
                // Do something
            }
        };
    }
}
