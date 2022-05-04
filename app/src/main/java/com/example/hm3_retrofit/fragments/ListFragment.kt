package com.example.hm3_retrofit.fragments

import retrofit2.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hm3_retrofit.R
import com.example.hm3_retrofit.adapter.ItemAdapter
import com.example.hm3_retrofit.databinding.FragmentListBinding
import com.example.hm3_retrofit.model.ItemType
import com.example.hm3_retrofit.model.ResponseApi
import com.example.hm3_retrofit.retrofit.RickMortyService


class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding: FragmentListBinding
        get() = requireNotNull(_binding) {
            "View was destroyed"
        }

    private val myAdapter by lazy {
        ItemAdapter { item ->
            val personItem = item as? ItemType.CartoonPerson ?: return@ItemAdapter
            findNavController().navigate(
                ListFragmentDirections.toDetails(personItem.idApi)
            )
        }
    }

    private var currentRequest: Call<ResponseApi>? = null
    private var pageCounter = 1
    private var isLoading = false
    private var finalFResultlist: List<ItemType> = emptyList()

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

        val layoutManager = LinearLayoutManager(view.context)
        //recycler init
        with(binding) {
            recyclerView.addSpaceDecoration(resources.getDimensionPixelSize(R.dimen.bottom_space))
            recyclerView.adapter = myAdapter
            recyclerView.layoutManager = layoutManager
        }

        swipeRefresh(view)
        makeRequest(1)

        with(binding) {
            recyclerView.addPaginationScrollListener(layoutManager, 2) {
                if (!isLoading) {
                    isLoading = true
                    makeRequest(pageCounter++)
                    isLoading = false
                }
            }
        }
    }

    private fun swipeRefresh(view: View) {
        binding.swipeLayout.setOnRefreshListener {

            makeRequest(1)
            binding.swipeLayout.isRefreshing = false // крутелка убирается
        }
    }


    private fun makeRequest(pageForRequest: Int) { //TODO need to return list of persons

        val request = RickMortyService.personApi.getUsers(pageForRequest)
        request.enqueue(object : Callback<ResponseApi> {
            override fun onResponse(
                call: Call<ResponseApi>,
                response: Response<ResponseApi>,
            ) {
                if (response.isSuccessful) {
                    val persons = response.body()?.results
                    val resultList = persons?.plus(ItemType.Loading) ?: return
                    val currentList = myAdapter.currentList.dropLast(1)
                    finalFResultlist = currentList + resultList
                    myAdapter.submitList(finalFResultlist)

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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        currentRequest?.cancel()
    }
}