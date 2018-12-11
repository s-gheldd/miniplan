package de.sauroter.miniplan.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageButton;

import butterknife.ButterKnife;
import butterknife.OnClick;
import de.sauroter.miniplan.data.AltarService;
import de.sauroter.miniplan.miniplan.R;
import de.sauroter.miniplan.miniplan.databinding.ActivityDetailBinding;

public class DetailActivity extends AppCompatActivity {

    public static final String DETAIL_ALTAR_SERVICE = "DETAIL_ALTAR_SERVICE";

    private ActivityDetailBinding mBinding;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        ButterKnife.bind(this);
    }


    @Override
    protected void onStart() {
        super.onStart();

        final Intent intent = this.getIntent();
        final AltarService altarService = (AltarService) intent.getSerializableExtra(DETAIL_ALTAR_SERVICE);
        mBinding.setAltarService(altarService);
    }

    @OnClick(value = R.id.detail_done_button)
    public void done(final ImageButton button) {
        finish();
    }
}
