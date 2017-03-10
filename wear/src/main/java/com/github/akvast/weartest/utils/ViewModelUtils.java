package com.github.akvast.weartest.utils;

import android.content.res.Resources;
import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.common.RotationOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.io.File;

public final class ViewModelUtils {

    @BindingAdapter({"src", "tint"})
    public static void loadDrawable(@NonNull ImageView view, int resourceId, int tint) {
        Resources resources = view.getResources();
        Drawable drawable = ResourcesCompat.getDrawable(resources, resourceId, null);
        if (drawable != null) {
            Drawable wrap = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(wrap, resources.getColor(tint));
            view.setImageDrawable(wrap);
        }
    }

    // Fresco adapter:

    @BindingAdapter("src")
    public static void loadImage(@NonNull SimpleDraweeView view, @Nullable Object object) {
        Uri uri;

        if (object instanceof String) {
            uri = Uri.parse((String) object);
        } else if (object instanceof File) {
            uri = Uri.fromFile((File) object);
        } else {
            view.setImageURI((String) null);
            return;
        }

        int width = Math.min(480, view.getMeasuredWidth());
        int height = Math.min(720, view.getMeasuredHeight());

        if (width == 0) width = 480;
        if (height == 0) height = 720;

        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setRotationOptions(RotationOptions.autoRotate())
                .setProgressiveRenderingEnabled(true)
                .setResizeOptions(new ResizeOptions(width, height))
                .build();

        view.setController(Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(view.getController())
                .build());
    }

    // GONE visibility:

    public static int goneIfNot(@Nullable String value) {
        return TextUtils.isEmpty(value) ? View.GONE : View.VISIBLE;
    }

    public static int goneIf(@Nullable String value) {
        return TextUtils.isEmpty(value) ? View.VISIBLE : View.GONE;
    }

    public static int goneIfNot(boolean value) {
        return value ? View.VISIBLE : View.GONE;
    }

    public static int goneIf(boolean value) {
        return value ? View.GONE : View.VISIBLE;
    }

    // INVISIBLE visibility:

    public static int invisibleIfNot(@Nullable String value) {
        return TextUtils.isEmpty(value) ? View.INVISIBLE : View.VISIBLE;
    }

    public static int invisibleIf(@Nullable String value) {
        return TextUtils.isEmpty(value) ? View.VISIBLE : View.INVISIBLE;
    }

    public static int invisibleIfNot(boolean value) {
        return value ? View.VISIBLE : View.INVISIBLE;
    }

    public static int invisibleIf(boolean value) {
        return value ? View.INVISIBLE : View.VISIBLE;
    }

    // Base:

    @BindingAdapter("layout_width")
    public static void setLayoutWidth(View view, int width) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = Utils.dpToPx(width);
        view.setLayoutParams(layoutParams);
    }

    @BindingAdapter("layout_height")
    public static void setLayoutHeight(View view, int height) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = Utils.dpToPx(height);
        view.setLayoutParams(layoutParams);
    }

    @BindingAdapter("topMargin")
    public static void setTopMargin(View view, int margin) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        layoutParams.topMargin = Utils.dpToPx(margin);
        view.setLayoutParams(layoutParams);
    }

    @BindingAdapter("bottomMargin")
    public static void setBottomMargin(View view, int margin) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        layoutParams.bottomMargin = Utils.dpToPx(margin);
        view.setLayoutParams(layoutParams);
    }

}
