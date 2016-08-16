# MVPLogin

在安卓开发中使用 MVP 模式已经非常普遍了，网上关于 MVP 的讲解也相当多了，不过看得再多还是自己写一遍比较熟练。

MVP 分别代表了：M（Model）、V（View）、P（Presenter）的缩写，代表了三个不同的模块。

*    Model：负责处理数据的加载或者存储，比如从网络中或者本地数据库中获取数据。
*    View：负责界面数据的展示，以及与用户行为的交互。
*    Presenter：是模型和视图的桥梁，将模型和视图分离开来，主要负责处理事务逻辑。


![mvp-and-mvc](http://7xqe3m.com1.z0.glb.clouddn.com/blog-mvp-and-mvc.png)

如果所示，MVP 与 MVC 的不同之处在于，MVP 中的 View 层和 Model 层之间不进行交互，是通过中间的 Presenter 中来进行交互的。这样的好处就是能够解耦，降低 View 层和 Model 层之间的耦合度。

谷歌官方也给出了一个 MVP 模式的示例：https://github.com/googlesamples/android-architecture 

## MVP 模式实现登陆功能

用 MVP 模式简单实现了一个登陆功能，项目的包结构如下，根据模块层次来分层的。

![mvp-project-dir](http://7xqe3m.com1.z0.glb.clouddn.com/blog-mvp-project-dir.png)

### 接口定义

在 LoginContract 接口中定义了 M 层、V 层、P 层的接口，以及 Model 层数据传递到 Presenter 时的回调接口，将所有的接口放到一个总的接口中，这样不会那么的散乱，便于管理。

而且， M 层、V 层、P 层的实现类都将实现这些接口，这样的好处就是便于替换。

因为 Java 中的向上转型，当我们如下声明一个类时，LoginPresenter 类就向上转型成了 LoginContract.Presenter 类，这时，LoginPresenter 只能调用向上转型的类，也就是接口类中的定义的方法，不能再调用自己实现的其他方法，这样一样，就方便我们在需要的时候对  M 层、V 层、P 层 的类进行替换，可以替换内部的实现方法，而调用的方法名不变。

```
private LoginContract.Presenter mPresenter = new LoginPresenter()
```

```
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
```

### V 层实现

将 Fragment 作为 MVP 中的 V 层，而 Activity 则是作为一个总的控制类，并没有做具体的业务逻辑操作，这里是仿照着谷歌官方的例子来编写的，当然也可以将 Activity 作为 V 层来实现。

在 V 层中需要绑定 P 层的 Presenter ，而 V 层则处理数据的展示和与用户的 UI 交互。

而 P 层则是负责处理具体的逻辑。

所以，V 层是将用户的事件都交给了 P 层去处理，而 P 层通过调用 M 层处理的数据，再将它展示给 V 层去。

```
/**
 * 给 View 来传入 Presenter 来绑定 presenter
 *
 * @param presenter
 */

@Override
public void setPresenter(LoginContract.Presenter presenter) {
    this.mPresenter = presenter;
}
```

### P 层实现

P 层是 MVP 中最重要的负责具体逻辑处理的一层，它持有 V 层和 M 层的引用，毕竟它需要调用 V 层和 M 层的方法。

由于 P 层持有了 V 层的引用，当 presenter 有后台异步的耗时操作时，如果这时退出了 V 层，而后台异步线程的操作并不会立即停止，这时候就会引发内存泄漏了。

```
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

    /**
     * 得到主线程,也就是 UI 线程的消息循环 Looper
     */
    UiHandler = new Handler(Looper.getMainLooper());
}
```

### M 层实现 

M 层也实现了 LoginContract 中的 Model 接口，M 层主要负责从网络或者本地数据库中加载数据，因为安卓的主线程并不是线程安全的，而且主线程中不允许进行网络和耗时操作，所以 M 层中的操作都是在异步的子线程中进行的，这样就需要向主线程的消息循环中发送消息，获得主线程中的消息循环（Looper.getMainLooper()），从而更新主线程的 UI 。

```
public class LoginModel implements LoginContract.Model {

    public LoginModel() {
    }
}
```

具体的代码实现都可以在 Github 上找到，项目地址是：https://github.com/glumes/MVPLogin 

### 注意事项 

#### 防止内存泄漏

在这里，我用的是 Fragment 作为 V 层，为了防止在 P 层中的异步耗时操作执行的时候，V 层已经退出了，所以在仿照谷歌官方的例子，在 V 层接口中定义了 ```isActive()``` 方法：
```
/**
 * 防止 P 层中持有 V 层导致内存泄漏
 * 通过 Fragment 中的 isAdded() 方法判断当前 Fragment 是否已经添加到 Activity 当中去
 * @return
 */
@Override
public boolean isActive() {
    return isAdded();
}
```

如果是采用 Activity 作为 V 层，则可以在 P 层添加一个销毁 V 层的方法，P 层增加了类似的生命周期的方法，用来在退出 Activity 的时候取消持有的 V 层引用。
```
//presenter中添加mvpView 置为null的方法public void onDestroy(){    
    mvpView = null;
}

//退出时销毁持有Activity@Overrideprotected void onDestroy() {    
    mvpPresenter.onDestroy();    
    super.onDestroy();
}
```
#### 声明 Presenter 并绑定 V 层 

在 Activity 中有这样一段代码，通过 new 创建了一个 Presenter，但却没有赋值给任何对象。

在谷歌的官方例子中也是这样创建的，这是因为将 Fragment 作为 V 层，并不需要在 Activity 中处理什么逻辑操作。

```
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
```

通过查看代码可以得知，在 Presenter 的构造函数中，通过 ```mView.setPresenter(this);``` 将 Presenter 自身又传入了 V 层中，这样 V 层通过 ```setPresenter()``` 方法持有了 P 层的引用，这样一来，P 层和 V 层就互相持有了各自的引用 。
```
public LoginPresenter(LoginContract.View view) {
    mView = view;
    /**
     *  Presenter 绑定 View
     */
    mView.setPresenter(this);
```




## 参考 

1、http://www.cnblogs.com/liuling/archive/2015/12/23/mvp-pattern-android.html
2、http://blog.csdn.net/dantestones/article/details/51445208
3、http://blog.csdn.net/lmj623565791/article/details/46596109