package com.valyakinaleksey.roleplayingsystem.core.view;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

import com.valyakinaleksey.roleplayingsystem.core.interfaces.DialogProvider;

public class BaseDialogFragment extends DialogFragment {

    public static <T extends Fragment & DialogProvider> BaseDialogFragment newInstance(T parentFragment) {
        Bundle args = new Bundle();
        BaseDialogFragment fragment = new BaseDialogFragment();
        fragment.setArguments(args);
        fragment.setTargetFragment(parentFragment, -1);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return ((DialogProvider) getTargetFragment()).getDialog(getTag());
    }

    @Override
    public void onDestroyView() {
        Dialog dialog = getDialog();
        // handles https://code.google.com/p/android/issues/detail?id=17423
        if (dialog != null && getRetainInstance()) {
            dialog.setDismissMessage(null);
        }
        super.onDestroyView();
    }

}
      