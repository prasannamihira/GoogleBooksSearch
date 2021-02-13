package com.ninetyninex.googlebookssearch.ui.main.volume

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ninetyninex.googlebookssearch.R
import com.ninetyninex.googlebookssearch.app.App
import com.ninetyninex.googlebookssearch.base.BaseActivity
import com.ninetyninex.googlebookssearch.data.remote.response.BooksVolumesResponse
import com.ninetyninex.googlebookssearch.data.remote.response.Volume
import com.ninetyninex.googlebookssearch.databinding.ActivityBooksVolumeBinding
import com.ninetyninex.googlebookssearch.repository.BooksVolumesApiRepository
import com.ninetyninex.googlebookssearch.ui.main.volume.adapter.BooksVolumesRecyclerViewAdapter
import com.ninetyninex.googlebookssearch.util.Config
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class BooksVolumeActivity : BaseActivity() {

    @Inject
    lateinit var booksVolumesApiRepository: BooksVolumesApiRepository

    companion object {
        var mBinding: ActivityBooksVolumeBinding? = null

        var volumeItemsPaginated: ArrayList<Volume> = ArrayList()
        var volumeItems: ArrayList<Volume> = ArrayList()

        // Books volumes list adapter to set data to the recycler list
        lateinit var booksVolumesRecyclerViewAdapter: BooksVolumesRecyclerViewAdapter

        // Layout manager for the books volumes list
        lateinit var layoutManager: LinearLayoutManager

        // pagination params
        var isLoading: Boolean = false
        var isLoadingEnd: Boolean = false
        var offset: Int = 1
        var limit: Int = 20
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        App.appComponent.inject(this)

        // set full screen window
        fullScreen()

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_books_volume)

        // load new data to the existing list when scroll to last item
        mBinding?.rvBooksList?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                val totalItemCount = layoutManager.itemCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                if (totalItemCount > 1) {
                    if (lastVisibleItem >= totalItemCount - 1) {
                        if (!isLoading && !isLoadingEnd) {
                            if (layoutManager.findLastCompletelyVisibleItemPosition() == volumeItemsPaginated.size - 1) {
                                offset += 1
                                fetchBooks("flowers")
                                isLoading = true
                            }
                        }
                    }
                }
            }
        })

        var filteredLockList = ArrayList<Volume>()

        val searchBar: SearchView =
            mBinding?.svBooks as SearchView

        // search books from list
        searchBar.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {

                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {

                filteredLockList.clear()

                filteredLockList = volumeItemsPaginated.filter {
                    it.volumeInfo?.title.toString().toLowerCase(
                    ).contains(newText)
                }.toCollection(ArrayList())

                booksVolumesRecyclerViewAdapter =
                    BooksVolumesRecyclerViewAdapter(
                        supportFragmentManager,
                        filteredLockList
                    )

                mBinding?.rvBooksList?.layoutManager = layoutManager
                mBinding?.rvBooksList?.adapter =
                    booksVolumesRecyclerViewAdapter
                booksVolumesRecyclerViewAdapter.notifyDataSetChanged()

                return true
            }
        })
    }

    override fun onResume() {
        super.onResume()

        volumeItemsPaginated.clear()

        fetchBooks("flowers")
    }

    /**
     * Search book volumes by given key word
     *
     * @param keyWord
     */
    @SuppressLint("CheckResult")
    private fun fetchBooks(keyWord: String?) {

        var booksVolumesData: BooksVolumesResponse?
        volumeItems.clear()

        booksVolumesApiRepository.fetchBooks(keyWord)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showProgress(mBinding!!.pbBookVolumes, true) }
            .doOnTerminate { showProgress(mBinding!!.pbBookVolumes, false) }
            .subscribe({
                if (it != null && booksVolumesApiRepository.dataBookVolumesRetrieved) {
                    booksVolumesData = booksVolumesApiRepository.dataBookVolumes

                    volumeItems =
                        booksVolumesData?.items?.toCollection(ArrayList()) as ArrayList<Volume>

                    if (volumeItems.size > 0) {

                        volumeItemsPaginated.addAll(volumeItems)

                        isLoading = false

                        if (volumeItems.size < limit) {
                            isLoadingEnd = true
                        }

                    } else {
                        // no records found
                        runOnUiSThread {
                            android.app.AlertDialog.Builder(
                                this
                            )
                                .setMessage(resources.getString(R.string.err_no_records_books_volumes))
                                .setPositiveButton(resources.getString(R.string.ok)) { dialogInterface, i ->
                                    dialogInterface.dismiss()
                                }
                                .setCancelable(false)
                                .show()
                        }
                    }
                    updateBookVolumesUI()

                } else {
                    if (booksVolumesApiRepository.errorCode == Config.REQUEST_FAILED) {
                        showProgress(mBinding!!.pbBookVolumes, false)

                    } else {
                        runOnUiSThread {
                            android.app.AlertDialog.Builder(
                                this
                            )
                                .setMessage(booksVolumesApiRepository.errorMessage)
                                .setPositiveButton(resources.getString(R.string.ok)) { dialogInterface, i ->
                                    dialogInterface.dismiss()
                                }
                                .setCancelable(false)
                                .show()
                        }
                    }
                }
            }, {
                handleNetworkError(it)
            })
    }

    /**
     * Update the UI using the books list
     *
     * */
    private fun updateBookVolumesUI() {
        // Create a custom adapter class for bind books data
        booksVolumesRecyclerViewAdapter =
            BooksVolumesRecyclerViewAdapter(
                supportFragmentManager,
                volumeItemsPaginated
            )

        layoutManager = LinearLayoutManager(this)
        mBinding?.rvBooksList?.layoutManager = layoutManager
        mBinding?.rvBooksList?.adapter =
            booksVolumesRecyclerViewAdapter
        booksVolumesRecyclerViewAdapter.notifyDataSetChanged()
    }

    private fun runOnUiSThread(runnable: Runnable) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            Handler(Looper.getMainLooper()).post(runnable)
        } else {
            runnable.run()
        }
    }

}