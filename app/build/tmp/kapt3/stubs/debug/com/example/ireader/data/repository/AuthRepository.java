package com.example.ireader.data.repository;

/**
 * 用户认证仓库，负责处理登录、注册和用户状态管理
 */
@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\b\u0007\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\b\u0010\u0013\u001a\u0004\u0018\u00010\u0014J\b\u0010\u0015\u001a\u00020\u0016H\u0002J$\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u000b0\u00182\u0006\u0010\u0019\u001a\u00020\u00142\u0006\u0010\u001a\u001a\u00020\u0014H\u0086@\u00a2\u0006\u0002\u0010\u001bJ\u0006\u0010\u001c\u001a\u00020\u0016J,\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u000b0\u00182\u0006\u0010\u0019\u001a\u00020\u00142\u0006\u0010\u001a\u001a\u00020\u00142\u0006\u0010\u001e\u001a\u00020\u0014H\u0086@\u00a2\u0006\u0002\u0010\u001fJ\u0010\u0010 \u001a\u00020\u00162\u0006\u0010\u0011\u001a\u00020\u000bH\u0002R\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\n\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u000b0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\f\u001a\b\u0012\u0004\u0012\u00020\t0\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\u000eR\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0019\u0010\u0011\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u000b0\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u000e\u00a8\u0006!"}, d2 = {"Lcom/example/ireader/data/repository/AuthRepository;", "", "authApi", "Lcom/example/ireader/data/network/AuthApi;", "context", "Landroid/content/Context;", "(Lcom/example/ireader/data/network/AuthApi;Landroid/content/Context;)V", "_isAuthenticated", "Landroidx/lifecycle/MutableLiveData;", "", "_user", "Lcom/example/ireader/data/model/User;", "isAuthenticated", "Landroidx/lifecycle/LiveData;", "()Landroidx/lifecycle/LiveData;", "sharedPreferences", "Landroid/content/SharedPreferences;", "user", "getUser", "getAuthHeader", "", "loadUserFromPrefs", "", "login", "Lcom/example/ireader/data/model/Resource;", "email", "password", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "logout", "register", "name", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "saveUser", "app_debug"})
public final class AuthRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.example.ireader.data.network.AuthApi authApi = null;
    @org.jetbrains.annotations.NotNull()
    private final android.content.SharedPreferences sharedPreferences = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<com.example.ireader.data.model.User> _user = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<com.example.ireader.data.model.User> user = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<java.lang.Boolean> _isAuthenticated = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<java.lang.Boolean> isAuthenticated = null;
    
    @javax.inject.Inject()
    public AuthRepository(@org.jetbrains.annotations.NotNull()
    com.example.ireader.data.network.AuthApi authApi, @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<com.example.ireader.data.model.User> getUser() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.lang.Boolean> isAuthenticated() {
        return null;
    }
    
    /**
     * 用户登录
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object login(@org.jetbrains.annotations.NotNull()
    java.lang.String email, @org.jetbrains.annotations.NotNull()
    java.lang.String password, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.ireader.data.model.Resource<com.example.ireader.data.model.User>> $completion) {
        return null;
    }
    
    /**
     * 用户注册
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object register(@org.jetbrains.annotations.NotNull()
    java.lang.String email, @org.jetbrains.annotations.NotNull()
    java.lang.String password, @org.jetbrains.annotations.NotNull()
    java.lang.String name, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.ireader.data.model.Resource<com.example.ireader.data.model.User>> $completion) {
        return null;
    }
    
    /**
     * 用户登出
     */
    public final void logout() {
    }
    
    /**
     * 从 SharedPreferences 加载用户信息
     */
    private final void loadUserFromPrefs() {
    }
    
    /**
     * 保存用户信息到 SharedPreferences
     */
    private final void saveUser(com.example.ireader.data.model.User user) {
    }
    
    /**
     * 获取认证头 - 由于 User 没有 token，这里返回 null
     * 实际项目中可能需要单独存储 token
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getAuthHeader() {
        return null;
    }
}