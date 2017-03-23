package com.zmm.multygreendao;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.zmm.multygreendao.adapter.MyAdapter;
import com.zmm.multygreendao.gen.CustomerDao;
import com.zmm.multygreendao.manager.GreenDaoManager;
import com.zmm.multygreendao.model.Customer;
import com.zmm.multygreendao.model.Order;
import com.zmm.multygreendao.utils.LogUtils;
import com.zmm.multygreendao.utils.ToastUtils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getSimpleName();

    @InjectView(R.id.name)
    EditText mName;
    @InjectView(R.id.age)
    EditText mAge;
    @InjectView(R.id.sex)
    CheckBox mSex;
    @InjectView(R.id.recycleview)
    RecyclerView mRecycleview;

    private final String KEY_ID = "KEYID";

    private InputMethodManager mInputMethodManager;
    private boolean mGender;
    private CustomerDao mCustomerDao;
    private List<Customer> mCustomers;
    private MyAdapter mMyAdapter;
    private long mKeyId = -1;
    private int mSelectPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        mCustomerDao = GreenDaoManager.getInstance().getCustomerDao();
        initView();
    }

    private void initView() {

        mSex.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                hideKeyBoard();
                if (isChecked) {
                    Log.d(TAG, "---man---");
                    mGender = true;
                } else {
                    Log.d(TAG, "---women---");
                    mGender = false;
                }
            }
        });

        mCustomers = mCustomerDao.loadAll();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycleview.setLayoutManager(layoutManager);
        mMyAdapter = new MyAdapter(mCustomers);
        mRecycleview.setAdapter(mMyAdapter);

        mMyAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position, long keyId) {
                LogUtils.d("keyId = " + keyId);
                mKeyId = keyId;
                mSelectPosition = position;
            }
        });

    }

    @OnClick({R.id.add, R.id.update, R.id.query, R.id.delete,R.id.order})
    public void onClick(View view) {

        hideKeyBoard();

        switch (view.getId()) {
            case R.id.add:
                checkEdit();
                break;
            case R.id.update:
                updateData();
                break;
            case R.id.query:
                queryData();
                break;
            case R.id.delete:
                deleteData();
                break;
            case R.id.order:
                jumpOrderAct();
                break;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mMyAdapter.update(mSelectPosition);
    }

    private void jumpOrderAct() {
        if (mSelectPosition < 0 || mMyAdapter.getLayoutPosition() < 0) {
            ToastUtils.SimpleToast("请选中条目");
            return;
        }

        Intent intent = new Intent(MainActivity.this, OrderActivity.class);
        intent.putExtra(KEY_ID,mKeyId);
        startActivity(intent);
    }

    /**
     * 更新数据
     */
    private void updateData() {

        if (TextUtils.isEmpty(mName.getText())) {
            ToastUtils.SimpleToast("姓名不能为空!");
            return;
        }

        if (TextUtils.isEmpty(mAge.getText())) {
            ToastUtils.SimpleToast("年龄不能为空!");
            return;
        }

        if (mSelectPosition < 0 || mMyAdapter.getLayoutPosition() < 0) {
            ToastUtils.SimpleToast("请选中需要更新的条目");
            return;
        }
        Customer customer = mCustomerDao.load(mKeyId);
        customer.setName(mName.getText().toString());
        customer.setAge(Integer.parseInt(mAge.getText().toString()));
        mCustomerDao.update(customer);
        mMyAdapter.update(mSelectPosition);
    }

    /**
     * 删除数据
     */
    private void deleteData() {
        LogUtils.d("position = " + mSelectPosition + ",keyId = " + mKeyId);
        if (mSelectPosition < 0 || mMyAdapter.getLayoutPosition() < 0) {
            ToastUtils.SimpleToast("请选中需要删除的条目");
            return;
        }
        mCustomerDao.deleteByKey(mKeyId);
        mMyAdapter.delete(mSelectPosition);
    }

    /**
     * 查询数据
     */
    private void queryData() {
        mCustomers = mCustomerDao.loadAll();
        if (mCustomers != null && mCustomers.size() > 0) {
            for (int i = 0; i < mCustomers.size(); i++) {
                Customer customer = mCustomers.get(i);
                Log.d(TAG, "Customer:" + customer.getName() + "  id= " + customer.getId());
            }
        }
    }


    /**
     * 添加数据
     */
    private void insertData() {
        int age = Integer.parseInt(mAge.getText().toString());
        Customer customer = new Customer(null, mName.getText().toString(), mGender, age);
        long insert = mCustomerDao.insert(customer);
        LogUtils.d("添加数据成功:" + insert);
        mName.setText("");
        mAge.setText("");

        mMyAdapter.add(customer, 0);
        mRecycleview.scrollToPosition(0);
    }


    /**
     * 检验数据
     */
    private void checkEdit() {
        if (TextUtils.isEmpty(mName.getText())) {
            ToastUtils.SimpleToast("姓名不能为空!");
            return;
        }

        if (TextUtils.isEmpty(mAge.getText())) {
            ToastUtils.SimpleToast("年龄不能为空!");
            return;
        }

        insertData();
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
