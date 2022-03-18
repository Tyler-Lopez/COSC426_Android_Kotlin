package com.company.weatherapplication.presentation.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.company.weatherapplication.data.City
import com.company.weatherapplication.databinding.ItemCityBinding

class CityAdapter(
    // Take list, set data of list to corresponding items of recycler view
    private val cities: MutableList<City>,
    val onClick: (City) -> Unit
): RecyclerView.Adapter<CityAdapter.CityViewHolder>() {

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
            tvTemp.text = "${cities[position].temp}"
        }
        // On click, callback the City
        holder.itemView.setOnClickListener {
            onClick(cities[position])
        }
    }

    override fun getItemCount(): Int = cities.size

}