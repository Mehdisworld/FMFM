package com.phone.myapplication.sneha.utils;

import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0007\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u00002\u00020\u0001B\u0019\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003¢\u0006\u0002\u0010\u0005J\t\u0010\t\u001a\u00020\u0003HÆ\u0003J\t\u0010\n\u001a\u00020\u0003HÆ\u0003J\u001d\u0010\u000b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\f\u001a\u00020\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u000f\u001a\u00020\u0010HÖ\u0001J\t\u0010\u0011\u001a\u00020\u0012HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0007¨\u0006\u0013"}, d2 = {"Lcom/phone/myapplication/sneha/utils/AspectRatio;", "", "aspectX", "", "aspectY", "(FF)V", "getAspectX", "()F", "getAspectY", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "toString", "", "app_release"}, k = 1, mv = {1, 4, 1})
public final class AspectRatio {
    private final float aspectX;
    private final float aspectY;

    public AspectRatio() {
        this(0.0f, 0.0f, 3, (DefaultConstructorMarker) null);
    }

    public static AspectRatio copy$default(AspectRatio aspectRatio, float f, float f2, int i, Object obj) {
        if ((i & 1) != 0) {
            f = aspectRatio.aspectX;
        }
        if ((i & 2) != 0) {
            f2 = aspectRatio.aspectY;
        }
        return aspectRatio.copy(f, f2);
    }

    public final float component1() {
        return this.aspectX;
    }

    public final float component2() {
        return this.aspectY;
    }

    public final AspectRatio copy(float f, float f2) {
        return new AspectRatio(f, f2);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof AspectRatio)) {
            return false;
        }
        AspectRatio aspectRatio = (AspectRatio) obj;
        if (Float.compare(this.aspectX, aspectRatio.aspectX) == 0 && Float.compare(this.aspectY, aspectRatio.aspectY) == 0) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return (Float.floatToIntBits(this.aspectX) * 31) + Float.floatToIntBits(this.aspectY);
    }

    public String toString() {
        return "AspectRatio(aspectX=" + this.aspectX + ", aspectY=" + this.aspectY + ")";
    }

    public AspectRatio(float f, float f2) {
        this.aspectX = f;
        this.aspectY = f2;
    }

    public final float getAspectX() {
        return this.aspectX;
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public AspectRatio(float f, float f2, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this((i & 1) != 0 ? 0.0f : f, (i & 2) != 0 ? 0.0f : f2);
    }

    public final float getAspectY() {
        return this.aspectY;
    }
}
