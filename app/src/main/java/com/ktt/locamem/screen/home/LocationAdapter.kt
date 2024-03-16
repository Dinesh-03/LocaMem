package com.ktt.locamem.screen.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ktt.locamem.databinding.ItemLocationBinding
import com.ktt.locamem.model.LocationData
import org.mongodb.kbson.ObjectId
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class LocationAdapter(
    private val context: Context,
    private var locations: List<LocationData>,
    private val locationItemListener: LocationItemListener
) :
    RecyclerView.Adapter<LocationAdapter.LocationViewHolder>() {

    interface LocationItemListener {
        fun onClickedLocation(location: LocationData)
    }

    fun updateLocations(locations: List<LocationData>) {
        this.locations = locations
        notifyDataSetChanged()
    }

    inner class LocationViewHolder(
        private val locationBinding: ItemLocationBinding,
        private val locationItemListener: LocationItemListener
    ) :
        RecyclerView.ViewHolder(locationBinding.root) {
        private lateinit var locationData: LocationData

        init {
            locationBinding.root.setOnClickListener {
                locationItemListener.onClickedLocation(locationData)
            }
        }

        fun bind(locationData: LocationData) {
            this.locationData = locationData
            locationBinding.latitude.text = buildString {
                append("Latitude: ")
                append(locationData.latitude)
            }
            locationBinding.longitude.text = buildString {
                append("Longitude: ")
                append(locationData.longitude)
            }
            val instant = Instant.ofEpochMilli(locationData.timeStamp)
            val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
            val formattedDate = localDateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
            val formattedTime = localDateTime.format(DateTimeFormatter.ofPattern("hh:mm a"))
            locationBinding.date.text = formattedDate
            locationBinding.time.text = formattedTime
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val binding: ItemLocationBinding =
            ItemLocationBinding.inflate(LayoutInflater.from(context), parent, false)
        return LocationViewHolder(binding, locationItemListener)
    }

    override fun getItemCount() = locations.size

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) =
        holder.bind(locations[position])
}