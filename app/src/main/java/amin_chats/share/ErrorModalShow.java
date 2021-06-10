package amin_chats.share;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import amin_chats.cursor.aminchats.R;

public class ErrorModalShow extends Dialog implements
        android.view.View.OnClickListener {

    public Context c;
    public Dialog d;
    public Button yes, no;
    public WebView webView;
    List<String> lists;
    public ErrorModalShow(Context a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
       // this.lists=lists;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.error_modal);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(lp);
        yes = (Button) findViewById(R.id.btnDialogAgree);
        yes.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnDialogAgree:
                dismiss();
                break;
            default:
                break;
        }
    }
    public interface ContactsAdapterListener {
        void onContactSelected(Location contact);
    }
}