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
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.countriesapp.adapter.CountriesAdapter
import com.example.countriesapp.animaionUtils.AnimationUtils
import com.example.countriesapp.databinding.FragmentHomeBinding
import com.example.countriesapp.retrofit.CountriesInstance
import com.example.countriesapp.retrofit.Country
import com.example.countriesapp.viewModels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "HomeFragment"

@AndroidEntryPoint
class HomeFragment : Fragment() {

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

        if (isNetworkAvailable(requireContext())) {
            // Si hay conexión a Internet, cargar datos desde el servicio
            getAllCountries()
        } else {
            // Si no hay conexión a Internet, cargar datos desde la base de datos
            getCountriesFromDatabase()
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
                        // Guardar los países en el ViewModel
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

        // Observar el LiveData para actualizar la UI cuando los datos cambien
        viewModel.countriesLiveData.observe(viewLifecycleOwner) { countries ->
            progressBar.visibility = View.INVISIBLE

            val adapter = CountriesAdapter(countries)
            binding.rvCountries.adapter = adapter
            binding.rvCountries.apply {
                layoutManager = GridLayoutManager(requireContext(), 2)
            }
            //metodo para el filter
            setupSearchFunctionality(adapter)
        }
    }

    private fun getCountriesFromDatabase() {
        // Obtener los países desde la base de datos
        viewModel.getCountriesFromDatabase().observe(viewLifecycleOwner, Observer { countries ->
            if (countries.isNotEmpty()) {
                // Si hay datos en la base de datos, mostrarlos en la UI
                val adapter = CountriesAdapter(countries)
                binding.rvCountries.adapter = adapter
                binding.rvCountries.layoutManager = GridLayoutManager(requireContext(), 2)

                //metodo para el filter
                setupSearchFunctionality(adapter)

            } else {
                Toast.makeText(requireContext(), "No hay datos en la bd.", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "No countries found in database")
                setupSearchFunctionality(CountriesAdapter(emptyList()))
                binding.searchLayout.visibility = View.INVISIBLE
                binding.imageEmpty.visibility = View.VISIBLE
                binding.emptyPhotos.visibility = View.VISIBLE
            }
        })
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
                binding.imageEmpty.visibility = View.INVISIBLE
                binding.emptyPhotos.visibility = View.INVISIBLE
            }

            override fun afterTextChanged(s: Editable?) {
                Log.d(TAG, "afterTextChanged: $s")
            }
        })
    }

}