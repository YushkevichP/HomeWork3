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

    private val adapter by lazy {
        ItemAdapter(requireContext()) { item ->
            val personItem = item as? ItemType.CartoonPerson ?: return@ItemAdapter
            findNavController().navigate(
                ListFragmentDirections.toDetails(personItem.idApi)
            )
        }
    }

    private var requesrCall: Call<ResponseApi>? = null
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
            recyclerView.adapter = adapter
            recyclerView.layoutManager = layoutManager
            toolbar.setOnClickListener {
                refreshListToStart()
            }
        }

        swipeRefresh()
        makeRequest(pageCounter)

        with(binding) {
            recyclerView.addPaginationScrollListener(layoutManager, 2) {
                if (!isLoading) {
                    isLoading = true
                    makeRequest(pageCounter)
                }
            }
        }
    }

    private fun refreshListToStart(){
        pageCounter = 1
        finalFResultlist = emptyList()
        adapter.submitList(finalFResultlist)
        makeRequest(pageCounter)

    }

    private fun swipeRefresh() {
        binding.swipeLayout.setOnRefreshListener {
// при свайпе обновляем и сетим пустой лист и сразу делаем запрос на первую страничку
            pageCounter = 1
            finalFResultlist = emptyList()
            adapter.submitList(finalFResultlist)
            makeRequest(pageCounter)
            binding.swipeLayout.isRefreshing = false // крутелка убирается
        }
    }

    private fun makeRequest(pageForRequest: Int) {

        requesrCall = RickMortyService.personApi.getUsers(pageForRequest)
        requesrCall?.enqueue(object : Callback<ResponseApi> {
            override fun onResponse(
                call: Call<ResponseApi>,
                response: Response<ResponseApi>,
            ) {
                if (response.isSuccessful) {

                    val persons = response.body()?.results
                    val resultList = persons?.plus(ItemType.Loading) ?: return
                    val currentList = adapter.currentList.dropLast(1)
                    finalFResultlist = currentList + resultList
                    adapter.submitList(finalFResultlist)

                    isLoading = false
                    pageCounter++

                } else {
                    HttpException(response).message()
                }
                requesrCall = null
            }

            override fun onFailure(call: Call<ResponseApi>, t: Throwable) {
                Toast.makeText(requireContext(), t.message, Toast.LENGTH_SHORT)
                    .show()
                requesrCall = null
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        requesrCall?.cancel()
    }
}