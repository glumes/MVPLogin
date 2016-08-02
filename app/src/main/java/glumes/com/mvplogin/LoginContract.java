package glumes.com.mvplogin;

import java.util.List;

/**
 * Created by zhaoying on 16/8/1.
 */
public interface LoginContract {

    interface View extends BaseView<Presenter> {
        void showLoading();

        void hideLoading();

        void setEmail(String email);

        void setPassword(String password);

        void toActivity();

        boolean isActive();
    }

    interface Presenter extends BasePresenter {
        void showLoadingDialog();

        void cancelLoadingDialog();

        void checkUserInfo(String email,String  password);
    }

    interface Model {

        List<String> LoaderInitData();

        boolean checkUserInfo(String email,String password,LoginContract.CallBack callBack);

        void setPresenter(LoginContract.Presenter presenter);
    }

    interface CallBack {
        void LoginSuccess();
        void LoginFailed();
    }
}
