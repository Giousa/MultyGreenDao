package com.zmm.multygreendao;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.zmm.multygreendao.gen.OrderDao;
import com.zmm.multygreendao.manager.GreenDaoManager;
import com.zmm.multygreendao.utils.LogUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Description:
 * Author:zhangmengmeng
 * Date:2017/3/22
 * Time:下午2:54
 */

public class OrderActivity extends AppCompatActivity {

    @InjectView(R.id.et_url)
    EditText mUrl;
    @InjectView(R.id.et_type)
    EditText mType;
    @InjectView(R.id.recycleview)
    RecyclerView mRecycleview;

    private final String KEY_ID = "KEYID";

    private InputMethodManager mInputMethodManager;
    private long mCustomerID;
    private OrderDao mOrderDao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ButterKnife.inject(this);

        mCustomerID = getIntent().getLongExtra(KEY_ID, 0);
        LogUtils.d("order keyId = "+ mCustomerID);
        mOrderDao = GreenDaoManager.getInstance().getOrderDao();
        initView();
    }

    private void initView() {
    }

    @OnClick({R.id.add, R.id.update, R.id.query, R.id.delete})
    public void onClick(View view) {

        hideKeyBoard();

        switch (view.getId()) {
            case R.id.add:
                break;
            case R.id.update:
                break;
            case R.id.query:
                break;
            case R.id.delete:
                break;
        }
    }

    /**
     * 隐藏键盘
     */
    protected void hideKeyBoard() {
        if (mInputMethodManager == null) {
            mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        }
        mInputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }
}
