package com.phone.myapplication.sneha.utils;

import com.io.filemanager.freefileexplorer.easily.R;
import kotlin.jvm.internal.DefaultConstructorMarker;

public enum RatioText {
    FREE(R.string.iamutkarshtiwari_github_io_ananas_free_size, new AspectRatio(0.0f, 0.0f, 3, (DefaultConstructorMarker) null)),
    ASPECT_INS_4_5(R.string.aspect_ins_4_5, new AspectRatio(4.0f, 5.0f)),
    ASPECT_INS_STORY(R.string.aspect_ins_story, new AspectRatio(9.0f, 16.0f)),
    ASPECT_5_4(R.string.aspect_5_4, new AspectRatio(5.0f, 4.0f)),
    ASPECT_3_4(R.string.aspect_3_4, new AspectRatio(3.0f, 4.0f)),
    ASPECT_4_3(R.string.aspect_4_3, new AspectRatio(4.0f, 3.0f)),
    ASPECT_FACE_POST(R.string.aspect_face_post, new AspectRatio(1.91f, 1.0f)),
    ASPECT_FACE_COVER(R.string.aspect_face_cover, new AspectRatio(2.62f, 1.0f)),
    ASPECT_PIN_POST(R.string.aspect_pin_post, new AspectRatio(2.0f, 3.0f)),
    ASPECT_3_2(R.string.aspect_3_2, new AspectRatio(3.0f, 2.0f)),
    ASPECT_9_16(R.string.aspect_9_16, new AspectRatio(9.0f, 16.0f)),
    ASPECT_16_9(R.string.aspect_16_9, new AspectRatio(16.0f, 9.0f)),
    ASPECT_1_2(R.string.aspect_1_2, new AspectRatio(1.0f, 2.0f)),
    ASPECT_YOU_COVER(R.string.aspect_you_cover, new AspectRatio(1.77f, 1.0f)),
    ASPECT_TWIT_POST(R.string.aspect_twit_post, new AspectRatio(1.91f, 1.0f)),
    ASPECT_TWIT_HEADER(R.string.aspect_twit_header, new AspectRatio(3.0f, 1.0f));
    
    private final AspectRatio aspectRatio;
    private final int ratioTextId;

    private RatioText(int i, AspectRatio aspectRatio2) {
        this.ratioTextId = i;
        this.aspectRatio = aspectRatio2;
    }

    public final AspectRatio getAspectRatio() {
        return this.aspectRatio;
    }

    public final int getRatioTextId() {
        return this.ratioTextId;
    }
}
