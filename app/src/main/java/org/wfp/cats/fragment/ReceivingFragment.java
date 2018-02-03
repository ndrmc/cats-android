package org.wfp.cats.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.wfp.cats.CatsApplication;
import org.wfp.cats.R;
import org.wfp.cats.ReceivingFormActivity;
import org.wfp.cats.adapter.ReceivedItemsRecyclerAdapter;
import org.wfp.cats.model.Received;
import org.wfp.cats.model.Received_;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.query.QueryBuilder;

public class ReceivingFragment extends Fragment {

    public static final String PROJECT_CODE_IDENTIFIER = "project-code";
    public static final String COMMODITY_TYPE_IDENTIFIER = "commodity-type";
    private static final int ADD_NEW_REQUEST_CODE = 1;
    @BindView(R.id.qr_code_image)
    ImageView scanButton;

    private List<Received> receivedItems;

    @BindView(R.id.received_items_recycler)
    RecyclerView receivedItemsRecycler;

    ReceivedItemsRecyclerAdapter mReceivedItemsRecyclerAdapter;

    @BindView(R.id.intro_container)
    LinearLayout introContainer;

    @BindView(R.id.add_new)
    FloatingActionButton addNewButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_receiving, container, false);

        ButterKnife.bind(this, rootView);

        loadReceivedItems();

        if (receivedItems.isEmpty()) {

            introContainer.setVisibility(View.VISIBLE);

        } else {

            mReceivedItemsRecyclerAdapter = new ReceivedItemsRecyclerAdapter(getContext(), receivedItems, this::showBottomSheet);

            receivedItemsRecycler.setAdapter(mReceivedItemsRecyclerAdapter);

            receivedItemsRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        }

        return rootView;
    }

    private void showBottomSheet(int position) {
        ReceivedItemDetailBottomSheetDialogFragment bottomSheetDialogFragment = ReceivedItemDetailBottomSheetDialogFragment.newInstance(receivedItems.get(position));
        bottomSheetDialogFragment.show(getActivity().getSupportFragmentManager(), "Detail bottom sheet dialog");
    }

    private void loadReceivedItems() {

        BoxStore boxStore = ((CatsApplication) getContext().getApplicationContext()).getBoxStore();

        Box<Received> receivedBox = boxStore.boxFor(Received.class);

        QueryBuilder<Received> builder = receivedBox.query();
        builder.order(Received_.receivingDate, QueryBuilder.DESCENDING);
        receivedItems = builder.build().find();

        if (!receivedItems.isEmpty()) {
            mReceivedItemsRecyclerAdapter = new ReceivedItemsRecyclerAdapter(getContext(), receivedItems, this::showBottomSheet);
            receivedItemsRecycler.setAdapter(mReceivedItemsRecyclerAdapter);
            mReceivedItemsRecyclerAdapter.notifyDataSetChanged();

            introContainer.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.scan_button)
    void scan() {
        new IntentIntegrator(getActivity()).initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(getContext(), "Canceled", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), result.getContents(), Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
                bundle.putString(PROJECT_CODE_IDENTIFIER, result.getContents());
                bundle.putString(COMMODITY_TYPE_IDENTIFIER, result.getContents());
                Intent intent = new Intent(getContext(), ReceivingFormActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }
    }

    @OnClick(R.id.add_new)
    void addNew() {
        startActivityForResult(new Intent(getActivity(), ReceivingFormActivity.class), ADD_NEW_REQUEST_CODE);
    }

    public static Fragment getInstance() {
        return new ReceivingFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadReceivedItems();
    }
}
