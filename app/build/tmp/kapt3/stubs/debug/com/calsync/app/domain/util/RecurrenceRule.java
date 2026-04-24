package com.calsync.app.domain.util;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0002\b\u001b\n\u0002\u0010\u000b\n\u0002\b\u0007\b\u0086\b\u0018\u0000 ,2\u00020\u0001:\u0002,-Be\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u0012\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u0005\u0012\u0010\b\u0002\u0010\t\u001a\n\u0012\u0004\u0012\u00020\u000b\u0018\u00010\n\u0012\u0010\b\u0002\u0010\f\u001a\n\u0012\u0004\u0012\u00020\u0005\u0018\u00010\n\u0012\u0010\b\u0002\u0010\r\u001a\n\u0012\u0004\u0012\u00020\u0005\u0018\u00010\n\u00a2\u0006\u0002\u0010\u000eJ\t\u0010\u001d\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001e\u001a\u00020\u0005H\u00c6\u0003J\u0010\u0010\u001f\u001a\u0004\u0018\u00010\u0007H\u00c6\u0003\u00a2\u0006\u0002\u0010\u001bJ\u0010\u0010 \u001a\u0004\u0018\u00010\u0005H\u00c6\u0003\u00a2\u0006\u0002\u0010\u0014J\u0011\u0010!\u001a\n\u0012\u0004\u0012\u00020\u000b\u0018\u00010\nH\u00c6\u0003J\u0011\u0010\"\u001a\n\u0012\u0004\u0012\u00020\u0005\u0018\u00010\nH\u00c6\u0003J\u0011\u0010#\u001a\n\u0012\u0004\u0012\u00020\u0005\u0018\u00010\nH\u00c6\u0003Jp\u0010$\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00072\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u00052\u0010\b\u0002\u0010\t\u001a\n\u0012\u0004\u0012\u00020\u000b\u0018\u00010\n2\u0010\b\u0002\u0010\f\u001a\n\u0012\u0004\u0012\u00020\u0005\u0018\u00010\n2\u0010\b\u0002\u0010\r\u001a\n\u0012\u0004\u0012\u00020\u0005\u0018\u00010\nH\u00c6\u0001\u00a2\u0006\u0002\u0010%J\u0013\u0010&\u001a\u00020\'2\b\u0010(\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010)\u001a\u00020\u0005H\u00d6\u0001J\u0006\u0010*\u001a\u00020\u000bJ\t\u0010+\u001a\u00020\u000bH\u00d6\u0001R\u0019\u0010\t\u001a\n\u0012\u0004\u0012\u00020\u000b\u0018\u00010\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0019\u0010\r\u001a\n\u0012\u0004\u0012\u00020\u0005\u0018\u00010\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0010R\u0019\u0010\f\u001a\n\u0012\u0004\u0012\u00020\u0005\u0018\u00010\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0010R\u0015\u0010\b\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\n\n\u0002\u0010\u0015\u001a\u0004\b\u0013\u0010\u0014R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019R\u0015\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u00a2\u0006\n\n\u0002\u0010\u001c\u001a\u0004\b\u001a\u0010\u001b\u00a8\u0006."}, d2 = {"Lcom/calsync/app/domain/util/RecurrenceRule;", "", "freq", "Lcom/calsync/app/domain/util/RecurrenceRule$Freq;", "interval", "", "until", "", "count", "byDay", "", "", "byMonthDay", "byMonth", "(Lcom/calsync/app/domain/util/RecurrenceRule$Freq;ILjava/lang/Long;Ljava/lang/Integer;Ljava/util/List;Ljava/util/List;Ljava/util/List;)V", "getByDay", "()Ljava/util/List;", "getByMonth", "getByMonthDay", "getCount", "()Ljava/lang/Integer;", "Ljava/lang/Integer;", "getFreq", "()Lcom/calsync/app/domain/util/RecurrenceRule$Freq;", "getInterval", "()I", "getUntil", "()Ljava/lang/Long;", "Ljava/lang/Long;", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "copy", "(Lcom/calsync/app/domain/util/RecurrenceRule$Freq;ILjava/lang/Long;Ljava/lang/Integer;Ljava/util/List;Ljava/util/List;Ljava/util/List;)Lcom/calsync/app/domain/util/RecurrenceRule;", "equals", "", "other", "hashCode", "toRRule", "toString", "Companion", "Freq", "app_debug"})
public final class RecurrenceRule {
    @org.jetbrains.annotations.NotNull()
    private final com.calsync.app.domain.util.RecurrenceRule.Freq freq = null;
    private final int interval = 0;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Long until = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Integer count = null;
    @org.jetbrains.annotations.Nullable()
    private final java.util.List<java.lang.String> byDay = null;
    @org.jetbrains.annotations.Nullable()
    private final java.util.List<java.lang.Integer> byMonthDay = null;
    @org.jetbrains.annotations.Nullable()
    private final java.util.List<java.lang.Integer> byMonth = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.calsync.app.domain.util.RecurrenceRule.Companion Companion = null;
    
