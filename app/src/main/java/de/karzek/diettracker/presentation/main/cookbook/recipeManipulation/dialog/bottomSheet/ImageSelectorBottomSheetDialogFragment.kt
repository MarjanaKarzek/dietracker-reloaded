package de.karzek.diettracker.presentation.main.cookbook.recipeManipulation.dialog.bottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import de.karzek.diettracker.R
import kotlinx.android.synthetic.main.snippet_bottom_sheet_image_selector.*

/**
 * Created by MarjanaKarzek on 29.06.2018.
 *
 * @author Marjana Karzek
 * @version 1.0
 * @date 29.06.2018
 */
class ImageSelectorBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private var cameraClickListener: OnOpenCameraClickListener? = null
    private var galleryClickListener: OnOpenGalleryClickListener? = null

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.snippet_bottom_sheet_image_selector, container,
                false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            cameraClickListener = activity as OnOpenCameraClickListener?
            galleryClickListener = activity as OnOpenGalleryClickListener?
        } catch (e: ClassCastException) {
            throw ClassCastException("fragment must implement OnOpenCameraClickListener and OnOpenGaleryClickListener")
        }

        image_source_camera.setOnClickListener {
            cameraClickListener!!.onOpenCameraClickedInBottomSheet()
            this.dismiss()
        }
        image_source_gallery.setOnClickListener {
            galleryClickListener!!.onOpenGalleryClickedInBottomSheet()
            this.dismiss()
        }
    }

    interface OnOpenCameraClickListener {
        fun onOpenCameraClickedInBottomSheet()
    }

    interface OnOpenGalleryClickListener {
        fun onOpenGalleryClickedInBottomSheet()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cameraClickListener = null
        galleryClickListener = null
    }

    companion object {

        fun newInstance(): ImageSelectorBottomSheetDialogFragment {
            return ImageSelectorBottomSheetDialogFragment()
        }
    }
}
