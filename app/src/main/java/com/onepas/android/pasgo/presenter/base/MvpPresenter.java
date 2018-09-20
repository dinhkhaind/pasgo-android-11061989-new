package com.onepas.android.pasgo.presenter.base;

public interface MvpPresenter<V extends MvpView> {
    void onAttach(V mvpView);

    void onDetach();

}
