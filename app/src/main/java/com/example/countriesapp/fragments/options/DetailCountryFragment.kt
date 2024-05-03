package com.example.countriesapp.fragments.options

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.countriesapp.R
import com.example.countriesapp.adapter.BorderCountriesAdapter
import com.example.countriesapp.databinding.FragmentDetailCountryBinding
import com.example.countriesapp.viewModels.DetailCountryViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.DecimalFormat

private const val TAG = "DetailCountryFragment"

@AndroidEntryPoint
class DetailCountryFragment : Fragment() {

    private lateinit var binding: FragmentDetailCountryBinding
    private val viewModel: DetailCountryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailCountryBinding.inflate(inflater)

        binding.imageClose.setOnClickListener {
            findNavController().navigateUp()
        }

        val args = arguments
        if (args != null) {
            val cca3 = args.getString("cca3")
            val name = args.getString("name")
            val population = args.getInt("population")
            val status = args.getString("status")
            val capital = args.getString("capital")
            val region = args.getString("region")
            val subregion = args.getString("subregion")
            val flags = args.getString("flags")
            val startOfWeek = args.getString("startOfWeek")
            val continents = args.getStringArrayList("continents")
            val borders = args.getStringArrayList("borders")

            binding.nameCountry.text = name
            binding.capital.text = capital.toString()
            binding.numberCode.text = cca3.toString()
            binding.stateName.text = status.toString()
            binding.regionName.text = region.toString()
            binding.subRegionName.text = subregion.toString()
            binding.startOfWeekName.text = startOfWeek.toString()
            binding.continentsName.text = continents.toString()

            if (flags != null) {
                Picasso.get()
                    .load(flags)
                    .fit()
                    .centerInside()
                    .into(binding.imageDetail)
            } else {
                binding.imageDetail.setImageResource(R.drawable.ic_country)
            }

            // Formateo la población antes de mostrarla
            binding.numberPopulation.text = formatPopulation(population)

            // Configuro el adaptador del RecyclerView
            val borderCountriesAdapter = BorderCountriesAdapter(ArrayList())
            binding.rvBorders.adapter = borderCountriesAdapter

            val layoutManager =
                GridLayoutManager(requireContext(), 2, LinearLayoutManager.VERTICAL, false)
            binding.rvBorders.layoutManager = layoutManager

            borders?.let { borderCodes ->
                lifecycleScope.launch {
                    val borderCountriesInfo = viewModel.getBorderCountriesInfo(borderCodes)
                    if (borderCountriesInfo.isEmpty()) {
                        binding.emptyBorders.visibility = View.VISIBLE
                        binding.rvBorders.visibility = View.GONE
                    } else {
                        for (countryInfo in borderCountriesInfo) {
                            Log.d(
                                TAG,
                                "Nombre: ${countryInfo.name}, Capital: ${countryInfo.capital}, Bandera: ${countryInfo.flags}"
                            )
                        }
                        binding.emptyBorders.visibility = View.GONE
                        binding.rvBorders.visibility = View.VISIBLE
                        borderCountriesAdapter.updateData(borderCountriesInfo)
                    }
                }
            }
        }

        return binding.root
    }

    // Función para formatear la población con puntos cada tres dígitos
    private fun formatPopulation(population: Int): String {
        val formatter = DecimalFormat("#,###")
        return formatter.format(population)
    }
}