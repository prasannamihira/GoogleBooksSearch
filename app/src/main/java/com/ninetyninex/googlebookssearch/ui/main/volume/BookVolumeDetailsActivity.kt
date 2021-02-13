package com.ninetyninex.googlebookssearch.ui.main.volume

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.ninetyninex.googlebookssearch.R
import com.ninetyninex.googlebookssearch.data.remote.response.Volume
import com.ninetyninex.googlebookssearch.databinding.ActivityBookVolumeDetailsBinding

class BookVolumeDetailsActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        var mBinding: ActivityBookVolumeDetailsBinding? = null

        private lateinit var bookVolume: Volume
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_book_volume_details)

        mBinding?.topBarMain?.ivBackToolbar?.visibility = View.VISIBLE

        // set click listener
        mBinding?.topBarMain?.ivBackToolbar?.setOnClickListener(this)

        // get the selected book volume data
        bookVolume = intent.getParcelableExtra<Volume>("book_volume")!!

        // set ui data
        populateUIData(bookVolume)
    }

    /**
     * populate book volume information
     *
     * @param volume
     */
    private fun populateUIData(volume: Volume) {
        // title
        mBinding?.topBarMain?.toolbarTvTitle?.text = volume.volumeInfo?.title

        // publisher
        val publisher = volume.volumeInfo?.publisher
        if(publisher != null)
            mBinding?.tvBookVolumePublisher?.text = "by $publisher"

        // set image icon
        mBinding?.ivBookVolumeDetails?.post{
            Glide.with(this)
                .load(volume.volumeInfo?.imageLinks?.smallThumbnail)
                .into(mBinding?.ivBookVolumeDetails!!)
        }
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_back_toolbar -> {
                // navigate to the main tab screen
                finish()
            }
        }
    }
}