package com.example.countriesapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.countriesapp.databinding.ListCardProductsBinding
import com.example.countriesapp.room.CountryEntity
import com.squareup.picasso.Picasso

class BorderCountriesAdapter(private var borderCountries: List<CountryEntity>) :
    RecyclerView.Adapter<BorderCountriesAdapter.BorderCountryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BorderCountryViewHolder {
        val binding =
            ListCardProductsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BorderCountryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BorderCountryViewHolder, position: Int) {
        val borderCountry = borderCountries[position]
        holder.bind(borderCountry)
    }

    override fun getItemCount(): Int = borderCountries.size

    inner class BorderCountryViewHolder(private val binding: ListCardProductsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(country: CountryEntity) {
            binding.tvCountry.text = country.name
            binding.tvCapital.text = country.capital
            Picasso.get().load(country.flags).into(binding.ivCountry)
        }
    }

    fun updateData(newData: List<CountryEntity>) {
        borderCountries = newData
        notifyDataSetChanged()
    }
}