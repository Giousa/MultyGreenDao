package com.zmm.multygreendao;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.zmm.multygreendao.gen.CustomerDao;
import com.zmm.multygreendao.gen.OrderDao;
import com.zmm.multygreendao.manager.GreenDaoManager;
import com.zmm.multygreendao.model.Customer;
import com.zmm.multygreendao.model.Order;
import com.zmm.multygreendao.utils.LogUtils;
import com.zmm.multygreendao.utils.ToastUtils;

import java.util.List;

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
    private CustomerDao mCustomerDao;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ButterKnife.inject(this);

        mCustomerID = getIntent().getLongExtra(KEY_ID, 0);
        LogUtils.d("order keyId = "+ mCustomerID);
        mOrderDao = GreenDaoManager.getInstance().getOrderDao();
        mCustomerDao = GreenDaoManager.getInstance().getCustomerDao();
        initView();
    }

    private void initView() {
    }

    @OnClick({R.id.add, R.id.update, R.id.query, R.id.delete})
    public void onClick(View view) {

        hideKeyBoard();

        switch (view.getId()) {
            case R.id.add:
                insertData();
                break;
            case R.id.update:
                break;
            case R.id.query:
                queryData();
                break;
            case R.id.delete:
                break;
        }
    }

    private void queryData() {
        List<Order> orderList = mOrderDao.loadAll();
        if(orderList != null && orderList.size()>0){
            for (Order order:orderList) {
                long customerId = order.getCustomerId();
                Customer customer = mCustomerDao.load(customerId);
                LogUtils.d("name = "+customer.getName()+",age = "+customer.getAge()+",url = "+order.getUrl());
            }
        }
    }

    private void insertData() {
        if (TextUtils.isEmpty(mUrl.getText())) {
            ToastUtils.SimpleToast("姓名不能为空!");
            return;
        }

        if (TextUtils.isEmpty(mType.getText())) {
            ToastUtils.SimpleToast("年龄不能为空!");
            return;
        }

        Order order = new Order(null,mCustomerID,"~~~",mUrl.getText().toString(),Integer.parseInt(mType.getText().toString()));
        long insert = mOrderDao.insert(order);
        LogUtils.d("Order 添加数据成功:" + insert);
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
