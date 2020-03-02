/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.skin.libs;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.R;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatCheckedTextView;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatMultiAutoCompleteTextView;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.AppCompatToggleButton;
import androidx.appcompat.widget.TintContextWrapper;
import androidx.appcompat.widget.VectorEnabledTintResources;
import androidx.core.view.ViewCompat;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * This class is responsible for manually inflating our tinted widgets.
 * <p>This class two main responsibilities: the first is to 'inject' our tinted views in place of
 * the framework versions in layout inflation; the second is backport the {@code android:theme}
 * functionality for any inflated widgets. This include theme inheritance from its parent.
 */
final class AppCompatViewInflater{
    private static final boolean IS_PRE_LOLLIPOP = Build.VERSION.SDK_INT < 21;
    private static final int[] sOnClickAttrs = new int[]{android.R.attr.onClick};

    /**
     * Allows us to emulate the {@code android:theme} attribute for devices before L.
     */
    private static Context themifyContext(Context context,AttributeSet attrs,boolean useAndroidTheme,
            boolean useAppTheme)
    {
        final TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.View,0,0);
        int themeId = 0;
        if(useAndroidTheme){
            // First try reading android:theme if enabled
            themeId = a.getResourceId(R.styleable.View_android_theme,0);
        }
        if(useAppTheme && themeId == 0){
            // ...if that didn't work, try reading app:theme (for legacy reasons) if enabled
            themeId = a.getResourceId(R.styleable.View_theme,0);
        }
        a.recycle();

