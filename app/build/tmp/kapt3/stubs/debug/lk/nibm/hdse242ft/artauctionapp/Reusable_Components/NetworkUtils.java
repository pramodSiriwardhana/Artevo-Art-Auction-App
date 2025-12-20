package lk.nibm.hdse242ft.artauctionapp.Reusable_Components;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\t\u001a\u00020\u00052\u0006\u0010\n\u001a\u00020\u000bJ\u000e\u0010\f\u001a\u00020\r2\u0006\u0010\n\u001a\u00020\u000bR\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00050\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\b\u00a8\u0006\u000e"}, d2 = {"Llk/nibm/hdse242ft/artauctionapp/Reusable_Components/NetworkUtils;", "", "()V", "_isConnected", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "isConnected", "Lkotlinx/coroutines/flow/StateFlow;", "()Lkotlinx/coroutines/flow/StateFlow;", "hasInternetConnection", "context", "Landroid/content/Context;", "updateNetworkStatus", "", "app_debug"})
public final class NetworkUtils {
    @org.jetbrains.annotations.NotNull()
    private static final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _isConnected = null;
    @org.jetbrains.annotations.NotNull()
    private static final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isConnected = null;
    @org.jetbrains.annotations.NotNull()
    public static final lk.nibm.hdse242ft.artauctionapp.Reusable_Components.NetworkUtils INSTANCE = null;
    
    private NetworkUtils() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isConnected() {
        return null;
    }
    
    public final void updateNetworkStatus(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
    }
    
    public final boolean hasInternetConnection(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return false;
    }
}