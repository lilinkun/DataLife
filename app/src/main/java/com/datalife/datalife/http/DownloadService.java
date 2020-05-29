package com.datalife.datalife.http;

import com.datalife.datalife.bean.DownloadBean;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by LG on 2018/3/18.
 */

public interface DownloadService {

    @GET("update.txt")
    Observable<DownloadBean> download();

}
