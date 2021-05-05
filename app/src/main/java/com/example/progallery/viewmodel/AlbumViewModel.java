package com.example.progallery.viewmodel;

import android.content.Context;
import android.os.Environment;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.progallery.model.Album;
import com.example.progallery.services.AlbumFetchService;

import java.io.File;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AlbumViewModel extends ViewModel {
    private MutableLiveData<List<Album>> allAlbums;

    public AlbumViewModel() {
        this.allAlbums = new MutableLiveData<>();
    }

    public void callService(Context context) {
        AlbumFetchService service = AlbumFetchService.getInstance();
        service.getAlbumList(context)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getAlbumsObserverRx());
    }

    public MutableLiveData<List<Album>> getAlbumsObserver() {
        return allAlbums;
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

    public boolean createAlbum(String albumName) {
        File pictureFolder = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES
        );
        File imagesFolder = new File(pictureFolder, albumName);
        return imagesFolder.mkdirs();
    }
}
