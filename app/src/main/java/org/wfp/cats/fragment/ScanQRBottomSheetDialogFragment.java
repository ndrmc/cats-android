package org.wfp.cats.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.wfp.cats.R;
import org.wfp.cats.model.Received;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ScanQRBottomSheetDialogFragment extends BottomSheetDialogFragment {

    @BindView(R.id.take_qr)
    TextView takeQRButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_scan_qr_bottom_sheet, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    public static ScanQRBottomSheetDialogFragment newInstance() {
        return new ScanQRBottomSheetDialogFragment();
    }

    @OnClick(R.id.take_qr) void takeQR() {
        new IntentIntegrator(getActivity()).initiateScan();
        dismiss();
    }
}