        if(themeId != 0 &&
           (! (context instanceof ContextThemeWrapper) || ((ContextThemeWrapper)context).getThemeResId() != themeId))
        {
            // If the context isn't a ContextThemeWrapper, or it is but does not have
            // the same theme as we need, wrap it in a new wrapper
            context = new ContextThemeWrapper(context,themeId);
        }
        return context;
    }

    public View createView(View parent,final String name,@NonNull Context context,@NonNull AttributeSet attrs){
        return createView(parent,name,context,attrs,
                IS_PRE_LOLLIPOP, /* Only read android:theme pre-L (L+ handles this anyway) */
                true, /* Read read app:theme as a fallback at all times for legacy reasons */
                VectorEnabledTintResources.shouldBeUsed() /* Only tint wrap the context if enabled */);
    }

    final View createView(View parent,final String name,@NonNull Context context,@NonNull AttributeSet attrs,
            boolean readAndroidTheme,boolean readAppTheme,boolean wrapContext)
    {

        if(readAndroidTheme || readAppTheme){
            // We then apply the theme on the context, if specified
            context = themifyContext(context,attrs,readAndroidTheme,readAppTheme);
        }
        if(wrapContext){
            context = TintContextWrapper.wrap(context);
        }

        View view = null;

        // We need to 'inject' our tint aware Views in place of the standard framework versions
        switch(name){
            case "LinearLayout":
                view = createLinearLayout(context,attrs);
                verifyNotNull(view,name);
                break;
            case "RelativeLayout":
                view = createRelativeLayout(context,attrs);
                verifyNotNull(view,name);
                break;
            case "FrameLayout":
                view = createFrameLayout(context,attrs);
                verifyNotNull(view,name);
                break;
            case "TextView":
                view = createTextView(context,attrs);
                verifyNotNull(view,name);
                break;
            case "ImageView":
                view = createImageView(context,attrs);
                verifyNotNull(view,name);
                break;
            case "Button":
                view = createButton(context,attrs);
                verifyNotNull(view,name);
                break;
            case "EditText":
                view = createEditText(context,attrs);
                verifyNotNull(view,name);
                break;
            case "Spinner":
                view = createSpinner(context,attrs);
                verifyNotNull(view,name);
                break;
            case "ImageButton":
                view = createImageButton(context,attrs);
                verifyNotNull(view,name);
                break;
            case "CheckBox":
                view = createCheckBox(context,attrs);
                verifyNotNull(view,name);
                break;
            case "RadioButton":
                view = createRadioButton(context,attrs);
                verifyNotNull(view,name);
                break;
            case "CheckedTextView":
                view = createCheckedTextView(context,attrs);
                verifyNotNull(view,name);
                break;
            case "AutoCompleteTextView":
                view = createAutoCompleteTextView(context,attrs);
                verifyNotNull(view,name);
                break;
            case "MultiAutoCompleteTextView":
                view = createMultiAutoCompleteTextView(context,attrs);
                verifyNotNull(view,name);
                break;
            case "RatingBar":
                view = createRatingBar(context,attrs);
                verifyNotNull(view,name);
                break;
            case "SeekBar":
                view = createSeekBar(context,attrs);
                verifyNotNull(view,name);
                break;
            case "ToggleButton":
                view = createToggleButton(context,attrs);
                verifyNotNull(view,name);
                break;
            default:
                // The fallback that allows extending class to take over view inflation
                // for other tags. Note that we don't check that the result is not-null.
                // That allows the custom inflater path to fall back on the default one
                // later in this method.
                view = createView(context,name,attrs);
        }

        if(view != null){
            // If we have created a view, check its android:onClick
            checkOnClickListener(view,attrs);
        }

        return view;
    }

    @NonNull
    protected AppCompatTextView createTextView(Context context,AttributeSet attrs){
        return new AppCompatTextView(context,attrs);
    }

    @NonNull
    protected AppCompatImageView createImageView(Context context,AttributeSet attrs){
        return new AppCompatImageView(context,attrs);
    }

    @NonNull
    protected AppCompatButton createButton(Context context,AttributeSet attrs){
        return new AppCompatButton(context,attrs);
    }

    @NonNull
    protected AppCompatEditText createEditText(Context context,AttributeSet attrs){
        return new AppCompatEditText(context,attrs);
    }

    @NonNull
    protected AppCompatSpinner createSpinner(Context context,AttributeSet attrs){
        return new AppCompatSpinner(context,attrs);
    }

    @NonNull
    protected AppCompatImageButton createImageButton(Context context,AttributeSet attrs){
        return new AppCompatImageButton(context,attrs);
    }

    @NonNull
    protected AppCompatCheckBox createCheckBox(Context context,AttributeSet attrs){
        return new AppCompatCheckBox(context,attrs);
    }

    @NonNull
    protected AppCompatRadioButton createRadioButton(Context context,AttributeSet attrs){
        return new AppCompatRadioButton(context,attrs);
    }

    @NonNull
    protected AppCompatCheckedTextView createCheckedTextView(Context context,AttributeSet attrs){
        return new AppCompatCheckedTextView(context,attrs);
    }

    @NonNull
    protected AppCompatAutoCompleteTextView createAutoCompleteTextView(Context context,AttributeSet attrs){
        return new AppCompatAutoCompleteTextView(context,attrs);
    }

    @NonNull
    protected AppCompatMultiAutoCompleteTextView createMultiAutoCompleteTextView(Context context,AttributeSet attrs){
        return new AppCompatMultiAutoCompleteTextView(context,attrs);
    }

    @NonNull
    protected AppCompatRatingBar createRatingBar(Context context,AttributeSet attrs){
        return new AppCompatRatingBar(context,attrs);
    }

    @NonNull
    protected AppCompatSeekBar createSeekBar(Context context,AttributeSet attrs){
        return new AppCompatSeekBar(context,attrs);
    }

    @NonNull
    protected AppCompatToggleButton createToggleButton(Context context,AttributeSet attrs){
        return new AppCompatToggleButton(context,attrs);
    }

    @NonNull
    protected LinearLayout createLinearLayout(Context context,AttributeSet attrs){
        return new LinearLayout(context,attrs);
    }

    @NonNull
    protected RelativeLayout createRelativeLayout(Context context,AttributeSet attrs){
        return new RelativeLayout(context,attrs);
    }

    @NonNull
    protected FrameLayout createFrameLayout(Context context,AttributeSet attrs){
        return new FrameLayout(context,attrs);
    }

    private void verifyNotNull(View view,String name){
        if(view == null){
            throw new IllegalStateException(
                    this.getClass().getName() + " asked to inflate view for <" + name + ">, but returned null");
        }
    }

    @Nullable
    protected View createView(Context context,String name,AttributeSet attrs){
        try{
            return LayoutInflater.from(context).createView(name,null,attrs);
        } catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * android:onClick doesn't handle views with a ContextWrapper context. This method
     * backports new framework functionality to traverse the Context wrappers to find a
     * suitable target.
     */
    private void checkOnClickListener(View view,AttributeSet attrs){
        final Context context = view.getContext();

        if(! (context instanceof ContextWrapper) || (Build.VERSION.SDK_INT >= 15 && ! ViewCompat.hasOnClickListeners(
                view)))
        {
            // Skip our compat functionality if: the Context isn't a ContextWrapper, or
            // the view doesn't have an OnClickListener (we can only rely on this on API 15+ so
            // always use our compat code on older devices)
            return;
        }

        final TypedArray a = context.obtainStyledAttributes(attrs,sOnClickAttrs);
        final String handlerName = a.getString(0);
        if(handlerName != null){
            view.setOnClickListener(new DeclaredOnClickListener(view,handlerName));
        }
        a.recycle();
    }

    /**
     * An implementation of OnClickListener that attempts to lazily load a
     * named click handling method from a parent or ancestor context.
     */
    private static class DeclaredOnClickListener implements View.OnClickListener{
        private final View mHostView;
        private final String mMethodName;

        private Method mResolvedMethod;
        private Context mResolvedContext;

        public DeclaredOnClickListener(@NonNull View hostView,@NonNull String methodName){
            mHostView = hostView;
            mMethodName = methodName;
        }

        @Override
        public void onClick(@NonNull View v){
            if(mResolvedMethod == null){
                resolveMethod(mHostView.getContext(),mMethodName);
            }

            try{
                mResolvedMethod.invoke(mResolvedContext,v);
            } catch(IllegalAccessException e){
                throw new IllegalStateException("Could not execute non-public method for android:onClick",e);
            } catch(InvocationTargetException e){
                throw new IllegalStateException("Could not execute method for android:onClick",e);
            }
        }

        @NonNull
        private void resolveMethod(@Nullable Context context,@NonNull String name){
            while(context != null){
                try{
                    if(! context.isRestricted()){
                        final Method method = context.getClass().getMethod(mMethodName,View.class);
                        if(method != null){
                            mResolvedMethod = method;
                            mResolvedContext = context;
                            return;
                        }
                    }
                } catch(NoSuchMethodException e){
                    // Failed to find method, keep searching up the hierarchy.
                }

                if(context instanceof ContextWrapper){
                    context = ((ContextWrapper)context).getBaseContext();
                } else{
                    // Can't search up the hierarchy, null out and fail.
                    context = null;
                }
            }

            final int id = mHostView.getId();
            final String idText =
                    id == View.NO_ID ? "" : " with id '" + mHostView.getContext().getResources().getResourceEntryName(
                            id) + "'";
            throw new IllegalStateException("Could not find method " + mMethodName +
                                            "(View) in a parent or ancestor Context for android:onClick " +
                                            "attribute defined on view " + mHostView.getClass() + idText);
        }
    }
}
