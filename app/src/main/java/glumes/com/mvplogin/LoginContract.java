package glumes.com.mvplogin;

import java.util.List;

/**
 * Created by zhaoying on 16/8/1.
 */
public interface LoginContract {

    /**
     * View 层的接口
     */
    interface View extends BaseView<Presenter> {
        void showLoading();

        void hideLoading();

        void setEmail(String email);

        void setPassword(String password);

        void toActivity();

        boolean isActive();
    }

    /**
     * Presenter 层的接口
     */
    interface Presenter extends BasePresenter {
        void showLoadingDialog();

        void cancelLoadingDialog();

        void checkUserInfo(String email,String  password);
    }

    /**
     * Model 层的接口
     */
    interface Model {

        List<String> LoaderInitData();

        void checkUserInfo(String email,String password,LoginContract.CallBack callBack);

    }

    /**
     * Model 层传递数据到 Presenter 层时的回调接口
     */
    interface CallBack {
        void LoginSuccess();
        void LoginFailed();
    }
}
