package org.wfp.cats.adapter;

import android.content.Context;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.wfp.cats.R;
import org.wfp.cats.model.Received;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReceivedItemsRecyclerAdapter extends RecyclerView.Adapter<ReceivedItemsRecyclerAdapter.ViewHolder> {

    private final List<Received> items;
    private final Context context;
    private final OnItemSelectedListener listener;

    public ReceivedItemsRecyclerAdapter(Context context, List<Received> items, OnItemSelectedListener listener) {
        this.items = items;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.received_single_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Received received = items.get(position);
        holder.bind(received);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.grn)
        TextView grn;

        @BindView(R.id.date_received)
        TextView dateReceived;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(ViewHolder.this, itemView);
        }

        void bind(Received received) {
            grn.setText(context.getString(R.string.grn_text, received.getGRN()));
            DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
            dateReceived.setText(context.getString(R.string.received_on, fmt.print(received.getReceivingDate())));
        }

        @OnClick(R.id.grn) void showDetails() {
            listener.selected(getAdapterPosition());
        }
    }

    public interface OnItemSelectedListener {
        void selected(int position);
    }
}
