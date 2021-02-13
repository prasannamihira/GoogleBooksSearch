package com.ninetyninex.googlebookssearch.data.remote.response

import android.os.Parcel
import android.os.Parcelable

/**
 * Books volumes response data
 *
 * @param kind
 * @param totalItems
 * @param items
 */
data class BooksVolumesResponse(val kind: String?, val totalItems: Int?, val items: Array<Volume>?) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BooksVolumesResponse

        if (kind != other.kind) return false
        if (totalItems != other.totalItems) return false
        if (items != null) {
            if (other.items == null) return false
            if (!items.contentEquals(other.items)) return false
        } else if (other.items != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = kind?.hashCode() ?: 0
        result = 31 * result + (totalItems ?: 0)
        result = 31 * result + (items?.contentHashCode() ?: 0)
        return result
    }
}

/**
 * Book volume
 *
 * @param kind
 * @param id
 * @param selfLink
 * @param volumeInfo
 */
data class Volume(
    val kind: String?,
    val id: String?,
    val selfLink: String?,
    val volumeInfo: VolumeInfo?,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(VolumeInfo::class.java.classLoader)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(kind)
        parcel.writeString(id)
        parcel.writeString(selfLink)
        parcel.writeParcelable(volumeInfo, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Volume> {
        override fun createFromParcel(parcel: Parcel): Volume {
            return Volume(parcel)
        }

        override fun newArray(size: Int): Array<Volume?> {
            return arrayOfNulls(size)
        }
    }
}

/**
 * Book volume
 *
 * @param title
 * @param authors
 * @param publisher
 * @param previewLink
 * @param infoLink
 */
data class VolumeInfo(
    val title: String?,
    val authors: Array<String>?,
    val publisher: String?,
    val imageLinks: ImageLinks?,
    val previewLink: String?,
    val infoLink: String?
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.createStringArray(),
        parcel.readString(),
        parcel.readParcelable(ImageLinks::class.java.classLoader),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as VolumeInfo

        if (title != other.title) return false
        if (!authors.contentEquals(other.authors)) return false
        if (publisher != other.publisher) return false
        if (imageLinks != other.imageLinks) return false
        if (previewLink != other.previewLink) return false
        if (infoLink != other.infoLink) return false

        return true
    }

    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + authors.contentHashCode()
        result = 31 * result + publisher.hashCode()
        result = 31 * result + imageLinks.hashCode()
        result = 31 * result + previewLink.hashCode()
        result = 31 * result + infoLink.hashCode()
        return result
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeStringArray(authors)
        parcel.writeString(publisher)
        parcel.writeParcelable(imageLinks, flags)
        parcel.writeString(previewLink)
        parcel.writeString(infoLink)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<VolumeInfo> {
        override fun createFromParcel(parcel: Parcel): VolumeInfo {
            return VolumeInfo(parcel)
        }

        override fun newArray(size: Int): Array<VolumeInfo?> {
            return arrayOfNulls(size)
        }
    }
}

/**
 * Book volume image links
 *
 * @param smallThumbnail
 * @param thumbnail
 */
data class ImageLinks(
    val smallThumbnail: String?,
    val thumbnail: String?
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(smallThumbnail)
        parcel.writeString(thumbnail)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ImageLinks> {
        override fun createFromParcel(parcel: Parcel): ImageLinks {
            return ImageLinks(parcel)
        }

        override fun newArray(size: Int): Array<ImageLinks?> {
            return arrayOfNulls(size)
        }
    }
}