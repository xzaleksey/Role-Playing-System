package com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.base;

import com.annimon.stream.Stream;
import com.valyakinaleksey.roleplayingsystem.core.view.LceView;
import com.valyakinaleksey.roleplayingsystem.core.view.view_model.ShouldRequestUpdateViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Base ViewState implementation for {@link LceView} with view-based navigation
 */
public abstract class AbsNavigationLceViewStateImpl<D extends ShouldRequestUpdateViewModel, V extends LceView<D>, T>
                        extends AbsLceViewStateImpl<D, V>
                        implements NavigationViewState<V, T> {

    protected List<PendingStateChange<V>> pendingStateChangesList;

    public AbsNavigationLceViewStateImpl() {
        pendingStateChangesList = new ArrayList<>();
    }

    protected void addPendingStateChange(PendingStateChange<V> dto) {
        pendingStateChangesList.add(dto);
        onPendingStateChangesListAdded();
    }

    protected void onPendingStateChangesListAdded() {
        // custom logic on item added
        // override if necessary
    }

    protected void setPendingStateChangeList(List<PendingStateChange<V>> list) {
        pendingStateChangesList = new ArrayList<>(list);
    }



    @Override
    public void apply(V view) {
        super.apply(view);
        runPendingOperations(view);
    }

    private void runPendingOperations(V view) {
        Stream.of(pendingStateChangesList).forEach(pendingChange -> pendingChange.apply(view));
        pendingStateChangesList.clear();
    }
}
