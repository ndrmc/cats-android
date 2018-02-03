package org.wfp.cats.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.wfp.cats.R;
import org.wfp.cats.model.Received;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ReceivedItemDetailBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private Received receivedItem;

    @BindView(R.id.grn)
    TextView grn;

    @BindView(R.id.date_received)
    TextView dateReceived;

    @BindView(R.id.hub)
    TextView hub;

    @BindView(R.id.warehouse)
    TextView warehouse;

    @BindView(R.id.transporter)
    TextView transporter;

    @BindView(R.id.quantity)
    TextView quantity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_received_item_bottom_sheet, container, false);
        ButterKnife.bind(this, rootView);

        grn.setText(receivedItem.getGRN());
        DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-mm-dd");
        dateReceived.setText(format.print(receivedItem.getReceivingDate()));
        hub.setText(receivedItem.getHub());
        warehouse.setText(receivedItem.getWarehouse());
        transporter.setText(receivedItem.getTransporter());
        quantity.setText(getString(R.string.number_double, receivedItem.getQuantity()));
        return rootView;
    }

    public static ReceivedItemDetailBottomSheetDialogFragment newInstance(Received item) {
        ReceivedItemDetailBottomSheetDialogFragment dialog = new ReceivedItemDetailBottomSheetDialogFragment();
        dialog.setReceivedItem(item);
        return dialog;
    }

    public void setReceivedItem(Received receivedItem) {
        this.receivedItem = receivedItem;
    }
}
