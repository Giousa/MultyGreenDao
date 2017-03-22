package com.zmm.multygreendao.manager;

import com.zmm.multygreendao.gen.CustomerDao;
import com.zmm.multygreendao.gen.DaoMaster;
import com.zmm.multygreendao.gen.DaoSession;
import com.zmm.multygreendao.gen.OrderDao;
import com.zmm.multygreendao.utils.UIUtils;

/**
 * Description:
 * Author:zhangmengmeng
 * Date:2017/3/22
 * Time:上午9:12
 */

public class GreenDaoManager {

    private static GreenDaoManager mInstance;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    public static GreenDaoManager getInstance() {
        if (mInstance == null) {
            mInstance = new GreenDaoManager();
        }
        return mInstance;
    }

    public GreenDaoManager() {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(UIUtils.getContext(), "customer_db", null);
        DaoMaster mDaoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        mDaoSession = mDaoMaster.newSession();
    }

    public DaoMaster getMaster() {
        return mDaoMaster;
    }

    public DaoSession getSession() {
        return mDaoSession;
    }

    public DaoSession getNewSession() {
        mDaoSession = mDaoMaster.newSession();
        return mDaoSession;
    }

    public CustomerDao getCustomerDao(){
        return mDaoSession.getCustomerDao();
    }

    public OrderDao getOrderDao(){
        return mDaoSession.getOrderDao();
    }
}
