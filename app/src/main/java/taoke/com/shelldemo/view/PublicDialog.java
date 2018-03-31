package taoke.com.shelldemo.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import taoke.com.shelldemo.R;

/**
 * 升级弹出框
 *
 * @author sh
 */
public class PublicDialog extends Dialog {

    TextView mTvTitle;
    TextView mTvContent;
    LinearLayout mLLClose;
    LinearLayout mLLConform;
    Button mBtnClose;
    Button mBtnConform;

    Builder mBuilder;

    public PublicDialog(Builder builder) {

        super(builder.mContext, R.style.dialog_public);
        mBuilder = builder;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_public);
        this.initView();

    }

    private void initView() {

        mTvTitle = (TextView) this.findViewById(R.id.tv_title);
        mTvContent = (TextView) this.findViewById(R.id.tv_content);
        mBtnClose = (Button) this.findViewById(R.id.btn_close);
        mLLClose = (LinearLayout) this.findViewById(R.id.lay_close);
        mBtnConform = (Button) this.findViewById(R.id.btn_conform);
        mLLConform = (LinearLayout) this.findViewById(R.id.lay_conform);

        if (TextUtils.isEmpty(mBuilder.title)) {
            mTvTitle.setVisibility(View.GONE);
        } else {
            mTvTitle.setVisibility(View.VISIBLE);
            mTvTitle.setText(mBuilder.title);
        }
        mTvContent.setText(mBuilder.content);
        mBtnClose.setText(mBuilder.cancel);
        mBtnConform.setText(mBuilder.conform);
        mBtnClose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (mBuilder.cancelListener != null) {
                    mBuilder.cancelListener.onClick(v);
                }
                dismiss();
            }
        });
        mBtnConform.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (mBuilder.conformListener != null) {
                    mBuilder.conformListener.onClick(v);
                }
                dismiss();
            }
        });
        switch (mBuilder.type){
            case CANCEL_CONFORM:
                mLLClose.setVisibility(View.VISIBLE);
                mLLConform.setVisibility(View.VISIBLE);
                break;
            case CONFORM:
                mLLClose.setVisibility(View.GONE);
                mLLConform.setVisibility(View.VISIBLE);
                break;
            case CANCEL:
                mLLClose.setVisibility(View.VISIBLE);
                mLLConform.setVisibility(View.GONE);
                break;
        }

    }

    public static final class Builder {

        private Context mContext;
        private String content;
        private String title;
        private String cancel;
        private String conform;
        private ShowEnum type = ShowEnum.CANCEL_CONFORM;
        private View.OnClickListener cancelListener;
        private View.OnClickListener conformListener;

        public enum ShowEnum{
            CANCEL_CONFORM,CANCEL,CONFORM
        }
        public Builder(Context context) {

            this.mContext = context;
        }

        public Builder setType(ShowEnum type) {
            this.type = type;
            return this;
        }

        public Builder setContent(String content) {

            this.content = content;
            return this;
        }

        public Builder setTitle(String title) {

            this.title = title;
            return this;
        }

        public Builder setCancel(String cancel) {

            this.cancel = cancel;
            return this;
        }

        public Builder setConform(String conform) {

            this.conform = conform;
            return this;
        }

        public Builder setConformListenr(View.OnClickListener conformListener) {

            this.conformListener = conformListener;
            return this;
        }

        public Builder setCancelListenr(View.OnClickListener cancelListener) {

            this.cancelListener = cancelListener;
            return this;
        }

        public PublicDialog build() {

            return new PublicDialog(this);
        }

        public void show() {

            PublicDialog dialog = build();
            dialog.show();
        }
    }
}