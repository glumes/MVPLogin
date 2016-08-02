package glumes.com.mvplogin.view;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import glumes.com.mvplogin.LoginContract;
import glumes.com.mvplogin.R;
import glumes.com.mvplogin.WelcomeMVPActivity;

/**
 * Created by zhaoying on 16/8/1.
 */

public class LoginFragment extends Fragment implements LoginContract.View, View.OnClickListener {

    private AutoCompleteTextView mTextView;
    private EditText mEditText;
    private Button mButton;
    private LoginContract.Presenter mPresenter;
    private ProgressDialog mProgressDialog;

    /**
     * 给 View 来传入 Presenter 来绑定 presenter
     *
     * @param presenter
     */

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    public LoginFragment() {
    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_login, container, false);
        mButton = (Button) root.findViewById(R.id.email_sign_in_button);
        mEditText = (EditText) root.findViewById(R.id.password);
        mTextView = (AutoCompleteTextView) root.findViewById(R.id.email);
        mButton.setOnClickListener(this);
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void showLoading() {
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setTitle("Sign In");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage("Please Waiting...");
        mProgressDialog.setCancelable(true);
        mProgressDialog.setButton("Cancle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mPresenter.cancelLoadingDialog();
            }
        });
        mProgressDialog.show();
    }

    @Override
    public void hideLoading() {
        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void setEmail(String email) {
        mTextView.setText(email);
    }

    @Override
    public void setPassword(String password) {
        mEditText.setText(password);
    }

    @Override
    public void onResume() {
        super.onResume();
        /**
         * Presenter start 方法做一些初始化的工作,比如初始化数据等.
         */
        mPresenter.start();
    }


    @Override
    public void onClick(View v) {
        mPresenter.showLoadingDialog();
        mPresenter.checkUserInfo(mTextView.getText().toString(), mEditText.getText().toString());
    }

    public void toActivity() {
        startActivity(new Intent(getActivity(), WelcomeMVPActivity.class));
    }

    /**
     * 防止 P 层中持有 V 层导致内存泄漏
     * 通过 Fragment 中的 isAdded() 方法判断当前 Fragment 是否已经添加到 Activity 当中去
     * @return
     */
    @Override
    public boolean isActive() {
        return isAdded();
    }

}
