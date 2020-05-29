package com.datalife.datalife.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.datalife.datalife.app.AppManager;
import com.datalife.datalife.mvp.IView;
import com.datalife.datalife.util.UToast;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by LG on 2018/1/17.
 */

public abstract class BaseMvpActivity<P extends BasePresenter> extends BaseActivity implements
        IView {
    private ProgressDialog mProgressDialog;
    protected View view;

    protected P mPresenter;

    protected abstract P loadPresenter();

    private Unbinder unbinder;
    private CompositeSubscription mCompositeSubscription;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(getView());
        mPresenter = loadPresenter();
//        unbinder = ButterKnife.bind(this);
        initCommonData();
    }

    /**
     * @return 显示的内容
     */
    /*public View getView() {
        view = View.inflate(this, getLayoutId(), null);
        return view;
    }*/

    /**
     * 所有rx订阅后，需要调用此方法，用于在detachView时取消订阅
     */
    protected void addSubscription(Subscription subscribe) {
        if (mCompositeSubscription == null)
            mCompositeSubscription = new CompositeSubscription();
        mCompositeSubscription.add(subscribe);
    }
    /**
     * 取消本页面所有订阅
     */
    protected void onUnsubscribe() {
        if (mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.unsubscribe();
        }
    }

    private void initCommonData() {

        //绑定Presenter
        if (mPresenter != null)
            mPresenter.attachView(this);
    }

//    protected abstract int getLayoutId();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mPresenter != null) mPresenter.detachView();
        onUnsubscribe();
    }

    protected ViewDataBinding setBindingContentView(@LayoutRes int layoutResID) {
        return DataBindingUtil.setContentView(this, layoutResID);
    }

    protected void toast(@StringRes int resId) {
        UToast.show(getActivity(), resId);
    }

    protected void toast(@StringRes int resId, int duration) {
        UToast.show(getActivity(), resId, duration);
    }

    protected void toast(@NonNull String text) {
        UToast.show(getActivity(), text);
    }

    protected void toast(@NonNull String text, int duration) {
        UToast.show(getActivity(), text, duration);
    }

    private Activity getActivity() {
        return this;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    protected Toolbar.OnMenuItemClickListener setUpMenuItemClick() {
        return null;
    }

    protected void showProgressDialog(int messageId) {
        final String message = getResources().getString(messageId);
        showProgressDialog(message);
    }

    protected void showProgressDialog(CharSequence message) {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.setMessage(message);
        } else {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setMessage(message);
            mProgressDialog.show();
        }
    }

    protected void dismissProgressDialog() {
        try {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
                mProgressDialog = null;
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

    }

    protected void showSnackBar(View view, int text) {
        showSnackBar(view, getString(text));
    }

    protected void showSnackBar(View view, String text) {
        Snackbar.make(view, text, Snackbar.LENGTH_SHORT).show();
    }

    protected void showSnackBar(View view, int text, int actionText, View.OnClickListener listener) {
        Snackbar.make(view, text, Snackbar.LENGTH_LONG)
                .setAction(actionText, listener).show();
    }
}

