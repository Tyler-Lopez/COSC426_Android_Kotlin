package com.company.weatherapplication.presentation.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.company.weatherapplication.R
import com.company.weatherapplication.data.City
import com.company.weatherapplication.databinding.ItemCityBinding

class CityAdapter(
    // Take list, set data of list to corresponding items of recycler view
    private val cities: MutableList<City>,
    private val context: Context,
    val celsiusLiveData: LiveData<Boolean>,
    val lifecycleOwner: LifecycleOwner,
    val deleteItemOnLongClick: (Int) -> Unit,
    val onClick: (City) -> Unit
) : RecyclerView.Adapter<CityAdapter.CityViewHolder>() {

    inner class CityViewHolder(val binding: ItemCityBinding) : RecyclerView.ViewHolder(binding.root)

    // Invoked when Recycler View needs a new View Holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val layoutInflater = LayoutInflater
            .from(parent.context)
        val binding = ItemCityBinding.inflate(layoutInflater, parent, false)
        return CityViewHolder(binding)
    }

    // Bind data to our items
    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        holder.binding.apply {
            tvTitle.text = cities[position].cityName
            tvSubtext.text = cities[position].weatherCondition
            // Initially set to F
            tvTemp.text = "%.1f".format((cities[position].temp - 273.15) * (9.0 / 5.0) + 32)
            // Listen for changes to C and back to F
            val celsiusObserver = Observer<Boolean> {
                tvTemp.text = when (it) {
                    true -> "%.1f".format(cities[position].temp - 273.15)
                    false -> "%.1f".format((cities[position].temp - 273.15) * (9.0 / 5.0) + 32)
                }
            }

            celsiusLiveData.observe(lifecycleOwner, celsiusObserver)

            cardBackground.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    when (cities[position].weatherCondition) {
                        "Thunderstorm" -> R.drawable.gradient_thunderstorm
                        "Drizzle" -> R.drawable.gradient_drizzle
                        "Rain" -> R.drawable.gradient_rain
                        "Snow" -> R.drawable.gradient_snow
                        "Clouds" -> R.drawable.gradient_clouds
                        "Clear" -> R.drawable.gradient_clear
                        else -> R.drawable.gradient_clouds
                    }
                )
            )
        }
        // On click, callback the City
        holder.itemView.setOnClickListener {
            onClick(cities[position])
        }

        // On long click, deletes city
        holder.itemView.setOnLongClickListener()
        {
            deleteItemOnLongClick(position)
            true
        }
    }

    override fun getItemCount(): Int = cities.size

}