package org.wfp.cats.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.wfp.cats.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReceivingFragment extends Fragment {

    private ReceivingFragmentListener nextListener;

    @BindView(R.id.scan_button)
    Button scanButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_receiving, container, false);

        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @OnClick(R.id.scan_button)
    void scan() {
        new IntentIntegrator(getActivity()).initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(getContext(), "Canceled", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), result.getContents(), Toast.LENGTH_SHORT).show();
                nextListener.next();
            }
        }
    }

    public static Fragment newInstance(ReceivingFragmentListener listener) {
        ReceivingFragment fragment = new ReceivingFragment();
        fragment.nextListener = listener;
        return fragment;
    }

    public interface ReceivingFragmentListener {
        void next();
    }
}
