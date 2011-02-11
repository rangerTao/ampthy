package com.input.floatinput;

import android.inputmethodservice.InputMethodService;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;

public class FloatInput extends InputMethodService {
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate();
        this.onCreateInputMethodInterface();
        EditorInfo ei = new EditorInfo();
        this.onStartInput(ei, true);
        this.onCreateInputView();
        this.onStartInputView(ei, true);
    }
}