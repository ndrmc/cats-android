package org.wfp.cats;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.wfp.cats.fragment.ReceivingFragment;
import org.wfp.cats.fragment.ScanQRBottomSheetDialogFragment;
import org.wfp.cats.model.Received;
import org.wfp.cats.model.Transporter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.objectbox.Box;
import io.objectbox.BoxStore;
import me.riddhimanadib.formmaster.FormBuilder;
import me.riddhimanadib.formmaster.model.BaseFormElement;
import me.riddhimanadib.formmaster.model.FormElementPickerSingle;
import me.riddhimanadib.formmaster.model.FormElementTextNumber;
import me.riddhimanadib.formmaster.model.FormElementTextSingleLine;
import me.riddhimanadib.formmaster.model.FormHeader;

public class ReceivingFormActivity extends AppCompatActivity {

    private static final int PROJECT_CODE_TAG = 1;
    @BindView(R.id.formsRecycler)
    RecyclerView formsRecycler;
    private FormBuilder mFormBuilder;

    @BindView(R.id.save_form)
    TextView saveFormButton;

    @BindView(R.id.scan_qr_code)
    TextView scanQRCode;

    FormElementTextSingleLine projectCodeInput;

    FormElementTextSingleLine commodityTypeInput;

    private FormElementTextSingleLine grnLabelInput;
    private FormElementTextSingleLine waybillInput;
    private FormElementPickerSingle uomPicker;
    private FormElementTextNumber quantityInput;
    private FormElementPickerSingle transporterPicker;
    private ScanQRBottomSheetDialogFragment dialogFragment;
    private BoxStore boxStore;
    private Box<Received> receivedBox;
    private Box<Transporter> transporterBox;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiving_form);
        ButterKnife.bind(this);

        boxStore = ((CatsApplication) getApplication()).getBoxStore();
        transporterBox = boxStore.boxFor(Transporter.class);
        receivedBox = boxStore.boxFor(Received.class);

        String projectCode = "";
        String commodityType = "";

        dialogFragment = ScanQRBottomSheetDialogFragment.newInstance();

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            projectCode = bundle.getString(ReceivingFragment.PROJECT_CODE_IDENTIFIER);
            commodityType = bundle.getString(ReceivingFragment.COMMODITY_TYPE_IDENTIFIER);
        } else {
            showMenuSheet();
        }

        mFormBuilder = new FormBuilder(this, formsRecycler);

        // Project info form elements
        FormHeader header = FormHeader.createInstance(getString(R.string.project_info));
        projectCodeInput = FormElementTextSingleLine.createInstance().setTitle(getString(R.string.project_code)).setHint(getString(R.string.enter_project_code)).setTag(PROJECT_CODE_TAG).setValue(projectCode).setRequired(true);
        commodityTypeInput = FormElementTextSingleLine.createInstance().setTitle(getString(R.string.commodity)).setHint(getString(R.string.select_commodity)).setValue(commodityType).setRequired(true);

        // Receiving details form elements
        FormHeader header2 = FormHeader.createInstance(getString(R.string.receiving_details));
        grnLabelInput = FormElementTextSingleLine.createInstance().setTitle(getString(R.string.grn_label)).setHint(getString(R.string.grn_label)).setRequired(true);
        waybillInput = FormElementTextSingleLine.createInstance().setTitle(getString(R.string.waybill_label)).setHint(getString(R.string.waybill_label)).setRequired(true);
        List<String> uoms = new ArrayList<>();
        uoms.add("kg");
        uoms.add("quintal");
        uomPicker = FormElementPickerSingle.createInstance().setTitle(getString(R.string.uom)).setOptions(uoms).setHint(getString(R.string.pick_uom)).setRequired(true);
        quantityInput = FormElementTextNumber.createInstance().setTitle(getString(R.string.quantity)).setHint(getString(R.string.quantity)).setRequired(true);
        List<String> transporters = new ArrayList<>();
        for(Transporter transporter : transporterBox.getAll()) {
            transporters.add(transporter.getName());
        }
        transporterPicker = FormElementPickerSingle.createInstance().setTitle(getString(R.string.transporter)).setOptions(transporters).setHint(getString(R.string.pick_transporter)).setRequired(true);

        List<BaseFormElement> projectInfoFormItems = new ArrayList<>();
        projectInfoFormItems.add(header);
        projectInfoFormItems.add(projectCodeInput);
        projectInfoFormItems.add(commodityTypeInput);
        projectInfoFormItems.add(header2);
        projectInfoFormItems.add(grnLabelInput);
        projectInfoFormItems.add(waybillInput);
        projectInfoFormItems.add(uomPicker);
        projectInfoFormItems.add(quantityInput);
        projectInfoFormItems.add(transporterPicker);

        mFormBuilder.addFormElements(projectInfoFormItems);
    }

    @OnClick(R.id.save_form) void saveForm() {
        // validate the form inputs
        if(!mFormBuilder.isValidForm()) {
            Toast.makeText(this, getString(R.string.invalid_form), Toast.LENGTH_SHORT).show();
            return;
        }

        Received r = new Received();
        r.setProjectCode(projectCodeInput.getValue());
        r.setCommodityType(commodityTypeInput.getValue());
        r.setGRN(grnLabelInput.getValue());
        r.setWayBill(waybillInput.getValue());
        r.setUom(uomPicker.getValue());
        r.setQuantity(Double.parseDouble(quantityInput.getValue()));
        r.setTransporter(transporterPicker.getValue());
        r.setReceivingDate(System.currentTimeMillis());
        r.setWarehouse("Warehouse");
        r.setHub("Hub");

        // Save the box
        receivedBox.put(r);
        Toast.makeText(this, getString(R.string.saved), Toast.LENGTH_SHORT).show();
        finish();
    }

    private void showMenuSheet() {
        dialogFragment.show(getSupportFragmentManager(), "QR Code");
    }

    @OnClick(R.id.scan_qr_code) void scan() {
        new IntentIntegrator(this).initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
            } else {
                // project code and commodity are separated by semi colon
                String[] inputs = TextUtils.split(result.getContents(), ";");
                if(inputs.length > 1) {
                    projectCodeInput.setValue(inputs[0]);
                    commodityTypeInput.setValue(inputs[inputs.length - 1]);
                    mFormBuilder.refresh();
                } else {
                    Toast.makeText(this, getString(R.string.malformed_qr), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
