package org.wfp.cats;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import org.wfp.cats.fragment.ReceivingFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.riddhimanadib.formmaster.FormBuilder;
import me.riddhimanadib.formmaster.model.BaseFormElement;
import me.riddhimanadib.formmaster.model.FormElementPickerSingle;
import me.riddhimanadib.formmaster.model.FormElementTextEmail;
import me.riddhimanadib.formmaster.model.FormElementTextNumber;
import me.riddhimanadib.formmaster.model.FormElementTextSingleLine;
import me.riddhimanadib.formmaster.model.FormHeader;

public class ReceivingFormActivity extends AppCompatActivity {

    @BindView(R.id.formsRecycler)
    RecyclerView formsRecycler;
    private FormBuilder mFormBuilder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiving_form);
        ButterKnife.bind(this);

        String projectCode = "";

        String commodityType = "";

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            projectCode = bundle.getString(ReceivingFragment.PROJECT_CODE_IDENTIFIER);
            commodityType = bundle.getString(ReceivingFragment.COMMODITY_TYPE_IDENTIFIER);
        }

        mFormBuilder = new FormBuilder(this, formsRecycler);

        // Project info form elements
        FormHeader header = FormHeader.createInstance(getString(R.string.project_info));
        FormElementTextSingleLine projectCodeInput = FormElementTextSingleLine.createInstance().setTitle(getString(R.string.project_code)).setHint(getString(R.string.enter_email)).setValue(projectCode);
        FormElementTextSingleLine commodityTypeInput = FormElementTextSingleLine.createInstance().setTitle(getString(R.string.commodity_type)).setHint(getString(R.string.select_commodity)).setValue(commodityType);

        // Receiving details form elements
        FormHeader header2 = FormHeader.createInstance(getString(R.string.receiving_details));
        FormElementTextSingleLine grnLabelInput = FormElementTextSingleLine.createInstance().setTitle(getString(R.string.grn_label)).setHint(getString(R.string.grn_label));
        FormElementTextSingleLine waybillInput = FormElementTextSingleLine.createInstance().setTitle(getString(R.string.waybill_label)).setHint(getString(R.string.waybill_label));
        List<String> uoms = new ArrayList<>();
        uoms.add("kg");
        uoms.add("quintal");
        FormElementPickerSingle uomPicker = FormElementPickerSingle.createInstance().setTitle(getString(R.string.uom)).setOptions(uoms).setHint(getString(R.string.pick_uom));
        FormElementTextNumber quantityInput = FormElementTextNumber.createInstance().setTitle(getString(R.string.quantity)).setHint(getString(R.string.quantity));
        List<String> transporters = new ArrayList<>();
        transporters.add("Transporter 1");
        transporters.add("Transporter 2");
        FormElementPickerSingle transporterPicker = FormElementPickerSingle.createInstance().setTitle(getString(R.string.transporter)).setOptions(transporters).setHint(getString(R.string.pick_transporter));

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
}
