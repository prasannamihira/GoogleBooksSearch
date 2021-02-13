package com.ninetyninex.googlebookssearch.ui.main.volume.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ninetyninex.googlebookssearch.R
import com.ninetyninex.googlebookssearch.app.App
import com.ninetyninex.googlebookssearch.data.remote.response.Volume
import com.ninetyninex.googlebookssearch.ui.main.volume.BookVolumeDetailsActivity

class BooksVolumesRecyclerViewAdapter(
    supportFragmentManager: FragmentManager,
    private val bookVolumesList: ArrayList<Volume>
) :
    RecyclerView.Adapter<BooksVolumesRecyclerViewAdapter.BooksVolumesRecyclerViewHolder>() {

    /**
     * Bind the ui layout with the view holder
     *
     * @param parent
     * @param viewType
     * @return
     */
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BooksVolumesRecyclerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView: View =
            inflater.inflate(R.layout.list_item_book_volume, parent, false)

        return BooksVolumesRecyclerViewHolder(itemView)
    }


    /**
     * Bind data values with ui controls
     *
     * @param holder
     * @param position
     */
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: BooksVolumesRecyclerViewHolder, position: Int) {

        // set image icon
        holder.bookVolumeImage.post{
            Glide.with(holder.bookVolumeImage.context)
                .load(bookVolumesList[position].volumeInfo?.imageLinks?.smallThumbnail)
                .into(holder.bookVolumeImage)
        }
        // book title
        holder.bookVolumeTitle.text = bookVolumesList[position].volumeInfo?.title
        // publisher
        val publisher = bookVolumesList[position].volumeInfo?.publisher
        if(publisher != null)
            holder.bookVolumePublisher.text = "by $publisher"
        // book details
        holder.bookVolumeDetails.text = bookVolumesList[position].volumeInfo?.title

        holder.rowView.setOnClickListener(View.OnClickListener {

                // Start book details activity
                val intent = Intent(App.instance, BookVolumeDetailsActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.putExtra("book_volume", bookVolumesList[position])
                App.instance.startActivity(intent)

        })
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    override fun getItemCount(): Int {
        return bookVolumesList.size
    }

    class BooksVolumesRecyclerViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val bookVolumeImage: ImageView =
            itemView.findViewById(R.id.iv_book_volume)
        val bookVolumeTitle: TextView =
            itemView.findViewById(R.id.tv_book_volume_title)
        val bookVolumePublisher: TextView =
            itemView.findViewById(R.id.tv_book_volume_publisher)
        val bookVolumeDetails: TextView =
            itemView.findViewById(R.id.tv_book_volume_details)

        var rowView: View = itemView
    }
}