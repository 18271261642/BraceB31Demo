package com.brace.android.b31.view;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.brace.android.b31.R;

/**
 * 密码输入框
 * Created by Admin
 * Date 2019/11/5
 */
public class CusInputEditView extends Dialog implements View.OnClickListener {

    private Button cancleBtn,sureBtn;
    private TextInputEditText inputEdit;
    private TextView titleTv;

    private CusInputDialogListener cusInputDialogListener;

    public void setCusInputDialogListener(CusInputDialogListener cusInputDialogListener) {
        this.cusInputDialogListener = cusInputDialogListener;
    }

    public CusInputEditView(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cus_input_dialog_view);

        initViews();

    }

    private void initViews() {
        cancleBtn = findViewById(R.id.inputDialogCancleBtn);
        sureBtn = findViewById(R.id.inputDialogSureBtn);
        inputEdit = findViewById(R.id.inputDialogEdit);
        titleTv = findViewById(R.id.inputDialogTitletTv);
        cancleBtn.setOnClickListener(this);
        sureBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.inputDialogCancleBtn) { //取消
            cancel();
            if (cusInputDialogListener != null)
                cusInputDialogListener.cusDialogCancle();
        } else if (id == R.id.inputDialogSureBtn) {   //确认
            Editable inputStr = inputEdit.getText();
            if (inputStr == null)
                return;
            if (inputStr.length() < 4)
                return;
            if (cusInputDialogListener != null)
                cusInputDialogListener.cusDialogSureData(inputStr.toString());
        }
    }

    public interface CusInputDialogListener{
        void cusDialogCancle();
        void cusDialogSureData(String data);
    }
}
