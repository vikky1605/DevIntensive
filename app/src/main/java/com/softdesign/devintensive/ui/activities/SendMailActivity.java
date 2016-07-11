package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.softdesign.devintensive.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/** Активность используется для получения деталей отправляемого пользователем e-mail:
 *  темы и текста письма
  */
public class SendMailActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.mail_subject)EditText mSubjectMail;
    @BindView(R.id.mail_text)EditText mTextMail;
    @BindView(R.id.button_send_mail)Button mButtonSendMail;
    String mUserMail;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_mail);

        ButterKnife.bind(this);
        mUserMail = getIntent().getStringExtra("E-mail");



        mButtonSendMail.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL,new String[] {mUserMail});
        intent.putExtra(Intent.EXTRA_SUBJECT, mSubjectMail.getText().toString());
        intent.putExtra(Intent.EXTRA_TEXT, mTextMail.getText().toString());

        try {
            startActivity(Intent.createChooser(intent, "Send mail"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(SendMailActivity.this, "There are no email applications installed.", Toast.LENGTH_SHORT).show();
        }


    }

}
