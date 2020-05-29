package com.datalife.datalife.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import com.datalife.datalife.R;
import com.datalife.datalife.util.Eyes;
import com.datalife.datalife.util.StatusBarUtil;
import com.datalife.datalife.util.UToast;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by LG on 2018/1/18.
 */

public class BaseHealthActivity extends AppCompatActivity {
    private ProgressDialog mProgressDialog;
    private Unbinder unbinder;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
//        StatusBarUtil.setStatusBarColor(this, R.color.bg_toolbar_title);
        Eyes.setStatusBarColor(this, ContextCompat.getColor(this, R.color.bg_toolbar_title));
        super.setContentView(layoutResID);
        unbinder = ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
        unbinder.unbind();
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

    protected void setUpActionBar(Toolbar toolbar) {
        setUpActionBar(toolbar, null);
    }

    protected void setUpActionBar(Toolbar toolbar, String title) {
        setUpActionBar(toolbar, title, null, true);
    }

    protected void setUpActionBar(Toolbar toolbar, String title, String subTitle, boolean showHomeIcon) {
        setSupportActionBar(toolbar);
        final Toolbar.OnMenuItemClickListener menuItemClick = setUpMenuItemClick();
        if (menuItemClick != null) toolbar.setOnMenuItemClickListener(menuItemClick);
        setUpActionBar(title, subTitle, showHomeIcon);
    }

    protected void setUpActionBar(String title, String subTitle, boolean showHomeIcon) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(showHomeIcon);
            if (!TextUtils.isEmpty(title)) actionBar.setTitle(title);
            if (!TextUtils.isEmpty(subTitle)) actionBar.setSubtitle(subTitle);
        }
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

