package com.calsync.app.domain.util;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u0015\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\f\n\u0002\u0010\t\n\u0002\b\u0004\b\u00c6\u0002\u0018\u00002\u00020\u0001:\u0001 B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J \u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00102\u0006\u0010\u0012\u001a\u00020\u0010H\u0002J\u0018\u0010\u0013\u001a\u00020\u00102\u0006\u0010\u0014\u001a\u00020\u00102\u0006\u0010\u0015\u001a\u00020\u0010H\u0002J\u0010\u0010\u0016\u001a\u00020\u00102\u0006\u0010\u0014\u001a\u00020\u0010H\u0002J\u000e\u0010\u0017\u001a\u00020\u00052\u0006\u0010\u000f\u001a\u00020\u0010J\u001e\u0010\u0018\u001a\u00020\u00052\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00102\u0006\u0010\u0012\u001a\u00020\u0010J\u001e\u0010\u0019\u001a\u00020\u00052\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00102\u0006\u0010\u0012\u001a\u00020\u0010J\u001e\u0010\u001a\u001a\u00020\u00052\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00102\u0006\u0010\u0012\u001a\u00020\u0010J\u0010\u0010\u001b\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u001c\u001a\u00020\u001dJ\u000e\u0010\u001e\u001a\u00020\u00052\u0006\u0010\u000f\u001a\u00020\u0010J\u0010\u0010\u001f\u001a\u00020\u00102\u0006\u0010\u0014\u001a\u00020\u0010H\u0002R\u0016\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u0006R\u0016\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u0006R\u0016\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u0006R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u0006R\u0016\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u0006\u00a8\u0006!"}, d2 = {"Lcom/calsync/app/domain/util/LunarCalendar;", "", "()V", "DAY_NAMES", "", "", "[Ljava/lang/String;", "EARTHLY_BRANCHES", "HEAVENLY_STEMS", "LUNAR_DATA", "", "MONTH_NAMES", "ZODIAC", "convertToLunar", "Lcom/calsync/app/domain/util/LunarCalendar$LunarInfo;", "year", "", "month", "dayOfMonth", "daysInLunarMonth", "y", "m", "daysInLunarYear", "getGanZhi", "getLunarDateString", "getLunarDayName", "getLunarMonthName", "getSolarTerm", "timestamp", "", "getZodiac", "leapMonth", "LunarInfo", "app_debug"})
public final class LunarCalendar {
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String[] MONTH_NAMES = {"\u6b63\u6708", "\u4e8c\u6708", "\u4e09\u6708", "\u56db\u6708", "\u4e94\u6708", "\u516d\u6708", "\u4e03\u6708", "\u516b\u6708", "\u4e5d\u6708", "\u5341\u6708", "\u5341\u4e00\u6708", "\u5341\u4e8c\u6708"};
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String[] DAY_NAMES = {"\u521d\u4e00", "\u521d\u4e8c", "\u521d\u4e09", "\u521d\u56db", "\u521d\u4e94", "\u521d\u516d", "\u521d\u4e03", "\u521d\u516b", "\u521d\u4e5d", "\u521d\u5341", "\u5341\u4e00", "\u5341\u4e8c", "\u5341\u4e09", "\u5341\u56db", "\u5341\u4e94", "\u5341\u516d", "\u5341\u4e03", "\u5341\u516b", "\u5341\u4e5d", "\u4e8c\u5341", "\u5eff\u4e00", "\u5eff\u4e8c", "\u5eff\u4e09", "\u5eff\u56db", "\u5eff\u4e94", "\u5eff\u516d", "\u5eff\u4e03", "\u5eff\u516b", "\u5eff\u4e5d", "\u4e09\u5341"};
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String[] HEAVENLY_STEMS = {"\u7532", "\u4e59", "\u4e19", "\u4e01", "\u620a", "\u5df1", "\u5e9a", "\u8f9b", "\u58ec", "\u7678"};
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String[] EARTHLY_BRANCHES = {"\u5b50", "\u4e11", "\u5bc5", "\u536f", "\u8fb0", "\u5df3", "\u5348", "\u672a", "\u7533", "\u9149", "\u620c", "\u4ea5"};
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String[] ZODIAC = {"\u9f20", "\u725b", "\u864e", "\u5154", "\u9f99", "\u86c7", "\u9a6c", "\u7f8a", "\u7334", "\u9e21", "\u72d7", "\u732a"};
    @org.jetbrains.annotations.NotNull()
    private static final int[] LUNAR_DATA = {19416, 19168, 42352, 21717, 53856, 55632, 91476, 22176, 39632, 21970, 19168, 42422, 42192, 53840, 119381, 46400, 54944, 44450, 38320, 84343, 18800, 42160, 46261, 27216, 27968, 109396, 11104, 38256, 21234, 18800, 25958, 54432, 59984, 28309, 23248, 11104, 100067, 37600, 116951, 51536, 54432, 120998, 46416, 22176, 107956, 9680, 37584, 53938, 43344, 46423, 27808, 46416, 86869, 19872, 42416, 83315, 21168, 43432, 59728, 27296, 44710, 43856, 19296, 43748, 42352, 21088, 62051, 55632, 23383, 22176, 38608, 19925, 19152, 42192, 54484, 53840, 54616, 46400, 46752, 103846, 38320, 18864, 43380, 42160, 45690, 27216, 27968, 44870, 43872, 38256, 19189, 18800, 25776, 29859, 59984, 27480, 23232, 43872, 38613, 37600, 51552, 55636, 54432, 55888, 30034, 22176, 43959, 9680, 37584, 51893, 43344, 46240, 47782, 44368, 21977, 19360, 42416, 86390, 21168, 43312, 31060, 27296, 44368, 23378, 19296, 42726, 42208, 53856, 60005, 54576, 23200, 30371, 38608, 19195, 19152, 42192, 118966, 53840, 54560, 56645, 46496, 22224, 21938, 18864, 42359, 42160, 43600, 111189, 27936, 44448, 84835, 37744, 18936, 18800, 25776, 92326, 59984, 27424, 108228, 43744, 37600, 53987, 51552, 54615, 54432, 55888, 23893, 22176, 42704, 21972, 21200, 43448, 43344, 46240, 46758, 44368, 21920, 43940, 42416, 21168, 45683, 26928, 29495, 27296, 44368, 84821, 19296, 42352, 21732, 53600, 59752, 54560, 55968, 92838, 22224, 19168, 43476, 41680, 53584, 62034, 54560};
    @org.jetbrains.annotations.NotNull()
    public static final com.calsync.app.domain.util.LunarCalendar INSTANCE = null;
    
    private LunarCalendar() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getLunarDateString(int year, int month, int dayOfMonth) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getLunarMonthName(int year, int month, int dayOfMonth) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getLunarDayName(int year, int month, int dayOfMonth) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getZodiac(int year) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getGanZhi(int year) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getSolarTerm(long timestamp) {
        return null;
    }
    
    private final com.calsync.app.domain.util.LunarCalendar.LunarInfo convertToLunar(int year, int month, int dayOfMonth) {
        return null;
    }
    
    private final int daysInLunarYear(int y) {
        return 0;
    }
    
    private final int daysInLunarMonth(int y, int m) {
        return 0;
    }
    
    private final int leapMonth(int y) {
        return 0;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0016\b\u0082\b\u0018\u00002\u00020\u0001B5\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\t\u00a2\u0006\u0002\u0010\u000bJ\t\u0010\u0014\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0015\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0016\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0017\u001a\u00020\u0007H\u00c6\u0003J\t\u0010\u0018\u001a\u00020\tH\u00c6\u0003J\t\u0010\u0019\u001a\u00020\tH\u00c6\u0003JE\u0010\u001a\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\tH\u00c6\u0001J\u0013\u0010\u001b\u001a\u00020\u00072\b\u0010\u001c\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u001d\u001a\u00020\u0003H\u00d6\u0001J\t\u0010\u001e\u001a\u00020\tH\u00d6\u0001R\u0011\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\n\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0010R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\rR\u0011\u0010\b\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u000fR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\r\u00a8\u0006\u001f"}, d2 = {"Lcom/calsync/app/domain/util/LunarCalendar$LunarInfo;", "", "year", "", "month", "day", "isLeap", "", "monthName", "", "dayName", "(IIIZLjava/lang/String;Ljava/lang/String;)V", "getDay", "()I", "getDayName", "()Ljava/lang/String;", "()Z", "getMonth", "getMonthName", "getYear", "component1", "component2", "component3", "component4", "component5", "component6", "copy", "equals", "other", "hashCode", "toString", "app_debug"})
    static final class LunarInfo {
        private final int year = 0;
        private final int month = 0;
        private final int day = 0;
        private final boolean isLeap = false;
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String monthName = null;
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String dayName = null;
        
        public LunarInfo(int year, int month, int day, boolean isLeap, @org.jetbrains.annotations.NotNull()
        java.lang.String monthName, @org.jetbrains.annotations.NotNull()
        java.lang.String dayName) {
            super();
        }
        
        public final int getYear() {
            return 0;
        }
        
        public final int getMonth() {
            return 0;
        }
        
        public final int getDay() {
            return 0;
        }
        
        public final boolean isLeap() {
            return false;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getMonthName() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getDayName() {
            return null;
        }
        
        public final int component1() {
            return 0;
        }
        
        public final int component2() {
            return 0;
        }
        
        public final int component3() {
            return 0;
        }
        
        public final boolean component4() {
            return false;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component5() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component6() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.calsync.app.domain.util.LunarCalendar.LunarInfo copy(int year, int month, int day, boolean isLeap, @org.jetbrains.annotations.NotNull()
        java.lang.String monthName, @org.jetbrains.annotations.NotNull()
        java.lang.String dayName) {
            return null;
        }
        
        @java.lang.Override()
        public boolean equals(@org.jetbrains.annotations.Nullable()
        java.lang.Object other) {
            return false;
        }
        
        @java.lang.Override()
        public int hashCode() {
            return 0;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public java.lang.String toString() {
            return null;
        }
    }
}