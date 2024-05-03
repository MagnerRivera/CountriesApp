package com.example.countriesapp.adapter

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filterable
import android.widget.Filter
import androidx.recyclerview.widget.RecyclerView
import com.example.countriesapp.databinding.ListCardProductsBinding
import com.example.countriesapp.retrofit.Country
import com.example.countriesapp.room.CountryEntity
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.io.File
import java.io.FileOutputStream
import java.util.Locale

class CountriesAdapter(private var countries: List<Any>) :
    RecyclerView.Adapter<CountriesAdapter.CountryViewHolder>(), Filterable {

    private var filteredCountries: List<Any> = countries
    private var onCountryClickListener: OnCountryClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val binding =
            ListCardProductsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CountryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        val country = filteredCountries[position]
        holder.bind(country)
    }

    override fun getItemCount(): Int = filteredCountries.size

    inner class CountryViewHolder(private val binding: ListCardProductsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val country = filteredCountries[position]
                    onCountryClickListener?.onCountryClicked(country)
                }
            }
        }

        fun bind(country: Any) {
            if (country is Country) {
                bindCountry(country)
            } else if (country is CountryEntity) {
                bindCountryEntity(country)
            }
        }

        private fun bindCountry(country: Country) {
            // Utilizo las llamadas seguras para evitar NullPointerException
            binding.tvCountry.text = country.name.common
            binding.tvCapital.text = country.capital.firstOrNull() ?: "Unknown"
            // Cargo la imagen usando Picasso
            Picasso.get().load(country.flags.png).into(binding.ivCountry)
        }

        private fun bindCountryEntity(countryEntity: CountryEntity) {
            binding.tvCountry.text = countryEntity.name
            binding.tvCapital.text = countryEntity.capital

            val context = binding.root.context
            val imageName = countryEntity.flags.split("/").last()
            val imagePath = File(context.cacheDir, imageName)

            if (imagePath.exists()) {
                // Cargo la imagen de manera local con Picasso
                Picasso.get().load(imagePath).into(binding.ivCountry)
            } else {
                // Cargo la imagen desde la web y la guardo localmente
                Picasso.get().load(countryEntity.flags).into(binding.ivCountry, object : Callback {
                    override fun onSuccess() {
                        // Guardo la imagen de manera local
                        try {
                            val outputStream = FileOutputStream(imagePath)
                            val bitmap = (binding.ivCountry.drawable as BitmapDrawable).bitmap
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                            outputStream.flush()
                            outputStream.close()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }

                    override fun onError(e: Exception?) {
                        e?.printStackTrace()
                    }
                })
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint.toString()
                filteredCountries = if (charString.isEmpty()) {
                    countries
                } else {
                    val filteredList = mutableListOf<Any>()
                    for (country in countries) {
                        if (country is Country && (country.name.common.lowercase(Locale.ROOT)
                                .contains(
                                    charString.lowercase(
                                        Locale.ROOT
                                    )
                                ) || country.capital.firstOrNull()?.lowercase(Locale.ROOT)
                                ?.contains(
                                    charString.lowercase(Locale.ROOT)
                                ) == true)
                        ) {
                            filteredList.add(country)
                        } else if (country is CountryEntity && (country.name.lowercase(Locale.ROOT)
                                .contains(
                                    charString.lowercase(Locale.ROOT)
                                ) ||
                                    country.capital.lowercase(Locale.ROOT).contains(
                                        charString.lowercase(
                                            Locale.ROOT
                                        )
                                    ))
                        ) {
                            filteredList.add(country)
                        }
                    }
                    filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = filteredCountries
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredCountries = results?.values as? List<Any> ?: listOf()
                notifyDataSetChanged()
            }
        }
    }

    fun setOnCountryClickListener(listener: OnCountryClickListener) {
        this.onCountryClickListener = listener
    }

    //Metodo para organizar el recycler de forma alfabetica
    fun sortByName() {
        filteredCountries = filteredCountries.sortedBy {
            when (it) {
                is Country -> it.name.common
                is CountryEntity -> it.name
                else -> ""
            }
        }
        notifyDataSetChanged()
    }

    interface OnCountryClickListener {
        fun onCountryClicked(country: Any)
    }
}
