package com.example.shopiki.fragments;

import static com.example.shopiki.models.Constant.contract;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.shopiki.R;
import com.example.shopiki.models.Constant;

public class ContractFrangment extends Fragment {

    TextView type;
    EditText name, email, content;
    Button submit;
    private String adminemail = "chieumu1999@gmail.com";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frangment_contract, container, false);

        type = root.findViewById(R.id.type_contract);
        name = root.findViewById(R.id.name_contract);
        email = root.findViewById(R.id.email_contract);
        content = root.findViewById(R.id.content_contract);
        submit = root.findViewById(R.id.submit);

        type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                typeDialog();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                input();
            }
        });

        return root;
    }

    private String subjectmail, contentmail, namemail, numberphonemail;

    private void input() {
        subjectmail = type.getText().toString().trim();
        contentmail = content.getText().toString().trim();
        namemail = name.getText().toString().trim();
        numberphonemail = email.getText().toString().trim();

        //check input
        if (TextUtils.isEmpty(namemail)) {
            Toast.makeText(getContext(), "Nhập tên bạn", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(numberphonemail)) {
            Toast.makeText(getContext(), "Nhập số điện thoại!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(subjectmail)) {
            Toast.makeText(getContext(), "Chọn vấn đề liên hệ!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(contentmail)) {
            Toast.makeText(getContext(), "Nhập nội dung email cần trao đổi!", Toast.LENGTH_SHORT).show();
            return;
        }

        sendEmail();
    }

    private void sendEmail() {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{"chieumu1999@gmail.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, type.getText().toString());
        i.putExtra(Intent.EXTRA_TEXT, name.getText().toString() + ": " + email.getText().toString() + "\n" + content.getText().toString());
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getContext(), "No email client configured. Please check.", Toast.LENGTH_SHORT).show();
        }
    }

    private void typeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Vấn Đề Liên Lạc")
                .setItems(contract, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String typecontract = contract[i];
                        type.setText(typecontract);

                    }
                })
                .show();
    }
}
