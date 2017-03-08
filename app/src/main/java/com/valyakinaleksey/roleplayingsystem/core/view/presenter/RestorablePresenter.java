package com.valyakinaleksey.roleplayingsystem.core.view.presenter;


import com.valyakinaleksey.roleplayingsystem.core.view.view_model.ShouldRequestUpdateViewModel;

/**
 * Add presenter ability to restore view model after low - memory
 *
 * Can be useful with complex screens where separation of one state-full presenter into several
 * state-less presenters seems like overkill
 */
public interface RestorablePresenter<M extends ShouldRequestUpdateViewModel> {
    void restoreViewModel(M viewModel);
}