    public RecurrenceRule(@org.jetbrains.annotations.NotNull()
    com.calsync.app.domain.util.RecurrenceRule.Freq freq, int interval, @org.jetbrains.annotations.Nullable()
    java.lang.Long until, @org.jetbrains.annotations.Nullable()
    java.lang.Integer count, @org.jetbrains.annotations.Nullable()
    java.util.List<java.lang.String> byDay, @org.jetbrains.annotations.Nullable()
    java.util.List<java.lang.Integer> byMonthDay, @org.jetbrains.annotations.Nullable()
    java.util.List<java.lang.Integer> byMonth) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.calsync.app.domain.util.RecurrenceRule.Freq getFreq() {
        return null;
    }
    
    public final int getInterval() {
        return 0;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long getUntil() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer getCount() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.List<java.lang.String> getByDay() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.List<java.lang.Integer> getByMonthDay() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.List<java.lang.Integer> getByMonth() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String toRRule() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.calsync.app.domain.util.RecurrenceRule.Freq component1() {
        return null;
    }
    
    public final int component2() {
        return 0;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long component3() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer component4() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.List<java.lang.String> component5() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.List<java.lang.Integer> component6() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.List<java.lang.Integer> component7() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.calsync.app.domain.util.RecurrenceRule copy(@org.jetbrains.annotations.NotNull()
    com.calsync.app.domain.util.RecurrenceRule.Freq freq, int interval, @org.jetbrains.annotations.Nullable()
    java.lang.Long until, @org.jetbrains.annotations.Nullable()
    java.lang.Integer count, @org.jetbrains.annotations.Nullable()
    java.util.List<java.lang.String> byDay, @org.jetbrains.annotations.Nullable()
    java.util.List<java.lang.Integer> byMonthDay, @org.jetbrains.annotations.Nullable()
    java.util.List<java.lang.Integer> byMonth) {
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0002J\u0012\u0010\u0007\u001a\u0004\u0018\u00010\b2\b\u0010\t\u001a\u0004\u0018\u00010\u0004J\u0017\u0010\n\u001a\u0004\u0018\u00010\u00062\u0006\u0010\u000b\u001a\u00020\u0004H\u0002\u00a2\u0006\u0002\u0010\f\u00a8\u0006\r"}, d2 = {"Lcom/calsync/app/domain/util/RecurrenceRule$Companion;", "", "()V", "formatUntil", "", "ts", "", "fromRRule", "Lcom/calsync/app/domain/util/RecurrenceRule;", "rule", "parseUntil", "s", "(Ljava/lang/String;)Ljava/lang/Long;", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.Nullable()
        public final com.calsync.app.domain.util.RecurrenceRule fromRRule(@org.jetbrains.annotations.Nullable()
        java.lang.String rule) {
            return null;
        }
        
        private final java.lang.String formatUntil(long ts) {
            return null;
        }
        
        private final java.lang.Long parseUntil(java.lang.String s) {
            return null;
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0006\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006\u00a8\u0006\u0007"}, d2 = {"Lcom/calsync/app/domain/util/RecurrenceRule$Freq;", "", "(Ljava/lang/String;I)V", "DAILY", "WEEKLY", "MONTHLY", "YEARLY", "app_debug"})
    public static enum Freq {
        /*public static final*/ DAILY /* = new DAILY() */,
        /*public static final*/ WEEKLY /* = new WEEKLY() */,
        /*public static final*/ MONTHLY /* = new MONTHLY() */,
        /*public static final*/ YEARLY /* = new YEARLY() */;
        
        Freq() {
        }
        
        @org.jetbrains.annotations.NotNull()
        public static kotlin.enums.EnumEntries<com.calsync.app.domain.util.RecurrenceRule.Freq> getEntries() {
            return null;
        }
    }
}