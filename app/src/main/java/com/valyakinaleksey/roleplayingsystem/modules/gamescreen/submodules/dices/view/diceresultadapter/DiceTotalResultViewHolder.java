package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.view.diceresultadapter;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.valyakinaleksey.roleplayingsystem.R;
import com.valyakinaleksey.roleplayingsystem.modules.gamescreen.submodules.dices.presenter.DicePresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.viewholders.FlexibleViewHolder;

public class DiceTotalResultViewHolder extends FlexibleViewHolder {
    @BindView(R.id.tv_result_value)
    TextView diceResultValue;

    @BindView(R.id.tv_max_result_value)
    TextView diceMaxResultValue;

    @BindView(R.id.btn_rethrow_dices)
    Button btnRethrow;


    public DiceTotalResultViewHolder(View itemView, FlexibleAdapter mAdapter) {
        super(itemView, mAdapter);
        ButterKnife.bind(this, itemView);
    }

    public void bind(DiceTotalResultViewModel diceSingleCollectionViewModel, DicePresenter dicePresenter) {
        diceResultValue.setText(diceSingleCollectionViewModel.getCurrentResultValue());
        diceMaxResultValue.setText(diceSingleCollectionViewModel.getMaxResultValue());
        btnRethrow.setOnClickListener(v ->
                dicePresenter.throwDices()
        );
    }

}
