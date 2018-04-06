package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.diceresultadapter;

import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.valyakinaleksey.roleplayingsystem.R;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.viewholders.FlexibleViewHolder;

public class DiceTotalResultViewHolder extends FlexibleViewHolder {
    @BindView(R.id.tv_result_value)
    TextView diceResultValue;

    @BindView(R.id.tv_max_result_value)
    TextView diceMaxResultValue;


    public DiceTotalResultViewHolder(View itemView, FlexibleAdapter mAdapter) {
        super(itemView, mAdapter);
        ButterKnife.bind(this, itemView);
    }

    public void bind(DiceTotalResultViewModel diceSingleCollectionViewModel) {
        diceResultValue.setText(diceSingleCollectionViewModel.getCurrentResultValue());
        diceMaxResultValue.setText(diceSingleCollectionViewModel.getMaxResultValue());
    }

}
