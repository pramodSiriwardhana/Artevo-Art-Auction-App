package lk.nibm.hdse242ft.artauctionapp.Screens;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000`\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0002\u001aE\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0015\b\u0002\u0010\u0004\u001a\u000f\u0012\u0004\u0012\u00020\u0001\u0018\u00010\u0005\u00a2\u0006\u0002\b\u00062\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\nH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u000b\u0010\f\u001a\u001a\u0010\r\u001a\u00020\u00012\u0006\u0010\u000e\u001a\u00020\u000f2\b\b\u0002\u0010\u0010\u001a\u00020\u0011H\u0007\u001a\u0010\u0010\u0012\u001a\u00020\u00012\u0006\u0010\u0013\u001a\u00020\u0014H\u0007\u001a\u0010\u0010\u0015\u001a\u00020\u00012\u0006\u0010\u0016\u001a\u00020\u0017H\u0007\u001a@\u0010\u0018\u001a\u00020\u00012\u0006\u0010\u0019\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\b2\u0006\u0010\u001c\u001a\u00020\b2\f\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u00010\u0005H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u001e\u0010\u001f\u001a \u0010 \u001a\u00020\u00012\u0006\u0010\u0019\u001a\u00020\u00032\u0006\u0010!\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u001aH\u0007\u001aL\u0010\"\u001a\u00020\u00012\u0006\u0010#\u001a\u00020$2\f\u0010%\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\u0018\u0010&\u001a\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00170(\u0012\u0004\u0012\u00020\u00010\'2\u0012\u0010)\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00010\'H\u0002\u0082\u0002\u0007\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006*"}, d2 = {"AnimatedStatusMessage_ArtistDashBoard", "", "message", "", "icon", "Lkotlin/Function0;", "Landroidx/compose/runtime/Composable;", "backgroundColor", "Landroidx/compose/ui/graphics/Color;", "position", "Landroidx/compose/ui/Alignment;", "AnimatedStatusMessage_ArtistDashBoard-9LQNqLg", "(Ljava/lang/String;Lkotlin/jvm/functions/Function0;JLandroidx/compose/ui/Alignment;)V", "ArtistDashboardScreen", "navController", "Landroidx/navigation/NavController;", "modifier", "Landroidx/compose/ui/Modifier;", "DashboardStatsCard", "stats", "Llk/nibm/hdse242ft/artauctionapp/Screens/ArtistStats;", "LiveAuctionCardFromArtwork", "artwork", "Llk/nibm/hdse242ft/artauctionapp/Api/Artwork;", "QuickActionButton", "label", "Landroidx/compose/ui/graphics/vector/ImageVector;", "bgColor", "iconColor", "onClick", "QuickActionButton-OoHUuok", "(Ljava/lang/String;Landroidx/compose/ui/graphics/vector/ImageVector;JJLkotlin/jvm/functions/Function0;)V", "StatRow", "value", "fetchLiveArtworks", "userId", "", "onStart", "onComplete", "Lkotlin/Function1;", "", "onError", "app_debug"})
public final class ArtistDashboardScreenKt {
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void ArtistDashboardScreen(@org.jetbrains.annotations.NotNull()
    androidx.navigation.NavController navController, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    private static final void fetchLiveArtworks(int userId, kotlin.jvm.functions.Function0<kotlin.Unit> onStart, kotlin.jvm.functions.Function1<? super java.util.List<lk.nibm.hdse242ft.artauctionapp.Api.Artwork>, kotlin.Unit> onComplete, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onError) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void LiveAuctionCardFromArtwork(@org.jetbrains.annotations.NotNull()
    lk.nibm.hdse242ft.artauctionapp.Api.Artwork artwork) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void DashboardStatsCard(@org.jetbrains.annotations.NotNull()
    lk.nibm.hdse242ft.artauctionapp.Screens.ArtistStats stats) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void StatRow(@org.jetbrains.annotations.NotNull()
    java.lang.String label, @org.jetbrains.annotations.NotNull()
    java.lang.String value, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.graphics.vector.ImageVector icon) {
    }
}