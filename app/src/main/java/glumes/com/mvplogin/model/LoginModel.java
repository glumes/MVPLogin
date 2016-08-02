package glumes.com.mvplogin.model;

import java.util.List;

import glumes.com.mvplogin.LoginContract;

/**
 * Created by zhaoying on 16/8/1.
 */
public class LoginModel implements LoginContract.Model {

    private LoginContract.Presenter mPresenter;

    public LoginModel() {
    }

    public static LoginModel newInstance() {
        return new LoginModel();
    }

    public void setPresenter(LoginContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    /**
     * 初始化数据
     */
    @Override
    public List<String> LoaderInitData() {
        return java.util.Arrays.asList("emial", "password");
    }

    /**
     * @param email
     * @param password
     * @return
     */
    @Override
    public boolean checkUserInfo(String email, String password, final LoginContract.CallBack callBack) {
        /**
         * 开启一个线程模拟网络请求
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    callBack.LoginSuccess();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        return true;

    }

}
