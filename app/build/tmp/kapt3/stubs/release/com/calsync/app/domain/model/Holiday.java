package com.calsync.app.domain.model;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0015\n\u0002\u0010\b\n\u0002\b\u0003\b\u0086\b\u0018\u00002\u00020\u0001:\u0001\"B?\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\t\u001a\u00020\n\u0012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\u0002\u0010\fJ\t\u0010\u0016\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0017\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u0018\u001a\u00020\u0007H\u00c6\u0003J\u000b\u0010\u0019\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\t\u0010\u001a\u001a\u00020\nH\u00c6\u0003J\u000b\u0010\u001b\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003JI\u0010\u001c\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u00052\b\b\u0002\u0010\t\u001a\u00020\n2\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u0005H\u00c6\u0001J\u0013\u0010\u001d\u001a\u00020\n2\b\u0010\u001e\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u001f\u001a\u00020 H\u00d6\u0001J\t\u0010!\u001a\u00020\u0005H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\t\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\u000fR\u0013\u0010\b\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0011R\u0013\u0010\u000b\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0011R\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015\u00a8\u0006#"}, d2 = {"Lcom/calsync/app/domain/model/Holiday;", "", "date", "", "name", "", "type", "Lcom/calsync/app/domain/model/Holiday$HolidayType;", "lunarDate", "isAdjustment", "", "solarTerm", "(JLjava/lang/String;Lcom/calsync/app/domain/model/Holiday$HolidayType;Ljava/lang/String;ZLjava/lang/String;)V", "getDate", "()J", "()Z", "getLunarDate", "()Ljava/lang/String;", "getName", "getSolarTerm", "getType", "()Lcom/calsync/app/domain/model/Holiday$HolidayType;", "component1", "component2", "component3", "component4", "component5", "component6", "copy", "equals", "other", "hashCode", "", "toString", "HolidayType", "app_release"})
public final class Holiday {
    private final long date = 0L;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String name = null;
    @org.jetbrains.annotations.NotNull()
    private final com.calsync.app.domain.model.Holiday.HolidayType type = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String lunarDate = null;
    private final boolean isAdjustment = false;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String solarTerm = null;
    
    public Holiday(long date, @org.jetbrains.annotations.NotNull()
    java.lang.String name, @org.jetbrains.annotations.NotNull()
    com.calsync.app.domain.model.Holiday.HolidayType type, @org.jetbrains.annotations.Nullable()
    java.lang.String lunarDate, boolean isAdjustment, @org.jetbrains.annotations.Nullable()
    java.lang.String solarTerm) {
        super();
    }
    
    public final long getDate() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getName() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.calsync.app.domain.model.Holiday.HolidayType getType() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getLunarDate() {
        return null;
    }
    
    public final boolean isAdjustment() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getSolarTerm() {
        return null;
    }
    
    public final long component1() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.calsync.app.domain.model.Holiday.HolidayType component3() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component4() {
        return null;
    }
    
    public final boolean component5() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component6() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.calsync.app.domain.model.Holiday copy(long date, @org.jetbrains.annotations.NotNull()
    java.lang.String name, @org.jetbrains.annotations.NotNull()
    com.calsync.app.domain.model.Holiday.HolidayType type, @org.jetbrains.annotations.Nullable()
    java.lang.String lunarDate, boolean isAdjustment, @org.jetbrains.annotations.Nullable()
    java.lang.String solarTerm) {
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0007\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007\u00a8\u0006\b"}, d2 = {"Lcom/calsync/app/domain/model/Holiday$HolidayType;", "", "(Ljava/lang/String;I)V", "HOLIDAY", "WORKDAY", "FESTIVAL", "SOLAR_TERM", "SCHOOL_HOLIDAY", "app_release"})
    public static enum HolidayType {
        /*public static final*/ HOLIDAY /* = new HOLIDAY() */,
        /*public static final*/ WORKDAY /* = new WORKDAY() */,
        /*public static final*/ FESTIVAL /* = new FESTIVAL() */,
        /*public static final*/ SOLAR_TERM /* = new SOLAR_TERM() */,
        /*public static final*/ SCHOOL_HOLIDAY /* = new SCHOOL_HOLIDAY() */;
        
        HolidayType() {
        }
        
        @org.jetbrains.annotations.NotNull()
        public static kotlin.enums.EnumEntries<com.calsync.app.domain.model.Holiday.HolidayType> getEntries() {
            return null;
        }
    }
}