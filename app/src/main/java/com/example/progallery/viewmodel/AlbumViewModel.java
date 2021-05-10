package com.example.progallery.viewmodel;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.progallery.model.models.Album;
import com.example.progallery.model.services.AlbumFetchService;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AlbumViewModel extends ViewModel {
    private MutableLiveData<List<Album>> allAlbums;
    private MutableLiveData<Album> favoriteAlbum;

    public AlbumViewModel() {
        this.allAlbums = new MutableLiveData<>();
        this.favoriteAlbum = new MutableLiveData<>();
    }

    public void callService(Context context) {
        AlbumFetchService service = AlbumFetchService.getInstance();
        service.getAlbumList(context)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getAlbumsObserverRx());
    }

    public void callFavoriteService(Context context) {
        AlbumFetchService service = AlbumFetchService.getInstance();
        service.getFavoriteAlbum(context)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getFavoriteObserverRx());
    }


    public MutableLiveData<List<Album>> getAlbumsObserver() {
        return allAlbums;
    }

    public MutableLiveData<Album> getFavoriteObserver() {
        return favoriteAlbum;
    }

    private Observer<List<Album>> getAlbumsObserverRx() {
        return new Observer<List<Album>>() {

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                // Do something
            }

            @Override
            public void onNext(@NonNull List<Album> albumList) {
                allAlbums.postValue(albumList);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                allAlbums.postValue(null);
            }

            @Override
            public void onComplete() {
                // Do something
            }
        };
    }

    private Observer<Album> getFavoriteObserverRx() {
        return new Observer<Album>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull Album album) {
                favoriteAlbum.postValue(album);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                favoriteAlbum.postValue(null);
            }

            @Override
            public void onComplete() {

            }
        };
    }


}
