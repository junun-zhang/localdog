package com.calsync.app.ui;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b6\u0018\u00002\u00020\u0001:\u0005\u000f\u0010\u0011\u0012\u0013B\u001f\b\u0004\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000e\u0082\u0001\u0005\u0014\u0015\u0016\u0017\u0018\u00a8\u0006\u0019"}, d2 = {"Lcom/calsync/app/ui/BottomNavItem;", "", "route", "", "icon", "Landroidx/compose/ui/graphics/vector/ImageVector;", "labelRes", "", "(Ljava/lang/String;Landroidx/compose/ui/graphics/vector/ImageVector;I)V", "getIcon", "()Landroidx/compose/ui/graphics/vector/ImageVector;", "getLabelRes", "()I", "getRoute", "()Ljava/lang/String;", "Day", "Month", "Schedule", "Tasks", "Week", "Lcom/calsync/app/ui/BottomNavItem$Day;", "Lcom/calsync/app/ui/BottomNavItem$Month;", "Lcom/calsync/app/ui/BottomNavItem$Schedule;", "Lcom/calsync/app/ui/BottomNavItem$Tasks;", "Lcom/calsync/app/ui/BottomNavItem$Week;", "app_release"})
public abstract class BottomNavItem {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String route = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.compose.ui.graphics.vector.ImageVector icon = null;
    private final int labelRes = 0;
    
    private BottomNavItem(java.lang.String route, androidx.compose.ui.graphics.vector.ImageVector icon, int labelRes) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getRoute() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.compose.ui.graphics.vector.ImageVector getIcon() {
        return null;
    }
    
    public final int getLabelRes() {
        return 0;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/calsync/app/ui/BottomNavItem$Day;", "Lcom/calsync/app/ui/BottomNavItem;", "()V", "app_release"})
    public static final class Day extends com.calsync.app.ui.BottomNavItem {
        @org.jetbrains.annotations.NotNull()
        public static final com.calsync.app.ui.BottomNavItem.Day INSTANCE = null;
        
        private Day() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/calsync/app/ui/BottomNavItem$Month;", "Lcom/calsync/app/ui/BottomNavItem;", "()V", "app_release"})
    public static final class Month extends com.calsync.app.ui.BottomNavItem {
        @org.jetbrains.annotations.NotNull()
        public static final com.calsync.app.ui.BottomNavItem.Month INSTANCE = null;
        
        private Month() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/calsync/app/ui/BottomNavItem$Schedule;", "Lcom/calsync/app/ui/BottomNavItem;", "()V", "app_release"})
    public static final class Schedule extends com.calsync.app.ui.BottomNavItem {
        @org.jetbrains.annotations.NotNull()
        public static final com.calsync.app.ui.BottomNavItem.Schedule INSTANCE = null;
        
        private Schedule() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/calsync/app/ui/BottomNavItem$Tasks;", "Lcom/calsync/app/ui/BottomNavItem;", "()V", "app_release"})
    public static final class Tasks extends com.calsync.app.ui.BottomNavItem {
        @org.jetbrains.annotations.NotNull()
        public static final com.calsync.app.ui.BottomNavItem.Tasks INSTANCE = null;
        
        private Tasks() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/calsync/app/ui/BottomNavItem$Week;", "Lcom/calsync/app/ui/BottomNavItem;", "()V", "app_release"})
    public static final class Week extends com.calsync.app.ui.BottomNavItem {
        @org.jetbrains.annotations.NotNull()
        public static final com.calsync.app.ui.BottomNavItem.Week INSTANCE = null;
        
        private Week() {
        }
    }
}