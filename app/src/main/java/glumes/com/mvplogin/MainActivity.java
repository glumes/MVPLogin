package glumes.com.mvplogin;

import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import glumes.com.mvplogin.presenter.LoginPresenter;
import glumes.com.mvplogin.view.LoginFragment;

/**
 * Activity 的作用是创建 View 和 Presenter
 * 以及将 View 传递给 Presenter 进行绑定
 */
public class MainActivity extends AppCompatActivity {

    private LoginFragment mFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFragment = LoginFragment.newInstance();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content,mFragment);
        transaction.commit();

        /**
         * 声明 Presenter 的同时,将 View 和 Presenter 绑定起来
         * 因为 Activity 是一个总控制类,所以 New 方法创建了一个 Presenter 却不需要赋值给 Activity 中的变量
         */
        new LoginPresenter(mFragment);
    }
}
