package com.valyakinaleksey.roleplayingsystem.core.view;


import com.valyakinaleksey.roleplayingsystem.core.view.view_model.EmptyViewModel;

/**
 * Interface for Load-Content-Error view
 */
public interface LceView<D extends EmptyViewModel, E> extends View {

	void showLoading();
	void hideLoading();

	void setData(D data);
	void showContent();

	void showError(E error);

	void loadData();
}
