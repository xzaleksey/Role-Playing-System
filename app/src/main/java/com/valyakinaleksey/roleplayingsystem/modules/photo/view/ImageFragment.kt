package com.valyakinaleksey.roleplayingsystem.modules.photo.view

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.valyakinaleksey.roleplayingsystem.R
import com.valyakinaleksey.roleplayingsystem.core.view.AbsActivity
import com.valyakinaleksey.roleplayingsystem.utils.ScreenUtils
import com.valyakinaleksey.roleplayingsystem.utils.StringUtils
import kotlinx.android.synthetic.main.fragment_photo_view.*
import uk.co.senab.photoview.PhotoViewAttacher

class ImageFragment : Fragment() {

  private var url: String? = null
  private var name: String? = null
  private var preview: String? = null
  private var success = false
  lateinit private var photoViewAttacher: PhotoViewAttacher
  private val layoutId: Int = R.layout.fragment_photo_view
  private var statusBarColor: Int? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    retainInstance = true
    if (savedInstanceState == null) {
      url = arguments.getString(IMAGE_URL)
      preview = arguments.getString(PREVIEW)
      name = arguments.getString(NAME)
    } else {
      url = savedInstanceState.getString(IMAGE_URL)
      preview = savedInstanceState.getString(PREVIEW)
      name = savedInstanceState.getString(NAME)
    }
  }


  override fun onCreateView(
      inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater!!.inflate(layoutId, container, false)
  }

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    btnRetry.setOnClickListener {
      btnRetry.visibility = View.GONE
      loadPreviewAndMainImage()
    }
    photoViewAttacher = PhotoViewAttacher(photo_container)
    val activity = activity as AbsActivity
    activity.supportActionBar?.hide()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      statusBarColor = activity.window?.statusBarColor
      activity.window?.let {
        // Set Fullscreen
        it.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        it.statusBarColor = Color.parseColor("#20111111")
      }
    }
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    if (!StringUtils.isEmpty(preview)) {
      loadPreviewAndMainImage()
    } else {
      loadMainImage()
    }
  }

  private fun loadPreviewAndMainImage() {
    if (!TextUtils.isEmpty(url) && !success) {
      progress.visibility = View.VISIBLE
      photo_container.post {
        val displayMetrics = ScreenUtils.getDisplayMetrics(context)
        Glide
            .with(activity)
            .load(preview)
            .override(displayMetrics.widthPixels, displayMetrics.heightPixels)
            .into(object : SimpleTarget<GlideDrawable>() {
              override fun onResourceReady(
                  resource: GlideDrawable,
                  glideAnimation: GlideAnimation<in GlideDrawable>) {
                progress.visibility = View.GONE
                photo_container.setImageDrawable(resource)
                loadMainImage()
              }

              override fun onLoadFailed(e: Exception?, errorDrawable: Drawable?) {
                super.onLoadFailed(e, errorDrawable)
                progress.visibility = View.VISIBLE
                loadMainImage()
              }
            })
      }
    }
  }

  private fun loadMainImage() {
    val displayMetrics = ScreenUtils.getDisplayMetrics(context)
    progress.visibility = View.VISIBLE
    Glide
        .with(activity)
        .load(url)
        .override(displayMetrics.widthPixels, displayMetrics.heightPixels)
        .into(object : SimpleTarget<GlideDrawable>() {
          override fun onResourceReady(
              resource: GlideDrawable, glideAnimation: GlideAnimation<in GlideDrawable>) {
            progress.visibility = View.GONE
            success = true
            photo_container.setImageDrawable(resource)
            photoViewAttacher.update()
          }

          override fun onLoadFailed(e: Exception?, errorDrawable: Drawable?) {
            super.onLoadFailed(e, errorDrawable)
            progress.visibility = View.GONE
            btnRetry.visibility = View.VISIBLE
          }
        })
  }

  override fun onDestroyView() {
    super.onDestroyView()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      statusBarColor?.let {
        activity.window?.statusBarColor = it
      }
    }
    (activity as AbsActivity).supportActionBar?.show()
  }

  override fun onSaveInstanceState(outState: Bundle?) {
    super.onSaveInstanceState(outState)
    outState!!.putString(IMAGE_URL, url)
    outState.putString(PREVIEW, preview)
    outState.putString(NAME, name)
  }

  companion object {
    @JvmField
    val IMAGE_URL = "IMAGE_URL"
    @JvmField
    val NAME = "NAME"
    val PREVIEW = "PREVIEW"
    @JvmField
    var TAG = "ImageFragment"

    @JvmStatic
    fun newInstance(url: String, preview: String): ImageFragment {
      val args = Bundle()
      args.putString(IMAGE_URL, url)
      args.putString(PREVIEW, preview)
      val fragment = ImageFragment()
      fragment.arguments = args
      return fragment
    }

    @JvmStatic
    fun newInstance(args: Bundle?): ImageFragment {
      val fragment = ImageFragment()
      fragment.arguments = args
      return fragment
    }
  }
}