package com.qw.freemusic.foundation;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * created by QY
 * description:
 */
public abstract class BaseFragment extends Fragment {

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        LOGD("onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LOGD("onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LOGD("onCreateView");
        View lView = inflater.inflate(getViewId(), null);
        initView(lView);
        return lView;
    }

    @Override
    public void onStart() {
        super.onStart();
        LOGD("onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        LOGD("onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        LOGD("onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        LOGD("onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LOGD("onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LOGD("onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LOGD("onDetach");
    }

    protected abstract String TAG();
    protected abstract void initView(View pView);
    protected abstract int getViewId();

    protected void LOGI(String msg) {
        Log.i(TAG(), msg);
    }

    protected void LOGE(String msg) {
        Log.e(TAG(), msg);
    }

    protected void LOGD(String msg) {
        Log.d(TAG(), msg);
    }

    protected void showToastL(String msg) {
        Toast.makeText(requireActivity(), msg, Toast.LENGTH_LONG).show();
        LOGD("showToastL: " + msg);
    }

    protected void showToastS(String msg) {
        Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show();
        LOGD("showToastS: " + msg);
    }
}

