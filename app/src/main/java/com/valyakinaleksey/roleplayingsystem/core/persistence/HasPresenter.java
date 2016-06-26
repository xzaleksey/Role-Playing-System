package com.valyakinaleksey.roleplayingsystem.core.persistence;


import com.valyakinaleksey.roleplayingsystem.core.presenter.Presenter;

/**
 * Base interface for dagger components for fragments with presenters
 */
public interface HasPresenter<P extends Presenter> {
    P getPresenter();
}
