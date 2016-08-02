package glumes.com.mvplogin.model;

import java.util.List;

import glumes.com.mvplogin.LoginContract;

/**
 * Created by zhaoying on 16/8/1.
 */
public class LoginModel implements LoginContract.Model {

    public LoginModel() {
    }


    /**
     * 初始化数据,提供默认的邮箱和密码
     */
    @Override
    public List<String> LoaderInitData() {
        return java.util.Arrays.asList("emial", "password");
    }

    /**
     * 模拟检查用户的邮箱和密码是否正确
     * @param email
     * @param password
     * @return
     */
    @Override
    public void checkUserInfo(String email, String password, final LoginContract.CallBack callBack) {
        /**
         * 在 Model 层中开启一个线程模拟网络请求
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    /**
                     * Model 层的数据处理完了之后,回调接口在 Presenter 层实现.
                     * 因为这是在子线程中,所以 Presenter 中必须要通过 Handler 在主线程在更新 UI
                     */
                    callBack.LoginSuccess();

//                    callBack.LoginFailed();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }

}
