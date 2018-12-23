package de.sauroter.miniplan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
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
