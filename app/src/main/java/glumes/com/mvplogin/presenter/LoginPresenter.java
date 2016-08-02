package glumes.com.mvplogin.presenter;

import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;
import java.util.List;

import glumes.com.mvplogin.LoginContract;
import glumes.com.mvplogin.model.LoginModel;

/**
 * Created by zhaoying on 16/8/1.
 */
public class LoginPresenter implements LoginContract.Presenter {

    private LoginContract.View mView;
    private LoginContract.Model mModel;
    private List<String> initInfo = new ArrayList<>();
    private Handler UiHandler;

    /**
     * 构造方法中通过 View 的 setPresenter 让 View 获得了 Presenter 的实例
     * 这样,在 View 中就可以对 Presenter 中的方法进行操作了
     *
     * @param view
     */
    public LoginPresenter(LoginContract.View view) {
        mView = view;
        /**
         *  Presenter 绑定 View
         */
        mView.setPresenter(this);

        /**
         * Presenter 引用数据层 Model
         */

        mModel = new LoginModel();

        mModel.setPresenter(this);

        UiHandler = new Handler(Looper.getMainLooper());
    }


    @Override
    public void start() {
        /**
         * 初始化数据和UI界面
         */
        if (mView.isActive()) {
            initInfo = mModel.LoaderInitData();
            mView.setEmail(initInfo.get(0));
            mView.setPassword(initInfo.get(1));
        }
    }

    @Override
    public void showLoadingDialog() {
        mView.showLoading();
    }

    @Override
    public void cancelLoadingDialog() {
        mView.hideLoading();
        mView.setEmail("cancel");
    }

    /**
     * @param email
     * @param password
     */
    @Override
    public void checkUserInfo(String email, String password) {

        if (mView.isActive()) {
            mModel.checkUserInfo(email, password, new LoginContract.CallBack() {
                @Override
                public void LoginSuccess() {
                    UiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            cancelLoadingDialog();
                            mView.toActivity();
                        }
                    });
                }

                @Override
                public void LoginFailed() {
                    UiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            cancelLoadingDialog();
                            mView.setEmail("cancel login");
                        }
                    });
                }
            });
        }
    }
}
