package com.softdesign.devintensive.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** класс для валидации полей пользовательского ввода
  */
public class MyTextWatcher implements TextWatcher {

    public boolean checkMask; // true, если EditText соответствует маске; false, если не соответствует
    public String editTextType; // принимает значение phone, mail, vk или git
    public EditText editText;

    public MyTextWatcher (EditText editText, String editTextType) {
        this.editText = editText;
        this.editTextType = editTextType;
        this.checkMask = true;
    }

    private void setCheckMask (boolean checkMask) {
        this.checkMask = checkMask;
    }

    public boolean getCheckMask() {
        return this.checkMask;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    // в зависимости от редактируемого поля - запускаем методы по проверке соответствия заданной маске
    public void afterTextChanged(Editable s) {
        String str = this.editText.getText().toString();
        if (this.editTextType.equals("phone")) this.setCheckMask(checkPhoneNumber(str));
        if (this.editTextType.equals("mail")) this.setCheckMask(checkMail(str));
        if (this.editTextType.equals("vk")) this.setCheckMask(checkProfileVk(str));
        if (this.editTextType.equals("git")) this.setCheckMask(checkGit(str));
    }


    // проверяем соответствие маске введенного номера телефона
    private boolean checkPhoneNumber (String s) {
        if (s.length()<11 || s.length()>20) return false;
        else {
        Pattern p = Pattern.compile("^(\\+\\d)((\\s\\d{3}){2})((\\-\\d{2}){2})$");
        Matcher m = p.matcher(s);
        return m.matches();}
    }

    // проверяем правильность ввода e-mail
    private boolean checkMail (String s) {
        Pattern p = Pattern.compile("^(\\w{3,})@(\\w{2,})(\\.)(\\w{2,})$");
        Matcher m = p.matcher(s);
        return m.matches();
    }

    // проверяем правильность ввода ссылки на профиль VK
    private boolean checkProfileVk (String s) {
        Pattern p = Pattern.compile("^(vk\\.com/)(\\S{1,})$");
        Matcher m = p.matcher(s);
        return m.matches();
    }

    // проверяем правильность ввода ссылки на репо на github
    private boolean checkGit (String s) {
        Pattern p = Pattern.compile("^(github\\.com\\/)(\\S{1,})$");
        Matcher m = p.matcher(s);
        return m.matches();
    }
}
