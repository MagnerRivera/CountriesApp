package com.example.countriesapp.fragments.options

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.countriesapp.R
import com.example.countriesapp.adapter.CountriesAdapter
import com.example.countriesapp.animaionUtils.AnimationUtils
import com.example.countriesapp.databinding.FragmentHomeBinding
import com.example.countriesapp.retrofit.CountriesInstance
import com.example.countriesapp.retrofit.Country
import com.example.countriesapp.room.CountryEntity
import com.example.countriesapp.viewModels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "HomeFragment"

@AndroidEntryPoint
class HomeFragment : Fragment(), CountriesAdapter.OnCountryClickListener {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var editTextSearch: EditText
    private lateinit var progressBar: ProgressBar
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = binding.progressBar
        editTextSearch = binding.editTextSearch

        // Obtengo los países desde la base de datos
        getCountriesFromDatabase()

        binding.sortButton.setOnClickListener {
            (binding.rvCountries.adapter as? CountriesAdapter)?.sortByName()
        }

        if (isNetworkAvailable(requireContext())) {
            // Si hay conexión a Internet, cargo los datos desde el servicio
            getAllCountries()
        }
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    private fun getAllCountries() {
        val call = CountriesInstance.countryService.getAllCountries()
        progressBar.visibility = View.VISIBLE

        call.enqueue(object : Callback<List<Country>> {
            override fun onResponse(call: Call<List<Country>>, response: Response<List<Country>>) {
                if (response.isSuccessful) {
                    val countries = response.body()
                    countries?.let {
                        // Guardo los países en el ViewModel
                        viewModel.saveCountries(it)
                    }
                } else {
                    Log.e(TAG, "Failed to retrieve countries: ${response.code()}")
                    progressBar.visibility = View.INVISIBLE
                }
            }

            override fun onFailure(call: Call<List<Country>>, t: Throwable) {
                Log.e(TAG, "Error retrieving countries: ${t.message}")
                progressBar.visibility = View.INVISIBLE
            }
        })
    }

    override fun onCountryClicked(country: Any) {
        if (country is CountryEntity) {
            viewModel.getCountryByCode(country.cca3).observe(viewLifecycleOwner) { countryEntity ->
                Log.d(TAG, "Country details: $countryEntity")
                val bundle = Bundle().apply {
                    putString("cca3", countryEntity.cca3)
                    putString("name", countryEntity.name)
                    putInt("population", countryEntity.population)
                    putString("status", countryEntity.status)
                    putString("capital", countryEntity.capital)
                    putString("region", countryEntity.region)
                    putString("subregion", countryEntity.subregion)
                    putString("flags", countryEntity.flags)
                    putString("startOfWeek", countryEntity.startOfWeek)
                    putStringArrayList("continents", ArrayList(countryEntity.continents))
                    putStringArrayList("borders", ArrayList(countryEntity.borders))
                }
                findNavController().navigate(
                    R.id.action_homeFragment_to_detailCountryFragment,
                    bundle
                )
            }
        } else {
            Log.e(TAG, "Invalid country type: $country")
        }
    }


    private fun getCountriesFromDatabase() {
        // Obtengo los países desde la base de datos
        viewModel.getCountriesFromDatabase().observe(viewLifecycleOwner) { countries ->
            if (countries.isNotEmpty()) {
                // Si hay datos en la base de datos, lo muestro en la UI
                val adapter = CountriesAdapter(countries)
                binding.rvCountries.adapter = adapter
                binding.rvCountries.layoutManager = GridLayoutManager(requireContext(), 2)

                // Configuro el clic en el adaptador
                adapter.setOnCountryClickListener(this@HomeFragment)

                //metodo para el filter
                setupSearchFunctionality(adapter)
                // Ocultar el ProgressBar
                progressBar.visibility = View.INVISIBLE
            } else {
                // No hay datos en la base de datos, obtengo los países del servicio
                getAllCountries()
            }
        }
    }

    private fun setupSearchFunctionality(adapter: CountriesAdapter) {
        val imageSearch = binding.imageSearch
        val editTextSearch = binding.editTextSearch

        imageSearch.setOnClickListener {
            if (editTextSearch.visibility == View.VISIBLE) {
                AnimationUtils.slideViewUp(editTextSearch)
                editTextSearch.visibility = View.INVISIBLE
            } else {
                AnimationUtils.slideViewDown(editTextSearch)
                editTextSearch.visibility = View.VISIBLE
            }
        }

        editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.d(TAG, "beforeTextChanged: $s")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.filter.filter(s)
            }

            override fun afterTextChanged(s: Editable?) {
                Log.d(TAG, "afterTextChanged: $s")
            }
        })
    }

}