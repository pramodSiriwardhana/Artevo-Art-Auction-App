package lk.nibm.hdse242ft.artauctionapp.Reusable_Components;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u00008\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u008b\u0001\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032K\u0010\u0004\u001aG\u0012\u0013\u0012\u00110\u0006\u00a2\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\t\u0012\u0013\u0012\u00110\n\u00a2\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\u000b\u0012\u0013\u0012\u00110\n\u00a2\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\f\u0012\u0004\u0012\u00020\u00010\u00052,\u0010\r\u001a(\u0012\u0019\u0012\u0017\u0012\u0004\u0012\u00020\u00010\u000f\u00a2\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\u0010\u0012\u0004\u0012\u00020\u00010\u000e\u00a2\u0006\u0002\b\u0011H\u0007\u001a]\u0010\u0012\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032K\u0010\u0004\u001aG\u0012\u0013\u0012\u00110\u0006\u00a2\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\t\u0012\u0013\u0012\u00110\n\u00a2\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\u000b\u0012\u0013\u0012\u00110\n\u00a2\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\f\u0012\u0004\u0012\u00020\u00010\u0005H\u0007\u00a8\u0006\u0013"}, d2 = {"LocationPermissionButton", "", "context", "Landroid/content/Context;", "onLocationReceived", "Lkotlin/Function3;", "", "Lkotlin/ParameterName;", "name", "address", "", "latitude", "longitude", "content", "Lkotlin/Function1;", "Lkotlin/Function0;", "onClick", "Landroidx/compose/runtime/Composable;", "getCurrentLocation", "app_debug"})
public final class LocationHelperKt {
    
    @android.annotation.SuppressLint(value = {"MissingPermission"})
    public static final void getCurrentLocation(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function3<? super java.lang.String, ? super java.lang.Double, ? super java.lang.Double, kotlin.Unit> onLocationReceived) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void LocationPermissionButton(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function3<? super java.lang.String, ? super java.lang.Double, ? super java.lang.Double, kotlin.Unit> onLocationReceived, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super kotlin.jvm.functions.Function0<kotlin.Unit>, kotlin.Unit> content) {
    }
}