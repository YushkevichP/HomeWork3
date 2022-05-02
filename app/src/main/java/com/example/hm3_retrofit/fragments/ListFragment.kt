package com.example.hm3_retrofit.fragments

import android.graphics.Rect
import retrofit2.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hm3_retrofit.R
import com.example.hm3_retrofit.adapter.PersonAdapter
import com.example.hm3_retrofit.databinding.FragmentListBinding
import com.example.hm3_retrofit.model.ResponseApi
import com.example.hm3_retrofit.retrofit.RickMortyService


class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding: FragmentListBinding
        get() = requireNotNull(_binding) {
            "View was destroyed"
        }


    private val myAdapter by lazy {

        PersonAdapter() {
            findNavController().navigate(
                ListFragmentDirections.toDetails(it.idApi)
            )
        }

    }


    private var currentRequest: Call<ResponseApi>? = null
    private var pageCounter = 3

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return FragmentListBinding.inflate(inflater, container, false)
            .also { _binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecycler(view)
        makeRequest(view)

        binding.swipeLayout.setOnRefreshListener {
          //  myAdapter.submitList(emptyList())  -- это чтоб обнулить ресайклер - сделать пустым.
            pageCounter = 1
            makeRequest(view)
            binding.swipeLayout.isRefreshing = false // крутелка убирается
        }
    }

    private fun initRecycler(view: View) {
        with(binding) {
            recyclerView.addSpaceDecoration(resources.getDimensionPixelSize(R.dimen.bottom_space))
//            myAdapter.submitList(emptyList())
//            recyclerView.adapter = myAdapter
//            recyclerView.layoutManager = LinearLayoutManager(view.context)
        }
    }

    private fun makeRequest(view: View) {
        val request = RickMortyService.personApi.getUsers(pageCounter)
        request.enqueue(object : Callback<ResponseApi> {
            override fun onResponse(call: Call<ResponseApi>, response: Response<ResponseApi>) {
                if (response.isSuccessful) {
                    val persons = response.body()?.results
                    with(binding) {
                        myAdapter.submitList(persons)
                        recyclerView.adapter = myAdapter
                        recyclerView.layoutManager = LinearLayoutManager(view.context)
                    }
                } else {
                    HttpException(response).message()
                }
                currentRequest = null
            }

            override fun onFailure(call: Call<ResponseApi>, t: Throwable) {
                Toast.makeText(requireContext(), t.message, Toast.LENGTH_SHORT)
                    .show()
                currentRequest = null

            }
        })
        currentRequest = request
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        currentRequest?.cancel()
    }
}